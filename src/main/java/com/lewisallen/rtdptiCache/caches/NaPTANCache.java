package com.lewisallen.rtdptiCache.caches;

import com.lewisallen.rtdptiCache.Naptan;
import com.lewisallen.rtdptiCache.db.TransportDatabase;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NaPTANCache {

	public static Map<String, Naptan> naptanCache = new HashMap<>();
	
	public static Map<String, String> getStopNames(String[] codes){
		Map<String, String> res = new HashMap<String, String>();
		for(String s : codes){
			res.put(s, NaPTANCache.naptanCache.get(s).getLongDescription());
		}
		
		return res;
	}
	
	public static void populateCache(TransportDatabase db) {
		try {
			Map<String, Naptan> naptans = new HashMap<String, Naptan>();
			ResultSet rs = db.queryNaptan();
			while (rs.next()) {
				String code = rs.getString("SystemCodeNumber");
				Naptan naptan = new Naptan(code,
						rs.getString("LongDescription"),
						rs.getString("Identifier"));

				naptans.put(code, naptan);
			}

			NaPTANCache.naptanCache = naptans;
		} catch (Exception e) {
			System.out.println("Error populating naptan cache: " + e.getMessage());
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
