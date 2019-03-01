package com.lewisallen.rtdptiCache.caches;

import com.lewisallen.rtdptiCache.logging.ErrorHandler;
import com.lewisallen.rtdptiCache.models.Station;
import com.lewisallen.rtdptiCache.db.TransportDatabase;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class TrainStationCache {

    public static String stationQuery = "SELECT StationName, CRSCode FROM stations WHERE Retrieve = 1;";

    public static Map<String, Station> stationCache = new HashMap<>();

    public static Set<String> getCachedCodes(){
        return TrainStationCache.stationCache.keySet();
    }

    public static Map<String, String> getStationNames(String[] crsCodes){
        Map<String, String> res = new HashMap<>();
        for(String s : crsCodes){
            res.put(s, TrainStationCache.stationCache.get(s).getStationName());
        }

        return res;
    }

    public static void populateCache(TransportDatabase db, String query){
        try {
            Map<String, Station> stations = new HashMap<>();
            ResultSet rs = db.query(query);
            while(rs.next()){
                String code = rs.getString("CRSCode");
                Station station = new Station(rs.getString("StationName"), code);

                stations.put(code, station);
            }

            TrainStationCache.stationCache = stations;
        } catch (Exception e) {
            String message = "Error in populate cache whilst retrieving train data from db:";
            ErrorHandler.handle(e, Level.SEVERE, message);
        }
    }

    public static boolean checkStopExists(String stationCode) { return TrainStationCache.stationCache.containsKey(stationCode); }

    public static Station getStation(String stationCode)
    {
        return TrainStationCache.stationCache.containsKey(stationCode) ? TrainStationCache.stationCache.get(stationCode) : null;
    }
}
