package com.lewisallen.rtdptiCache.caches;

import java.util.HashMap;
import java.util.Map;

public class NaPTANCache {

	public static Map<String, String> naptanCache = new HashMap<String, String>();
	
	public static Map<String, String> getStopNames(String[] codes){
		Map<String, String> res = new HashMap<String, String>();
		for(String s : codes){
			res.put(s, NaPTANCache.naptanCache.get(s));
		}
		
		return res;
	}

}
