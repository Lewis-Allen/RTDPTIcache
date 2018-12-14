package com.lewisallen.rtdptiCache.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;

import com.lewisallen.rtdptiCache.Naptan;
import com.lewisallen.rtdptiCache.caches.NaPTANCache;
import com.lewisallen.rtdptiCache.db.NaptanDatabase;

public class NaPTANCacheTest {

	@Test
	public void testNaPTANCacheCodes() {
		
		// Populate data.
		for(int i = 0; i < 10; i++){
			NaPTANCache.naptanCache.put(Integer.toString(i), new Naptan(Integer.toString(i), "Example Location" + Integer.toString(i), "adj"));
		}
		
		// Generate list of keys to pass to function.
		String[] codesList = new String[10];
		for(int i = 0; i < 10; i++){
			codesList[i] = Integer.toString(i);
		}
		
		// Get response from cache.
		Map<String, String> res = NaPTANCache.getStopNames(codesList);
		
		// Test response.
		for(int i = 0; i < 10; i++){
			assertEquals(res.containsKey(Integer.toString(i)), true);
		}
	}
	
	@Test
	public void testCachePopulate(){
		try {
			NaPTANCache.populateCache(new NaptanDatabase());
		} catch (Exception e){
			e.printStackTrace();
			fail();
		}
	}

}
