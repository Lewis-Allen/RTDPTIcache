package com.lewisallen.rtdptiCache.caches;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SIRICache {

	public static Map<Object, JSONObject> siriCache = new HashMap<>();
	
	public static String getSiriJson(String[] naptans) throws JSONException{
		
		JSONObject k = new JSONObject();
		
		JSONObject j = new JSONObject();
		for(String s : naptans){
			j.put(s, SIRICache.siriCache.get(s));
		}
			
		k.put("payload", j);
		
		return k.toString();
	}
}
