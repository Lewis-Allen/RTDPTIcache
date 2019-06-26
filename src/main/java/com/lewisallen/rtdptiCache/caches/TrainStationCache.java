package com.lewisallen.rtdptiCache.caches;

import com.lewisallen.rtdptiCache.db.TransportDatabase;
import com.lewisallen.rtdptiCache.logging.ErrorHandler;
import com.lewisallen.rtdptiCache.models.Station;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class TrainStationCache
{
    // Queries only active stations in the Station cache.
    public static String stationQuery = "SELECT StationName, CRSCode FROM stations WHERE Retrieve = 1;";

    public static Map<String, Station> stationCache = new ConcurrentHashMap<>();

    private static Connection conn;

    /**
     * Get the set of codes cached in the Station cache.
     *
     * @return Set of codes cached.
     */
    public static Set<String> getCachedCodes()
    {
        return TrainStationCache.stationCache.keySet();
    }

    /**
     * Returns station names for the given list of codes.
     *
     * @param crsCodes Codes for station names.
     * @return Map containing codes to station name
     */
    public static Map<String, String> getStationNames(String[] crsCodes)
    {
        Map<String, String> res = new HashMap<>();
        for (String s : crsCodes)
        {
            res.put(s, TrainStationCache.stationCache.get(s).getStationName());
        }

        return res;
    }

    /**
     * Populates the Station cache with values based off active stops in DB.
     *
     * @param db    Database connector
     * @param query Query to populate cache.
     */
    public static void populateCache(TransportDatabase db, String query)
    {
        try
        {
            Map<String, Station> stations = new HashMap<>();

            conn = db.getDbConnection();
            ResultSet rs = db.query(query, conn);
            while (rs.next())
            {
                String code = rs.getString("CRSCode");
                Station station = new Station(rs.getString("StationName"), code);

                stations.put(code, station);
            }

            TrainStationCache.stationCache = stations;
        }
        catch (Exception e)
        {
            String message = "Error in populate cache whilst retrieving train data from db:";
            ErrorHandler.handle(e, Level.SEVERE, message);
        }
        finally
        {
            try
            {
                if(conn != null)
                    conn.close();
            }
            catch (SQLException e)
            {
                String message = "Error closing connection to database.";
                ErrorHandler.handle(e, Level.SEVERE, message);
            }
        }
    }

    /**
     * Checks if a given code exists in the station cache.
     *
     * @param stationCode Code to check
     * @return If code exists.
     */
    public static boolean checkStopExists(String stationCode)
    {
        return TrainStationCache.stationCache.containsKey(stationCode);
    }

    /**
     * Gets a Station object for a given code.
     *
     * @param stationCode Code to get object for.
     * @return Station object for code.
     */
    public static Station getStation(String stationCode)
    {
        return TrainStationCache.stationCache.getOrDefault(stationCode, null);
    }
}
