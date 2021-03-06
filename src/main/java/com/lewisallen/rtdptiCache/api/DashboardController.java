package com.lewisallen.rtdptiCache.api;

import com.google.gson.Gson;
import com.lewisallen.rtdptiCache.caches.BusCodesCache;
import com.lewisallen.rtdptiCache.caches.Caches;
import com.lewisallen.rtdptiCache.caches.TrainCodesCache;
import com.lewisallen.rtdptiCache.logging.ResourceNotFoundException;
import com.lewisallen.rtdptiCache.models.Bus;
import com.lewisallen.rtdptiCache.models.Dashboard;
import com.lewisallen.rtdptiCache.models.Station;
import com.lewisallen.rtdptiCache.models.Template;
import com.lewisallen.rtdptiCache.repositories.DashboardRepository;
import com.lewisallen.rtdptiCache.repositories.TemplateRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping(value = "dashboard")
public class DashboardController {
    private final DashboardRepository dashboardRepository;
    private final TemplateRepository templateRepository;

    public DashboardController(DashboardRepository dashboardRepository, TemplateRepository templateRepository) {
        this.dashboardRepository = dashboardRepository;
        this.templateRepository = templateRepository;
    }

    @GetMapping(value = "create")
    public String showDashboardCreate(Model model) {
        SortedMap<String, Bus> busesCopy = new TreeMap<>(BusCodesCache.busCodeCache);
        SortedMap<String, Station> stationCopy = new TreeMap<>(TrainCodesCache.stationCache);

        model.addAttribute("buses", busesCopy);
        model.addAttribute("stations", stationCopy);

        List<String> availableTemplates = getListOfTemplates();
        model.addAttribute("availableTemplates", availableTemplates);

        return "create";
    }

    /**
     * Retrieves list of templates from database.
     *
     * @return List of files.
     */
    private List<String> getListOfTemplates() {
        List<Template> templates = templateRepository.findAll();
        List<String> templateNames = new ArrayList<>();

        templates.forEach((template) -> templateNames.add(template.getTemplateName()));

        return templateNames;
    }

    @GetMapping(value = "/{dashboardId}")
    public String showDashboard(@PathVariable Long dashboardId,
                                UriComponentsBuilder builder,
                                Model model) {

        // TODO: split up into methods based on separated code

        Optional<Dashboard> dashboardOptional = dashboardRepository.findById(dashboardId);

        if (!dashboardOptional.isPresent()) {
            throw new ResourceNotFoundException();
        }

        Dashboard dashboard = dashboardOptional.get();
        String[] codes = new String[0];
        String[] crs = new String[0];

        // Parse data
        JSONObject dashboardData = new JSONObject(dashboard.getData());
        JSONObject departureInfoRequest = new JSONObject();

        if (dashboardData.has("buses")) {
            codes = parseCodes(dashboardData, "buses");
            departureInfoRequest.put("codes", Arrays.asList(codes));
        }

        if (dashboardData.has("trains")) {
            crs = parseCodes(dashboardData, "trains");
            departureInfoRequest.put("crs", Arrays.asList(crs));
        }

        JSONObject departureInformation = getDepartureInformation(departureInfoRequest);


        // Fill in stop name for any locations that have no visits.
        List<String> removedBusCodes = new ArrayList<>();
        if (departureInformation.getJSONObject("payload").has("busStops")) {
            JSONObject codesArray = departureInformation.getJSONObject("payload").getJSONObject("busStops");

            // Make a copy of the codes array to avoid a concurrent modification exception.
            JSONObject newCodesArray = new JSONObject(codesArray, JSONObject.getNames(codesArray));

            for (String stopCode : codesArray.keySet()) {
                if (newCodesArray.getJSONObject(stopCode).isEmpty()) {
                    if (BusCodesCache.checkStopExists(stopCode)) {
                        JSONObject objForStop = newCodesArray.getJSONObject(stopCode);
                        Bus bus = BusCodesCache.getBus(stopCode);

                        objForStop.put("StopName", bus.getLongDescription());
                        objForStop.put("Identifier", bus.getIdentifier());
                        objForStop.put("MonitoredStopVisits", new ArrayList<>());
                        newCodesArray.put(stopCode, objForStop);
                    } else {
                        removedBusCodes.add(stopCode);
                        newCodesArray.remove(stopCode);
                    }
                }
            }

            // Replace the bus stops array with our new codes array.
            departureInformation.getJSONObject("payload").put("busStops", newCodesArray);
        }

        // Fill in trains that have no visit.
        List<String> removedTrainCodes = new ArrayList<>();
        if (departureInformation.getJSONObject("payload").has("trainStations")) {
            JSONObject codesArray = departureInformation.getJSONObject("payload").getJSONObject("trainStations");

            // Make a copy of the codes array to avoid a concurrent modification exception.
            JSONObject newCodesArray = new JSONObject(codesArray, JSONObject.getNames(codesArray));

            for (String stationCode : codesArray.keySet()) {
                if (newCodesArray.getJSONObject(stationCode).isEmpty()) {
                    if (TrainCodesCache.checkStopExists(stationCode)) {
                        JSONObject objForStop = newCodesArray.getJSONObject(stationCode);
                        Station station = TrainCodesCache.getStation(stationCode);

                        objForStop.put("stationName", station.getStationName());
                        objForStop.put("departures", new ArrayList<>());
                        newCodesArray.put(stationCode, objForStop);
                    } else {
                        removedTrainCodes.add(stationCode);
                        newCodesArray.remove(stationCode);
                    }
                }
            }

            departureInformation.getJSONObject("payload").put("trainStations", newCodesArray);
        }


        // Add list of codes to the model, making sure we remove any codes that were removed previously.
        // We use this so we can add stops in the correct order on the template.
        List<String> modelBusCodes = new ArrayList<>();
        if (dashboardData.has("buses")) {
            modelBusCodes = new ArrayList<>(Arrays.asList(codes));
            modelBusCodes.removeAll(removedBusCodes);
        }
        model.addAttribute("busCodes", modelBusCodes);

        List<String> modelTrainCodes = new ArrayList<>();
        if (dashboardData.has("trains")) {
            modelTrainCodes = new ArrayList<>(Arrays.asList(crs));
            modelTrainCodes.removeAll(removedTrainCodes);
        }
        model.addAttribute("trainCodes", modelTrainCodes);


        // Thymeleaf apparently doesn't like transversing JSON...
        // This converts the json back into a 'java object' which Thymeleaf doesn't complain about.

        // TODO: duplication in here + TimetableController::showTimetable

        Gson gson = new Gson();
        Object departureInformation2 = gson.fromJson(departureInformation.toString(), Object.class);
        model.addAttribute("departureInformation", departureInformation2);

        // Add clock attributes.
        model.addAttribute("localDateTime", LocalDateTime.now());

        String template = dashboard.getTemplate();

        // Add the switch URL if applicable.
        Long switchId = dashboard.getSwitchId();
        if (switchId != null) {
            model.addAttribute("flipUrl", builder.path("/dashboard/" + switchId)
                    .build()
                    .toUriString());
        }

        String overrideName = dashboard.getOverrideName();
        if (overrideName != null)
            model.addAttribute("name", overrideName);

        dashboard.setLastUsedDate(LocalDateTime.now());
        dashboardRepository.saveAndFlush(dashboard);

        return "dashboardTemplates/" + template;
    }

