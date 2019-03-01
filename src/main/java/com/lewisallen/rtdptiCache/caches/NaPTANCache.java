package com.lewisallen.rtdptiCache.caches;

import com.lewisallen.rtdptiCache.logging.ErrorHandler;
import com.lewisallen.rtdptiCache.models.Naptan;
import com.lewisallen.rtdptiCache.db.TransportDatabase;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class NaPTANCache {

	public static String naptanQuery = "SELECT SystemCodeNumber, LongDescription, Identifier FROM naptan WHERE Active = 'True' AND Retrieve = 1;";

	public static Map<String, Naptan> naptanCache = new HashMap<>();
	
	public static Map<String, String> getStopNames(String[] codes){
		Map<String, String> res = new HashMap<>();
		for(String s : codes){
			res.put(s, NaPTANCache.naptanCache.get(s).getLongDescription());
		}
		
		return res;
	}

	/**
	 * Populates the NaPTAN cache with values based off active stops in DB.
	 * @param db
	 */
	public static void populateCache(TransportDatabase db, String query) {
		try {
			Map<String, Naptan> naptans = new HashMap<>();
			ResultSet rs = db.query(query);
			while (rs.next()) {
				String code = rs.getString("SystemCodeNumber");
				Naptan naptan = new Naptan(code,
						rs.getString("LongDescription"),
						rs.getString("Identifier"));

				naptans.put(code, naptan);
			}

			NaPTANCache.naptanCache = naptans;
		} catch (Exception e) {
			String message = "Error populating NaPTAN cache";
			ErrorHandler.handle(e, Level.SEVERE, message);
		}
	}

	public static boolean checkStopExists(String naptan) { return NaPTANCache.naptanCache.containsKey(naptan); }

	public static Naptan getNaptan(String naptan)
	{
		return NaPTANCache.naptanCache.containsKey(naptan) ? NaPTANCache.naptanCache.get(naptan) : null;
	}
	
	public static Set<String> getCachedCodes(){
	    return NaPTANCache.naptanCache.keySet();
	}

}
