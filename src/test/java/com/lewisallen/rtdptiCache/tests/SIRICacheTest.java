package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.caches.SIRICache;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SIRICacheTest {
	
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
			Assertions.fail("Failed to add key with random integer");
		}
		
		// Insert JSON Object into cache
		SIRICache.siriCache.put("1", j);
			
		// Ensure method returns stops.
		try {
			JSONObject res = SIRICache.getSiriJson(new String[]{"1"});
			Assertions.assertTrue(res.getJSONObject("busStops").has("1"));
		} catch (JSONException e){
			e.printStackTrace();
			Assertions.fail();
		}
	}
	
	@Test
	public void testDodgyCode(){
		try {
			JSONObject res = SIRICache.getSiriJson(new String[]{"Dodgy"});
			Assertions.assertEquals(res.toString(), "{\"busStops\":{}}");
		} catch (JSONException e) {
			e.printStackTrace();
			Assertions.fail();
		}
	}
}

