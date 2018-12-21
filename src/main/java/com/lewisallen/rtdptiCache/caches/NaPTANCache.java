package com.lewisallen.rtdptiCache.caches;

import com.lewisallen.rtdptiCache.Naptan;
import com.lewisallen.rtdptiCache.db.NaptanDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NaPTANCache {

	public static Map<String, Naptan> naptanCache = new HashMap<String, Naptan>();
	
	public static Map<String, String> getStopNames(String[] codes){
		Map<String, String> res = new HashMap<String, String>();
		for(String s : codes){
			res.put(s, NaPTANCache.naptanCache.get(s).getLongDescription());
		}
		
		return res;
	}
	
	public static void populateCache(NaptanDatabase db){
		try {
			Map<String, Naptan> naptans = new HashMap<String, Naptan>();
			ResultSet rs = db.queryNaptan();
			while(rs.next()){
				String code = rs.getString("SystemCodeNumber");
				Naptan naptan = new Naptan(code,
										   rs.getString("LongDescription"),
										   rs.getString("Identifier"));
				
				naptans.put(code, naptan);
			}
			
			NaPTANCache.naptanCache = naptans;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Could not connect to db." + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static Set<String> getCachedCodes(){
	    return NaPTANCache.naptanCache.keySet();
	}

}
