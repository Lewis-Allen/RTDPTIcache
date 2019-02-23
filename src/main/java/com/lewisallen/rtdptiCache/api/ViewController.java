package com.lewisallen.rtdptiCache.api;

import com.google.gson.Gson;
import com.lewisallen.rtdptiCache.Naptan;
import com.lewisallen.rtdptiCache.caches.NaPTANCache;
import com.lewisallen.rtdptiCache.caches.SIRICache;
import com.lewisallen.rtdptiCache.caches.TrainDepartureCache;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ViewController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showTitle() {
        return "title";
    }

    @RequestMapping(value = "/dashboard", method=RequestMethod.GET)
    public String showDashboard(@RequestParam(value="template", required = false) String template,
                                @RequestParam(value="code[]", required = false) String[] codes,
                                @RequestParam(value="crs[]", required = false) String[] crs,
                                Model model
    ){
        JSONObject departureInformation;
        // Check if no transport codes were provided.
        if(codes == null && crs == null){
            throw new RuntimeException("No transport codes provided. Provide codes as a parameter with code[]={code} or crs[]={crs}");
        }
        else
        {
            // Else we add codes to Model.
            JSONObject j = new JSONObject();
            if(codes != null){
                j.put("codes", Arrays.asList(codes));
            }

            if(crs != null)
                j.put("crs", Arrays.asList(crs));

            departureInformation = getDepartureInformation(j);

            // Fill in stop name for any locations that have no visits.
            if(departureInformation.getJSONObject("payload").has("busStops")) {
                JSONObject codesArray = departureInformation.getJSONObject("payload").getJSONObject("busStops");
                {
                    for (String aString : codesArray.keySet())
                    {
                        if (departureInformation.getJSONObject("payload").getJSONObject("busStops").getJSONObject(aString).isEmpty()) {
                            if (NaPTANCache.checkStopExists(aString))
                            {
                                Naptan naptan = NaPTANCache.getNaptan(aString);
                                departureInformation.getJSONObject("payload").getJSONObject("busStops").getJSONObject(aString).put("StopName", naptan.getLongDescription());
                                departureInformation.getJSONObject("payload").getJSONObject("busStops").getJSONObject(aString).put("Identifier", naptan.getIdentifier());
                                departureInformation.getJSONObject("payload").getJSONObject("busStops").getJSONObject(aString).put("MonitoredStopVisits", new ArrayList<>());
                            }
                            else
                            {
                                departureInformation.getJSONObject("payload").getJSONObject("busStops").remove(aString);
                            }
                        }
                    }
                }
            }

            // Thymeleaf apparently doesn't like parsing JSON...
            // This converts the json back into a 'java object' which Thymeleaf doesn't complain about.
            Gson gson = new Gson();
            Object departureInformation2 = gson.fromJson(departureInformation.toString(), Object.class);
            model.addAttribute("departureInformation", departureInformation2);
        }

        // Check if a template was provided. Provide default if not.
        if(template == null)
            template = "default";

        return template;
    }

    private JSONObject getDepartureInformation(JSONObject request)
    {
        // Create a JSON Object to hold the response.
        JSONObject k = new JSONObject();

        // Create a list to hold bus stops and train stations.
        JSONObject busesAndTrains = new JSONObject();

        // Add any bus stops to the JSON
        if(request.has("codes")){
            if(request.get("codes") instanceof JSONArray) {
                JSONArray busCodeList = request.getJSONArray("codes");

                List<String> busCodes = new ArrayList<>();
                for (int i = 0; i < busCodeList.length(); i++) {
                    busCodes.add(busCodeList.get(i).toString());
                }

                busesAndTrains.put("busStops", SIRICache.getSiriJson(busCodes.stream().toArray(String[]::new)).get("busStops"));
            }
            else
            {
                String[] singleStop = new String[]{request.get("codes").toString()};
                busesAndTrains.put("busStops", SIRICache.getSiriJson(singleStop).get("busStops"));
            }
        }

        // Add any train stations to the JSON
        if(request.has("crs")){
            // Grab as object if only one CRS requested, else array
            if(request.get("crs") instanceof JSONArray){
                JSONArray trainCodeList = request.getJSONArray("crs");

                List<String> trainCodes = new ArrayList<>();
                for(int i = 0; i < trainCodeList.length(); i++){
                    trainCodes.add(trainCodeList.get(i).toString());
                }

                busesAndTrains.put("trainStations", TrainDepartureCache.getTrainJSON(trainCodes.stream().toArray(String[]::new)).get("trainStations"));
            }
            else
            {
                String[] singleCRS = new String[]{request.get("crs").toString()};
                busesAndTrains.put("trainStations", TrainDepartureCache.getTrainJSON(singleCRS).get("trainStations"));
            }
        }

        // Wrap all JSON into one object
        k.put("payload", busesAndTrains);

        return k;
    }
}
