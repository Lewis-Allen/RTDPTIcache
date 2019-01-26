package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.caches.TrainCache;
import org.json.JSONObject;
import org.junit.Test;

public class TrainCacheTest {

    @Test
    public void testTrainCache() {
        for(int i = 0; i < 10; i++){
            TrainCache.trainCache.put(Integer.toString(i), new JSONObject());
        }

        // Generate list of keys to pass to function.
        String[] stationCodes = new String[10];
		for(int i = 0; i < 10; i++){
            stationCodes[i] = Integer.toString(i);
        }

        // Get response from cache.
        String res = TrainCache.getTrainJSON(stationCodes);

		// Put into JSON Object
		JSONObject j = new JSONObject(res);

        // Test response.
		for(int i = 0; i < 10; i++){
            assert(j.getJSONObject("payload").has(Integer.toString(i)));
        }
}
}
