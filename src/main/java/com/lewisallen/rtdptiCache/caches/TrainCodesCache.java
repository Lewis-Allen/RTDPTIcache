package com.lewisallen.rtdptiCache.caches;

import com.lewisallen.rtdptiCache.models.Station;
import com.lewisallen.rtdptiCache.repositories.TrainRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TrainCodesCache {
    public static Map<String, Station> stationCache = new ConcurrentHashMap<>();
    private static TrainRepository repository;

    public TrainCodesCache(TrainRepository repository) {
        TrainCodesCache.repository = repository;
    }

    /**
     * Get the set of codes cached in the Station cache.
     *
     * @return Set of codes cached.
     */
    public static Set<String> getCachedCodes() {
        return TrainCodesCache.stationCache.keySet();
    }

    /**
     * Returns station names for the given list of codes.
     *
     * @param crsCodes Codes for station names.
     * @return Map containing codes to station name
     */
    public static Map<String, String> getStationNames(String[] crsCodes) {
        Map<String, String> res = new HashMap<>();
        for (String s : crsCodes) {
            res.put(s, TrainCodesCache.stationCache.get(s).getStationName());
        }

        return res;
    }

    /**
     * Populates the Station Codes cache with values based off active stops in DB.
     */
    public static void populateCache() {
        List<Station> retrievedStationCodes = repository.findByRetrieve(1);

        Map<String, Station> stationCodes = new ConcurrentHashMap<>();
        for (Station station : retrievedStationCodes) {
            stationCodes.put(station.getCrsCode(), station);
        }

        TrainCodesCache.stationCache = stationCodes;
    }

    /**
     * Checks if a given code exists in the station cache.
     *
     * @param stationCode Code to check
     * @return If code exists.
     */
    public static boolean checkStopExists(String stationCode) {
        return TrainCodesCache.stationCache.containsKey(stationCode);
    }

    /**
     * Gets a Station object for a given code.
     *
     * @param stationCode Code to get object for.
     * @return Station object for code.
     */
    public static Station getStation(String stationCode) {
        return TrainCodesCache.stationCache.getOrDefault(stationCode, null);
    }
}
