package com.lewisallen.rtdptiCache.api;

import com.lewisallen.rtdptiCache.caches.SIRICache;
import com.lewisallen.rtdptiCache.caches.TrainDepartureCache;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StopController
{

    @RequestMapping(value = "api/stop", method = RequestMethod.POST)
    public ResponseEntity<String> stops(@RequestBody JSONObject json)
    {
        // Create a JSON Object to hold the response.
        JSONObject k = new JSONObject();

        // Create a list to hold bus stops and train stations.
        JSONObject busesAndTrains = new JSONObject();

        // Add any bus stops to the JSON
        if (json.has("codes"))
        {
            if (json.get("codes") instanceof JSONArray)
            {
                JSONArray busCodeList = json.getJSONArray("codes");

                List<String> busCodes = new ArrayList<>();
                for (int i = 0; i < busCodeList.length(); i++)
                {
                    busCodes.add(busCodeList.get(i).toString());
                }

                busesAndTrains.put("busStops", SIRICache.getSiriJson(busCodes.stream().toArray(String[]::new)).get("busStops"));
            }
            else
            {
                String[] singleStop = new String[]{json.get("codes").toString()};
                busesAndTrains.put("busStops", SIRICache.getSiriJson(singleStop).get("busStops"));
            }
        }

        // Add any train stations to the JSON
        if (json.has("CRS"))
        {
            // Grab as object if only one CRS requested, else array
            if (json.get("CRS") instanceof JSONArray)
            {
                JSONArray trainCodeList = json.getJSONArray("CRS");

                List<String> trainCodes = new ArrayList<>();
                for (int i = 0; i < trainCodeList.length(); i++)
                {
                    trainCodes.add(trainCodeList.get(i).toString());
                }

                busesAndTrains.put("trainStations", TrainDepartureCache.getTrainJSON(trainCodes.stream().toArray(String[]::new)).get("trainStations"));
            }
            else
            {
                String[] singleCRS = new String[]{json.get("CRS").toString()};
                busesAndTrains.put("trainStations", TrainDepartureCache.getTrainJSON(singleCRS).get("trainStations"));
            }
        }

        // Wrap all JSON into one object
        k.put("payload", busesAndTrains);

        return new ResponseEntity(k.toString(), HttpStatus.OK);
    }
}
