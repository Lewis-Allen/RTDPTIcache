package com.lewisallen.rtdptiCache.caches;

import org.json.JSONObject;

import java.util.Map;

public final class Caches {

    private static final DataCache BUS_DATA_CACHE = new DataCache();
    private static final DataCache TRAIN_DATA_CACHE = new DataCache();

    public static void resetBusData(Map<Object, JSONObject> cache) {
        BUS_DATA_CACHE.reset(cache);
    }

    public static JSONObject getSiriJSON(String[] naptans) {
        return BUS_DATA_CACHE.getJSON(naptans, "busStops");
    }

    public static DataCache getBusDataCache() {
        return BUS_DATA_CACHE;
    }

    public static void resetTrainData(Map<Object, JSONObject> cache) {
        TRAIN_DATA_CACHE.reset(cache);
    }

    public static JSONObject getTrainJSON(String[] stationCodes) {
        return TRAIN_DATA_CACHE.getJSON(stationCodes, "trainStations");
    }

    public static DataCache getTrainDataCache() {
        return TRAIN_DATA_CACHE;
    }
}
