package com.lewisallen.rtdptiCache.caches;

import com.lewisallen.rtdptiCache.models.Bus;
import com.lewisallen.rtdptiCache.repositories.BusRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// TODO: remove duplication here + TrainCodesCache

@Component
public class BusCodesCache {
    public static Map<String, Bus> busCodeCache = new ConcurrentHashMap<>();
    private static BusRepository repository;

    public BusCodesCache(BusRepository repository) {
        BusCodesCache.repository = repository;
    }

    /**
     * Returns stop names for the given list of codes.
     *
     * @param codes Codes for stop names.
     * @return Map of code to stop name
     */
    public static Map<String, String> getStopNames(String[] codes) {
        Map<String, String> res = new HashMap<>();
        for (String s : codes) {
            res.put(s, BusCodesCache.busCodeCache.get(s).getLongDescription());
        }

        return res;
    }

    /**
     * Populates the Bus Codes cache with values based off active stops in DB.
     */
    public static void populateCache() {
        List<Bus> retrieveBuses = repository.findByRetrieve(1);

        Map<String, Bus> busCodes = new ConcurrentHashMap<>();

        for (Bus bus : retrieveBuses) {
            busCodes.put(bus.getSystemCodeNumber(), bus);
        }

        BusCodesCache.busCodeCache = busCodes;
    }

    /**
     * Checks if a stop exists in the cache.
     *
     * @param busCode Bus code of stop to check.
     * @return Whether stop exists or not.
     */
    public static boolean checkStopExists(String busCode) {
        return BusCodesCache.busCodeCache.containsKey(busCode);
    }

    /**
     * Get the Bus object for supplied code
     *
     * @param busCode the Bus code to get
     * @return Bus object for given code.
     */
    public static Bus getBus(String busCode) {
        return BusCodesCache.busCodeCache.getOrDefault(busCode, null);
    }

    /**
     * Get the set of codes cached in the Bus codes cache.
     *
     * @return Set of codes cached.
     */
    public static Set<String> getCachedCodes() {
        return BusCodesCache.busCodeCache.keySet();
    }

}
