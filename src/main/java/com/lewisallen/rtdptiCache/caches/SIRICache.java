package com.lewisallen.rtdptiCache.caches;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class SIRICache {

	public static Map<Object, JSONObject> siriCache = new HashMap<Object, JSONObject>();
	
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
