package com.lewisallen.rtdptiCache.caches;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class SIRICache {

	public static Map<Object, JSONObject> siriCache = new HashMap<Object, JSONObject>();
	
	public static String getSiriJson(String[] naptans){
		
		JSONObject k = new JSONObject();
		
		try {	
			JSONObject j = new JSONObject();
			for(String s : naptans){
				j.put(s, SIRICache.siriCache.get(s));
			}
			
			k.put("payload", j);
			
		} catch (JSONException e){
			e.printStackTrace();
		}
		
		return k.toString();
	}
}
