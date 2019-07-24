package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.caches.TrainCodesCache;
import com.lewisallen.rtdptiCache.models.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrainCodesCacheTest {
    @Test
    void testStationNames() {
        // Populate data.
        for (int i = 0; i < 10; i++) {
            TrainCodesCache.stationCache.put(Integer.toString(i), new Station("Example Location" + i, Integer.toString(i)));
        }

        // Generate list of keys to pass to function.
        String[] codesList = new String[10];
        for (int i = 0; i < 10; i++) {
            codesList[i] = Integer.toString(i);
        }

        // Get response from cache.
        Map<String, String> res = TrainCodesCache.getStationNames(codesList);

        // Test response.
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(res.containsKey(Integer.toString(i)), true);
        }
    }

    @Test
    void testCachedCodes() {
        TrainCodesCache.stationCache = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            TrainCodesCache.stationCache.put(Integer.toString(i), new Station("Example Location" + i, Integer.toString(i)));
        }

        Set<String> keys = TrainCodesCache.getCachedCodes();

        Assertions.assertEquals(10, keys.size());
    }

    @Test
    void testCachePopulate() {
        TrainCodesCache.populateCache();
    }

    @Test
    void testDoesStopExist() {
        Assertions.assertFalse(TrainCodesCache.checkStopExists("shouldn't exist"));

        TrainCodesCache.stationCache.put("shouldn't exist", new Station("shouldn't exist'", "Test"));

        Assertions.assertTrue(TrainCodesCache.checkStopExists("shouldn't exist"));
    }

    @Test
    void testGetStation() {
        Station station = new Station("TestStation", "TST");
        Assertions.assertEquals(TrainCodesCache.getStation("TST"), null);

        TrainCodesCache.stationCache.put("TST", station);

        Assertions.assertEquals(TrainCodesCache.getStation("TST"), station);
    }
}
