package com.lewisallen.rtdptiCache;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.lewisallen.rtdptiCache.caches.NaPTANCache;

public class NaPTANCacheTest {
	
	@Test
	public void testInitialisation(){
		@SuppressWarnings("unused")
		NaPTANCache cache = new NaPTANCache();
	}

	@Test
	public void testNaPTANCacheCodes() {
		
		// Populate data.
		for(int i = 0; i < 10; i++){
			NaPTANCache.naptanCache.put(Integer.toString(i), Integer.toString(i));
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

}
