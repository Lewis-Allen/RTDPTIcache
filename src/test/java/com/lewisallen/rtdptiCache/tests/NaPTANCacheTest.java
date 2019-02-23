package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.Naptan;
import com.lewisallen.rtdptiCache.caches.NaPTANCache;
import com.lewisallen.rtdptiCache.db.TransportDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NaPTANCacheTest {

	@BeforeAll
	void initialiseNaPTAN(){
		new NaPTANCache();
	}

	@Test
	void testNaPTANStopNames() {
		NaPTANCache.naptanCache = new HashMap<>();

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
			Assertions.assertEquals(res.containsKey(Integer.toString(i)), true);
		}
	}

	@Test
	void testCachedCodes(){
		NaPTANCache.naptanCache = new HashMap<>();

		for(int i = 0; i < 10; i++){
			NaPTANCache.naptanCache.put(Integer.toString(i), new Naptan(Integer.toString(i), "Example Location" + Integer.toString(i), "adj"));
		}

		Set<String> keys = NaPTANCache.getCachedCodes();

		Assertions.assertEquals(10, keys.size());
	}
	
	@Test
	void testCachePopulate(){
		try {
			NaPTANCache.populateCache(new TransportDatabase());
		} catch (Exception e){
			e.printStackTrace();
			Assertions.fail("Failed to populate cache.");
		}
	}

	@Test
	void testDoesStopExist()
	{
		Assertions.assertFalse(NaPTANCache.checkStopExists("shouldn't exist"));

		NaPTANCache.naptanCache.put("shouldn't exist", new Naptan("shouldn't exist'", "Test", "adj"));

		Assertions.assertTrue(NaPTANCache.checkStopExists("shouldn't exist"));
	}

	@Test
	void testGetNaptan()
	{
		Naptan naptan = new Naptan("naptan", "Test Naptan", "adj");
		Assertions.assertEquals(NaPTANCache.getNaptan("naptan"), null);

		NaPTANCache.naptanCache.put("naptan", naptan);

		Assertions.assertEquals(NaPTANCache.getNaptan("naptan"), naptan);
	}
}