    @PostMapping(value = "")
    public RedirectView createNewDashboard(@RequestBody String request) {
        JSONObject json = new JSONObject(request);

        String template = json.has("template") ? json.getString("template") : "default";
        String name = json.has("name") ? json.getString("name") : null;
        String flipTo = json.has("flipTo") ? json.getString("flipTo") : null;

        Optional<Dashboard> existingDashboard = dashboardRepository.findDashboardByData(request);
        if (existingDashboard.isPresent()) {
            return new RedirectView("dashboard/" + existingDashboard.get().getId());
        }

        Dashboard dashboard = new Dashboard(template, name, request);
        dashboardRepository.saveAndFlush(dashboard);

        // We create another URL for a template we flip to.
        if (flipTo != null) {
            // Swap the flipTo and template in the raw data.
            JSONObject requestCopyJSON = new JSONObject(request);
            requestCopyJSON.put("flipTo", template);
            requestCopyJSON.put("template", flipTo);

            Dashboard flipToDashboard = new Dashboard(flipTo, name, requestCopyJSON.toString(), dashboard.getId());
            dashboardRepository.saveAndFlush(flipToDashboard);

            dashboard.setSwitchId(flipToDashboard.getId());

            dashboardRepository.saveAndFlush(dashboard);
        }

        return new RedirectView("dashboard/" + dashboard.getId());
    }

    private JSONObject getDepartureInformation(JSONObject request) {
        // TODO: duplication in this method and in StopController::stops

        // Create a JSON Object to hold the response.
        JSONObject response = new JSONObject();

        // Create a list to hold bus stops and train stations.
        JSONObject busesAndTrains = new JSONObject();

        // Add any bus stops to the JSON
        if (request.has("codes")) {
            JSONArray busCodeList = request.getJSONArray("codes");

            List<String> busCodes = new ArrayList<>();
            for (int i = 0; i < busCodeList.length(); i++) {
                busCodes.add(busCodeList.get(i).toString());
            }

            busesAndTrains.put("busStops", Caches.getSiriJSON(busCodes.stream().toArray(String[]::new)).get("busStops"));
        }

        // Add any train stations to the JSON
        if (request.has("crs")) {
            JSONArray trainCodeList = request.getJSONArray("crs");

            List<String> trainCodes = new ArrayList<>();
            for (int i = 0; i < trainCodeList.length(); i++) {
                trainCodes.add(trainCodeList.get(i).toString());
            }

            busesAndTrains.put("trainStations", Caches.getTrainJSON(trainCodes.stream().toArray(String[]::new)).get("trainStations"));
        }

        // Wrap all JSON into one object
        response.put("payload", busesAndTrains);

        return response;
    }

    private String[] parseCodes(JSONObject data, String identifier) {
        String[] codes = new String[0];
        if (data.has(identifier)) {
            int length = data.getJSONArray(identifier).length();
            codes = new String[length];
            for (int i = 0; i < length; i++) {
                codes[i] = data.getJSONArray(identifier).getString(i);
            }
        }
        return codes;
    }
}
