package com.lewisallen.rtdptiCache.caches;

import com.lewisallen.rtdptiCache.Station;
import com.lewisallen.rtdptiCache.db.TransportDatabase;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TrainStationCache {

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

    public static void populateCache(TransportDatabase db){
        try {
            Map<String, Station> stations = new HashMap<>();
            ResultSet rs = db.queryStation();
            while(rs.next()){
                String code = rs.getString("CRSCode");
                Station station = new Station(rs.getString("StationName"), code);

                stations.put(code, station);
            }

            TrainStationCache.stationCache = stations;
        } catch (Exception e) {
            System.out.println("Error whilst retrieving train data from db: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
