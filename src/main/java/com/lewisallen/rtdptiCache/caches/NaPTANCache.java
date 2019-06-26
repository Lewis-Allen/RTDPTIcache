package com.lewisallen.rtdptiCache.caches;

import com.lewisallen.rtdptiCache.db.TransportDatabase;
import com.lewisallen.rtdptiCache.logging.ErrorHandler;
import com.lewisallen.rtdptiCache.models.Naptan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class NaPTANCache
{
    // Queries only active stops in the NaPTAN cache.
    public static String naptanQuery = "SELECT SystemCodeNumber, LongDescription, Identifier FROM naptan WHERE Active = 'True' AND Retrieve = 1;";

    public static Map<String, Naptan> naptanCache = new ConcurrentHashMap<>();

    private static Connection conn;

    /**
     * Returns stop names for the given list of codes.
     *
     * @param codes Codes for stop names.
     * @return Map of code to stop name
     */
    public static Map<String, String> getStopNames(String[] codes)
    {
        Map<String, String> res = new HashMap<>();
        for (String s : codes)
        {
            res.put(s, NaPTANCache.naptanCache.get(s).getLongDescription());
        }

        return res;
    }

    /**
     * Populates the NaPTAN cache with values based off active stops in DB.
     *
     * @param db    database connector.
     * @param query query to populate the cache.
     */
    public static void populateCache(TransportDatabase db, String query)
    {
        try
        {
            Map<String, Naptan> naptans = new HashMap<>();

            conn = db.getDbConnection();
            ResultSet rs = db.query(query, conn);
            while (rs.next())
            {
                String code = rs.getString("SystemCodeNumber");
                Naptan naptan = new Naptan(code,
                        rs.getString("LongDescription"),
                        rs.getString("Identifier"));

                naptans.put(code, naptan);
            }

            NaPTANCache.naptanCache = naptans;
        }
        catch (Exception e)
        {
            String message = "Error populating NaPTAN cache";
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
     * Checks if a stop exists in the cache.
     *
     * @param naptan NaPTAN code of stop to check.
     * @return Whether stop exists or not.
     */
    public static boolean checkStopExists(String naptan)
    {
        return NaPTANCache.naptanCache.containsKey(naptan);
    }

    /**
     * Get the NaPTAN object for supplied code
     *
     * @param naptan the NaPTAN code to get
     * @return NaPTAN object for given code.
     */
    public static Naptan getNaptan(String naptan)
    {
        return NaPTANCache.naptanCache.getOrDefault(naptan, null);
    }

    /**
     * Get the set of codes cached in the NaPTAN cache.
     *
     * @return Set of codes cached.
     */
    public static Set<String> getCachedCodes()
    {
        return NaPTANCache.naptanCache.keySet();
    }

}
