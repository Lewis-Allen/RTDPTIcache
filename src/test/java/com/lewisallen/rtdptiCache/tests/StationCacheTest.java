package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.Naptan;
import com.lewisallen.rtdptiCache.Station;
import com.lewisallen.rtdptiCache.caches.NaPTANCache;
import com.lewisallen.rtdptiCache.caches.TrainStationCache;
import com.lewisallen.rtdptiCache.db.TransportDatabase;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StationCacheTest {

	@Test
	public void testStationCacheCodes() {
		
		// Populate data.
		for(int i = 0; i < 10; i++){
			TrainStationCache.stationCache.put(Integer.toString(i), new Station("Example Location" + Integer.toString(i), Integer.toString(i)));
		}
		
		// Generate list of keys to pass to function.
		String[] codesList = new String[10];
		for(int i = 0; i < 10; i++){
			codesList[i] = Integer.toString(i);
		}
		
		// Get response from cache.
		Map<String, String> res = TrainStationCache.getStationNames(codesList);
		
		// Test response.
		for(int i = 0; i < 10; i++){
			assertEquals(res.containsKey(Integer.toString(i)), true);
		}
	}
	
	@Test
	public void testCachePopulate(){
		try {
			TrainStationCache.populateCache(new TransportDatabase());
		} catch (Exception e){
			e.printStackTrace();
			fail();
		}
	}

}
