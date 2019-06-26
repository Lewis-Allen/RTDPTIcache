package com.lewisallen.rtdptiCache.api;

import com.google.gson.Gson;
import com.lewisallen.rtdptiCache.caches.NaPTANCache;
import com.lewisallen.rtdptiCache.caches.SIRICache;
import com.lewisallen.rtdptiCache.caches.TrainDepartureCache;
import com.lewisallen.rtdptiCache.caches.TrainStationCache;
import com.lewisallen.rtdptiCache.logging.ErrorHandler;
import com.lewisallen.rtdptiCache.models.Naptan;
import com.lewisallen.rtdptiCache.models.Station;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class ViewController
{
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showTitle()
    {
        return "title";
    }

    @RequestMapping(value = "/dashboard/create", method = RequestMethod.GET)
    public String showDashboardCreate(Model model)
    {
        SortedMap<String, Naptan> busesCopy = new TreeMap<>(NaPTANCache.naptanCache);
        SortedMap<String, Station> stationCopy = new TreeMap<>(TrainStationCache.stationCache);

        // ToDo: Sort the maps based on name
        model.addAttribute("buses", busesCopy);
        model.addAttribute("stations", stationCopy);

        Path path = Paths.get( "templates.txt");
        List<String> availableTemplates = getListOfTemplates(path);
        model.addAttribute("availableTemplates", availableTemplates);

        return "create";
    }

    /**
     * Retrieves list of files in dashboard template directory.
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

    @GetMapping(value = "/timetable")
    public String showLoadTemplateScreen(Model model)
    {
        return "uploadForm";
    }



    @RequestMapping(value = "/timetable", method = RequestMethod.POST)
    public String loadTemplate(Model model, @RequestBody String timetable)
    {
        String timetableString = timetable.trim().split("=")[1];

        model.addAttribute("formContent", timetableString);
        JSONObject o = new JSONObject();

        String[] lines = timetableString.split(System.lineSeparator());
        o.put("stopName", lines[0]);

        List<String> linesAsList = Arrays.asList(lines);
        LocalTime now = LocalTime.now();

        // Get only departures in future.
        List<String> linesToDisplay = linesAsList.stream().skip(1)
                .filter(line -> LocalTime.parse(line.split(",")[0]).isAfter(now)).collect(Collectors.toList());

        JSONArray stops = new JSONArray();
        for(int i = 0; i < linesToDisplay.size() && i < 9; i++)
        {
            String[] visit = linesToDisplay.get(i).split(",");
            JSONObject stop = new JSONObject();
            stop.put("Departure", visit[0]);
            stop.put("Vehicle", visit[1]);
            stop.put("Destination", visit[2]);
            stops.put(stop);
        }

        o.put("visits", stops);
        JSONObject wrapper = new JSONObject();
        wrapper.put("payload", o);

        Gson gson = new Gson();
        Object departureInformation = gson.fromJson(wrapper.toString(), Object.class);
        model.addAttribute("departureInformation", departureInformation);

        return "dashboardTemplates/timetable";
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String showDashboard(@RequestParam(value = "template", required = false) String template,
                                @RequestParam(value = "code[]", required = false) String[] codes,
                                @RequestParam(value = "crs[]", required = false) String[] crs,
                                @RequestParam(value = "flipTo", required = false) String flipTo,
                                @RequestParam(value = "name", required = false) String name,
                                UriComponentsBuilder builder,
                                Model model
    )
    {
        // Check if no transport codes were provided.
        if (codes == null && crs == null)
        {
            throw new RuntimeException("No transport codes provided. Provide codes as a parameter with code[]={code} or crs[]={crs}");
        }

        JSONObject departureInfoRequest = new JSONObject();
        if (codes != null)
        {
            departureInfoRequest.put("codes", Arrays.asList(codes));
        }

        if (crs != null)
        {
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
                    if (NaPTANCache.checkStopExists(stopCode))
                    {
                        JSONObject objForStop = newCodesArray.getJSONObject(stopCode);
                        Naptan naptan = NaPTANCache.getNaptan(stopCode);

                        objForStop.put("StopName", naptan.getLongDescription());
                        objForStop.put("Identifier", naptan.getIdentifier());
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
                    if (TrainStationCache.checkStopExists(stationCode))
                    {
                        JSONObject objForStop = newCodesArray.getJSONObject(stationCode);
                        Station station = TrainStationCache.getStation(stationCode);

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
        if (codes != null)
        {
            modelBusCodes = new ArrayList<>(Arrays.asList(codes));
            modelBusCodes.removeAll(removedBusCodes);
        }
        model.addAttribute("busCodes", modelBusCodes);

        List<String> modelTrainCodes = new ArrayList<>();
        if (crs != null)
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

        // Check if a template was provided. Provide default if not.
        if (template == null || template.equals(""))
            template = "default";

        // Add the switch URL if applicable.
        if (flipTo != null && !flipTo.equals(""))
        {
            model.addAttribute("flipUrl", builder.path("/dashboard")
                    .queryParam("template", flipTo)
                    .queryParam("flipTo", template)
                    .queryParam("code[]", codes)
                    .queryParam("crs[]", crs)
                    .build()
                    .toUriString());
        }

        if(name != null)
            model.addAttribute("name", name);

        return "dashboardTemplates/" + template;
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

            busesAndTrains.put("busStops", SIRICache.getSiriJson(busCodes.stream().toArray(String[]::new)).get("busStops"));
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

            busesAndTrains.put("trainStations", TrainDepartureCache.getTrainJSON(trainCodes.stream()
                    .toArray(String[]::new)).get("trainStations"));
        }

        // Wrap all JSON into one object
        k.put("payload", busesAndTrains);

        return k;
    }
}
