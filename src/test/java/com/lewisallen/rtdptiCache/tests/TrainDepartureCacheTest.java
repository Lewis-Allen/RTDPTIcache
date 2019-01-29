package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.caches.TrainDepartureCache;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class TrainDepartureCacheTest {

    @Before
    public void InitialiseTrainDepartureCache(){
        new TrainDepartureCache();
    }

    @Test
    public void testTrainCache() {
        for(int i = 0; i < 10; i++){
            TrainDepartureCache.trainDepartureCache.put(Integer.toString(i), new JSONObject());
        }

        // Generate list of keys to pass to function.
        String[] stationCodes = new String[10];
		for(int i = 0; i < 10; i++){
            stationCodes[i] = Integer.toString(i);
        }

        // Get response from cache.
        JSONObject res = TrainDepartureCache.getTrainJSON(stationCodes);

        // Test response.
		for(int i = 0; i < 10; i++){
            assert(res.getJSONObject("trainStations").has(Integer.toString(i)));
        }
}
}