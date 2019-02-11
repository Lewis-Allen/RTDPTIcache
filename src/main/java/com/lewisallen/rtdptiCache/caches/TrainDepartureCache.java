package com.lewisallen.rtdptiCache.caches;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TrainDepartureCache {
    public static Map<Object, JSONObject> trainDepartureCache = new HashMap<>();

    public static JSONObject getTrainJSON(String[] stationCodes) throws JSONException {
        JSONObject k = new JSONObject();

        JSONObject j = new JSONObject();
        for(String stationCode : stationCodes){
            if(trainDepartureCache.containsKey(stationCode)){
                j.put(stationCode, trainDepartureCache.get(stationCode));
            }
            else
            {
                j.put(stationCode, new JSONObject());
            }
        }

        k.put("trainStations", j);

        return k;
    }
}
