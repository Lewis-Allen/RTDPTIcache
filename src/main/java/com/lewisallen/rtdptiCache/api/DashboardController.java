package com.lewisallen.rtdptiCache.api;

import com.google.gson.Gson;
import com.lewisallen.rtdptiCache.caches.BusCodesCache;
import com.lewisallen.rtdptiCache.caches.BusDataCache;
import com.lewisallen.rtdptiCache.caches.TrainCodesCache;
import com.lewisallen.rtdptiCache.caches.TrainDataCache;
import com.lewisallen.rtdptiCache.logging.ErrorHandler;
import com.lewisallen.rtdptiCache.logging.ResourceNotFoundException;
import com.lewisallen.rtdptiCache.models.Bus;
import com.lewisallen.rtdptiCache.models.Dashboard;
import com.lewisallen.rtdptiCache.models.Station;
import com.lewisallen.rtdptiCache.repositories.DashboardRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping(value = "dashboard")
public class DashboardController
{
    private final DashboardRepository repository;

    public DashboardController(DashboardRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping(value = "create")
    public String showDashboardCreate(Model model)
    {
        SortedMap<String, Bus> busesCopy = new TreeMap<>(BusCodesCache.busCodeCache);
        SortedMap<String, Station> stationCopy = new TreeMap<>(TrainCodesCache.stationCache);

        // ToDo: Sort the maps based on name
        model.addAttribute("buses", busesCopy);
        model.addAttribute("stations", stationCopy);

        Path path = Paths.get("templates.txt");
        List<String> availableTemplates = getListOfTemplates(path);
        model.addAttribute("availableTemplates", availableTemplates);

        return "create";
    }

    /**
     * Retrieves list of files in dashboard template directory.
     *
     * @param path Path to dashboard template directory.
     * @return List of files.
     */
    public List<String> getListOfTemplates(Path path)
    {
        List<String> templates = new ArrayList<>();

        try
        {
            Stream<String> templateStream = Files.lines(path);
            templates = templateStream.collect(Collectors.toList());
        }
        catch (IOException e)
        {
            ErrorHandler.handle(e, Level.WARNING, "Error parsing dashboard template list. File should be located in the root directory.");
        }

        return templates;
    }

    @GetMapping(value = "/{dashboardId}")
    public String showDashboard(@PathVariable Long dashboardId,
                                UriComponentsBuilder builder,
                                Model model)
    {
        Optional<Dashboard> dashboardOptional = repository.findById(dashboardId);

        if (dashboardOptional.isPresent())
        {
            Dashboard dashboard = dashboardOptional.get();
            String[] codes = new String[0];
            String[] crs = new String[0];


            // Parse data
            JSONObject dashboardData = new JSONObject(dashboard.getData());
            JSONObject departureInfoRequest = new JSONObject();
            if (dashboardData.has("buses"))
            {
                int length = dashboardData.getJSONArray("buses").length();
                codes = new String[length];
                for (int i = 0; i < length; i++)
                {
                    codes[i] = dashboardData.getJSONArray("buses").getString(i);
                }
                departureInfoRequest.put("codes", Arrays.asList(codes));
            }

            if (dashboardData.has("trains"))
            {
                int length = dashboardData.getJSONArray("trains").length();
                crs = new String[length];
                for (int i = 0; i < length; i++)
                {
                    crs[i] = dashboardData.getJSONArray("trains").getString(i);
                }
                departureInfoRequest.put("crs", Arrays.asList(crs));
            }

            JSONObject departureInformation = getDepartureInformation(departureInfoRequest);

            // Fill in stop name for any locations that have no visits.
            List<String> removedBusCodes = new ArrayList<>();
            if (departureInformation.getJSONObject("payload").has("busStops"))
            {
                JSONObject codesArray = departureInformation.getJSONObject("payload").getJSONObject("busStops");

                // Make a copy of the codes array to avoid a concurrent modification exception.
                JSONObject newCodesArray = new JSONObject(codesArray, JSONObject.getNames(codesArray));

                for (String stopCode : codesArray.keySet())
                {
                    if (newCodesArray.getJSONObject(stopCode).isEmpty())
                    {
                        if (BusCodesCache.checkStopExists(stopCode))
                        {
                            JSONObject objForStop = newCodesArray.getJSONObject(stopCode);
                            Bus bus = BusCodesCache.getBus(stopCode);

                            objForStop.put("StopName", bus.getLongDescription());
                            objForStop.put("Identifier", bus.getIdentifier());
                            objForStop.put("MonitoredStopVisits", new ArrayList<>());
                            newCodesArray.put(stopCode, objForStop);
                        }
                        else
                        {
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
            if (departureInformation.getJSONObject("payload").has("trainStations"))
            {
                JSONObject codesArray = departureInformation.getJSONObject("payload").getJSONObject("trainStations");

                // Make a copy of the codes array to avoid a concurrent modification exception.
                JSONObject newCodesArray = new JSONObject(codesArray, JSONObject.getNames(codesArray));

                for (String stationCode : codesArray.keySet())
                {
                    if (newCodesArray.getJSONObject(stationCode).isEmpty())
                    {
                        if (TrainCodesCache.checkStopExists(stationCode))
                        {
                            JSONObject objForStop = newCodesArray.getJSONObject(stationCode);
                            Station station = TrainCodesCache.getStation(stationCode);

                            objForStop.put("stationName", station.getStationName());
                            objForStop.put("departures", new ArrayList<>());
                            newCodesArray.put(stationCode, objForStop);
                        }
                        else
                        {
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
            if (dashboardData.has("buses"))
            {
                modelBusCodes = new ArrayList<>(Arrays.asList(codes));
                modelBusCodes.removeAll(removedBusCodes);
            }
            model.addAttribute("busCodes", modelBusCodes);

            List<String> modelTrainCodes = new ArrayList<>();
            if (dashboardData.has("trains"))
            {
                modelTrainCodes = new ArrayList<>(Arrays.asList(crs));
                modelTrainCodes.removeAll(removedTrainCodes);
            }
            model.addAttribute("trainCodes", modelTrainCodes);

            // Thymeleaf apparently doesn't like transversing JSON...
            // This converts the json back into a 'java object' which Thymeleaf doesn't complain about.
            Gson gson = new Gson();
            Object departureInformation2 = gson.fromJson(departureInformation.toString(), Object.class);
            model.addAttribute("departureInformation", departureInformation2);

            // Add clock attributes.
            model.addAttribute("localDateTime", LocalDateTime.now());

            String template = dashboard.getTemplate();

            // Add the switch URL if applicable.
            Long switchId = dashboard.getSwitchId();
            if (switchId != null)
            {
                model.addAttribute("flipUrl", builder.path("/dashboard/" + switchId)
                        .build()
                        .toUriString());
            }

            String overrideName = dashboard.getOverrideName();
            if (overrideName != null)
                model.addAttribute("name", overrideName);

            dashboard.setLastUsedDate(LocalDateTime.now());
            repository.saveAndFlush(dashboard);

            return "dashboardTemplates/" + template;

        }
        else
        {
            throw new ResourceNotFoundException("No resource found for this URL.");
        }
    }

    @PostMapping(value = "")
    public RedirectView createNewDashboard(@RequestBody String request)
    {
        JSONObject json = new JSONObject(request);

        String template = json.has("template") ? json.getString("template") : "default";
        String name = json.has("name") ? json.getString("name") : null;
        String flipTo = json.has("flipTo") ? json.getString("flipTo") : null;

        Optional<Dashboard> existingDashboard = repository.findDashboardByData(request);
        if (existingDashboard.isPresent())
        {
            return new RedirectView("dashboard/" + existingDashboard.get().getId());
        }
        else
        {
            Dashboard dashboard = new Dashboard(template, name, request);
            repository.saveAndFlush(dashboard);

            // We create another URL for a template we flip to.
            if (flipTo != null)
            {
                // Swap the flipTo and template in the raw data.
                JSONObject requestCopyJSON = new JSONObject(request);
                requestCopyJSON.put("flipTo", template);
                requestCopyJSON.put("template", flipTo);

                Dashboard flipToDashboard = new Dashboard(flipTo, name, requestCopyJSON.toString(), dashboard.getId());
                repository.saveAndFlush(flipToDashboard);

                dashboard.setSwitchId(flipToDashboard.getId());

                repository.saveAndFlush(dashboard);
            }

            return new RedirectView("dashboard/" + dashboard.getId());
        }
    }

    private JSONObject getDepartureInformation(JSONObject request)
    {
        // Create a JSON Object to hold the response.
        JSONObject k = new JSONObject();

        // Create a list to hold bus stops and train stations.
        JSONObject busesAndTrains = new JSONObject();

        // Add any bus stops to the JSON
        if (request.has("codes"))
        {
            JSONArray busCodeList = request.getJSONArray("codes");

            List<String> busCodes = new ArrayList<>();
            for (int i = 0; i < busCodeList.length(); i++)
            {
                busCodes.add(busCodeList.get(i).toString());
            }

            busesAndTrains.put("busStops", BusDataCache.getSiriJson(busCodes.stream().toArray(String[]::new)).get("busStops"));
        }

        // Add any train stations to the JSON
        if (request.has("crs"))
        {
            JSONArray trainCodeList = request.getJSONArray("crs");

            List<String> trainCodes = new ArrayList<>();
            for (int i = 0; i < trainCodeList.length(); i++)
            {
                trainCodes.add(trainCodeList.get(i).toString());
            }

            busesAndTrains.put("trainStations", TrainDataCache.getTrainJSON(trainCodes.stream()
                    .toArray(String[]::new)).get("trainStations"));
        }

        // Wrap all JSON into one object
        k.put("payload", busesAndTrains);

        return k;
    }
}
