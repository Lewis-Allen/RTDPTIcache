package com.lewisallen.rtdptiCache.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.lewisallen.rtdptiCache.caches.SIRICache;


public class SiriCacheTest {
	
	@Test
	public void testInitialisation(){
		@SuppressWarnings("unused")
		SIRICache cache = new SIRICache();
	}
	
	/**
	 * Asserts getting from cache wraps in payload.
	 */
	@Test
	public void testSIRICacheWrapping() {
		
		// Create a JSON Object with a random key/value
		String randomInt = Double.toString(Math.random());
		
		JSONObject j = new JSONObject();
		try {
			j.put("Key", randomInt);
		} catch (JSONException e) {
			e.printStackTrace();
			fail();
		}
		
		// Insert JSON Object into cache
		SIRICache.siriCache.put("1", j);
			
		// Get JSON Object using method that wraps with "payload"
		try {
			String res = SIRICache.getSiriJson(new String[]{"1"});
			JSONObject resJSONObject = new JSONObject(res);
			assert(resJSONObject.has("payload"));
		} catch (JSONException e){
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testDodgyCode(){
		try {
			String res = SIRICache.getSiriJson(new String[]{"Dodgy"});
			assertEquals(res, "{\"payload\":{}}");
		} catch (JSONException e) {
			e.printStackTrace();
			fail();
		}
	}
}

