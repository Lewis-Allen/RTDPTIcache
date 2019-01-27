package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.db.TransportDatabase;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.fail;

public class TransportDatabaseTest {

	@Test
	public void testDatabaseRetrieval(){
		TransportDatabase db = new TransportDatabase();
		
		try {
			@SuppressWarnings("unused")
			ResultSet rs = db.queryNaptan();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			fail();
		}
	}
	
}
