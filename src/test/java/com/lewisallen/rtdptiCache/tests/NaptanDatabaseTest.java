package com.lewisallen.rtdptiCache.tests;

import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.lewisallen.rtdptiCache.db.NaptanDatabase;

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
