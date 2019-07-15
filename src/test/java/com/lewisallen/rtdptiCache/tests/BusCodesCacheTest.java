package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.caches.BusCodesCache;
import com.lewisallen.rtdptiCache.models.Bus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BusCodesCacheTest
{
    @Test
    void testNaPTANStopNames()
    {
        BusCodesCache.busCodeCache = new ConcurrentHashMap<>();
        // Populate data.
        for (int i = 0; i < 10; i++)
        {
            BusCodesCache.busCodeCache.put(Integer.toString(i), new Bus(Integer.toString(i), "Example Location" + i, "adj"));
        }

        // Generate list of keys to pass to function.
        String[] codesList = new String[10];
        for (int i = 0; i < 10; i++)
        {
            codesList[i] = Integer.toString(i);
        }

        // Get response from cache.
        Map<String, String> res = BusCodesCache.getStopNames(codesList);

        // Test response.
        for (int i = 0; i < 10; i++)
        {
            Assertions.assertEquals(res.containsKey(Integer.toString(i)), true);
        }
    }

    @Test
    void testCachedCodes()
    {
        BusCodesCache.busCodeCache = new HashMap<>();

        for (int i = 0; i < 10; i++)
        {
            BusCodesCache.busCodeCache.put(Integer.toString(i), new Bus(Integer.toString(i), "Example Location" + i, "adj"));
        }

        Set<String> keys = BusCodesCache.getCachedCodes();

        Assertions.assertEquals(10, keys.size());
    }

    @Test
    void testCachePopulate()
    {
        BusCodesCache.populateCache();
    }

    @Test
    void testDoesStopExist()
    {
        Assertions.assertFalse(BusCodesCache.checkStopExists("shouldn't exist"));

        BusCodesCache.busCodeCache.put("shouldn't exist", new Bus("shouldn't exist'", "Test", "adj"));

        Assertions.assertTrue(BusCodesCache.checkStopExists("shouldn't exist"));
    }

    @Test
    void testGetBus()
    {
        Bus bus = new Bus("bus", "Test Bus", "adj");
        Assertions.assertEquals(BusCodesCache.getBus("bus"), null);

        BusCodesCache.busCodeCache.put("bus", bus);

        Assertions.assertEquals(BusCodesCache.getBus("bus"), bus);
    }
}
