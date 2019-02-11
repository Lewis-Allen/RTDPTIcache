package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.Station;
import com.lewisallen.rtdptiCache.caches.TrainStationCache;
import com.lewisallen.rtdptiCache.db.TransportDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StationCacheTest {

	@BeforeAll
	public void initialiseStationCache(){
		new TrainStationCache();
	}

	@Test
	public void testStationNames() {
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
	public void testCachedCodes(){
		TrainStationCache.stationCache = new HashMap<>();

		for(int i = 0; i < 10; i++){
			TrainStationCache.stationCache.put(Integer.toString(i), new Station("Example Location" + Integer.toString(i), Integer.toString(i)));
		}

		Set<String> keys = TrainStationCache.getCachedCodes();

		assertEquals(10, keys.size());
	}
	
	@Test
	public void testCachePopulate(){
		try {
			TrainStationCache.populateCache(new TransportDatabase());
		} catch (Exception e){
			e.printStackTrace();
			fail("Failed to populate cache");
		}
	}
}
