package com.lewisallen.rtdptiCache.caches;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataCache
{
    private Map<Object, JSONObject> cache = new ConcurrentHashMap<>();

    public void reset(Map<Object, JSONObject> cache) {
        this.cache.clear();
        this.cache.putAll(cache);
    }

    /**
     *
     * @param codes
     * @return
     * @throws JSONException
     */
    public JSONObject getJSON(String[] codes, String identifier) throws JSONException
    {
        JSONObject result = new JSONObject();
        JSONObject entry = new JSONObject();

        for (String s : codes)
        {
            entry.put(s, cache.containsKey(s) ? cache.get(s) : new JSONObject());
        }

        result.put(identifier, entry);

        return result;
    }

    public Map<Object, JSONObject> getCache()
    {
        return cache;
    }
}
