package com.lewisallen.rtdptiCache.caches;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BusDataCache
{

    public static Map<Object, JSONObject> siriCache = new ConcurrentHashMap<>();

    /**
     * Given a string of NaPTAN codes, return the stored JSON for each code.
     *
     * @param naptans List of codes to query.
     * @return JSON object containing all codes and their respective JSON.
     * @throws JSONException
     */
    public static JSONObject getSiriJson(String[] naptans) throws JSONException
    {
        JSONObject k = new JSONObject();

        JSONObject j = new JSONObject();
        for (String s : naptans)
        {
            if (BusDataCache.siriCache.containsKey(s))
            {
                j.put(s, BusDataCache.siriCache.get(s));
            }
            else
            {
                j.put(s, new JSONObject());
            }
        }

        k.put("busStops", j);

        return k;
    }
}
