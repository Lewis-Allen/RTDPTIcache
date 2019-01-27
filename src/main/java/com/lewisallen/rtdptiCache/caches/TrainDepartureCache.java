package com.lewisallen.rtdptiCache.caches;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TrainDepartureCache {
    public static Map<Object, JSONObject> trainDepartureCache = new HashMap<>();

    public static String getTrainJSON(String[] stationCodes) throws JSONException {
        JSONObject k = new JSONObject();

        JSONObject j = new JSONObject();
        for(String stationCode : stationCodes){
            j.put(stationCode, trainDepartureCache.get(stationCode));
        }

        k.put("payload", j);

        return k.toString();
    }
}
