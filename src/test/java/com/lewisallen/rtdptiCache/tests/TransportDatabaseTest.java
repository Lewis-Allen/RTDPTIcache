package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.db.TransportDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransportDatabaseTest {

	@Test
	void testDatabaseRetrieval(){
		TransportDatabase db = new TransportDatabase();
		
		try {
			@SuppressWarnings("unused")
			ResultSet rs = db.queryNaptan();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			Assertions.fail();
		}
	}
	
}
