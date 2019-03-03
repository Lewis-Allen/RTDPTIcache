package com.lewisallen.rtdptiCache.caches;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TrainDepartureCache
{
    public static Map<Object, JSONObject> trainDepartureCache = new ConcurrentHashMap<>();

    /**
     * Given a string of station codes, return the stored JSON for each code.
     *
     * @param stationCodes List of station codes to query.
     * @return JSON object containing all codes and their respective JSON.
     * @throws JSONException
     */
    public static JSONObject getTrainJSON(String[] stationCodes) throws JSONException
    {
        JSONObject k = new JSONObject();

        JSONObject j = new JSONObject();
        for (String stationCode : stationCodes)
        {
            if (trainDepartureCache.containsKey(stationCode))
            {
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
