package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.db.NaptanDatabase;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.fail;

public class NaptanDatabaseTest {

	@Test
	public void testDatabaseRetrieval(){
		NaptanDatabase db = new NaptanDatabase();
		
		try {
			@SuppressWarnings("unused")
			ResultSet rs = db.queryNaptan();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			fail();
		}
	}
	
}
