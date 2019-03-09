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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

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

        // ToDo: Get list of templates from /resources/templates/dashboardTemplates
        Path path = Paths.get( "templates", "dashboardTemplates");
        List<String> availableTemplates = filesInDashboardTemplateDirectory(path);
        model.addAttribute("availableTemplates", availableTemplates);

        return "create";
    }

    /**
     * Retrieves list of files in dashboard template directory.
     * @param path Path to dashboard template directory.
     * @return List of files.
     */
    public List<String> filesInDashboardTemplateDirectory(Path path)
    {
        List<String> fileNames = new ArrayList<>();

        // Attempt to list all the files in given directory.
        try
        {
            ClassLoader classLoader = this.getClass().getClassLoader();
            Path dashboardTemplateDirectory = Paths.get(classLoader.getResource(path.toString()).toURI());

            // Gets a list of dashboard templates with no file extension.
            fileNames = Files.walk(dashboardTemplateDirectory)
                    .filter(p -> p.toString().endsWith(".html"))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(this::removeExtension)
                    .sorted()
                    .collect(Collectors.toList());
        }
        catch (Exception e)
        {
            ErrorHandler.handle(e, Level.WARNING, "Error parsing dashboard template directory.");
        }

        return fileNames;
    }

    /**
     * Removes a file extension from a file.
     * @param filename Name of file.
     * @return Filename without extension.
     */
    private String removeExtension(String filename)
    {
        int pos = filename.lastIndexOf(".");
        return pos > 0 ? filename.substring(0, pos) : filename;
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String showDashboard(@RequestParam(value = "template", required = false) String template,
                                @RequestParam(value = "code[]", required = false) String[] codes,
                                @RequestParam(value = "crs[]", required = false) String[] crs,
                                @RequestParam(value = "flipTo", required = false) String flipTo,
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
