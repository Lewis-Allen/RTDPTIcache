package com.lewisallen.rtdptiCache;

import static org.junit.Assert.fail;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.lewisallen.rtdptiCache.caches.SIRICache;


public class SiriCacheTest {
	
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
		String res = SIRICache.getSiriJson(new String[]{"1"});
		
		try {
			JSONObject resJSONObject = new JSONObject(res);
			assert(resJSONObject.has("payload"));
		} catch (JSONException e) {
			e.printStackTrace();
			fail();
		}
	}
}

