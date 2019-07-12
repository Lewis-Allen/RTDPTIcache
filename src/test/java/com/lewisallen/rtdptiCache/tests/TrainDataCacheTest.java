package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.caches.Caches;
import com.lewisallen.rtdptiCache.caches.TrainDataCache;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrainDataCacheTest
{

    @BeforeAll
    void InitialiseTrainDepartureCache()
    {
        new TrainDataCache();
    }

    @Test
    void testTrainCache()
    {
        for (int i = 0; i < 10; i++)
        {
            TrainDataCache.trainDepartureCache.put(Integer.toString(i), new JSONObject());
        }

        // Generate list of keys to pass to function.
        String[] stationCodes = new String[10];
        for (int i = 0; i < 10; i++)
        {
            stationCodes[i] = Integer.toString(i);
        }

        // Get response from cache.
        JSONObject res = Caches.getTrainJSON(stationCodes);

        // Test response.
        for (int i = 0; i < 10; i++)
        {
            Assertions.assertTrue(res.getJSONObject("trainStations").has(Integer.toString(i)));
        }
    }
}
