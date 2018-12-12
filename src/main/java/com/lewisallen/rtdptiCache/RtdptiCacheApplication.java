package com.lewisallen.rtdptiCache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.lewisallen.rtdptiCache.db.NaptanDatabase;
import com.lewisallen.rtdptiCache.requests.SIRIString;

@SpringBootApplication
public class RtdptiCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(RtdptiCacheApplication.class, args);
		
		new AppConfig();
		
		NaptanDatabase db = new NaptanDatabase();
		ResultSet rs;

		List<String> naptans = new ArrayList<String>();
		try 
		{
			rs = db.queryNaptan();
			
			while(rs.next()){
				String naptan = rs.getString("SystemCodeNumber");
				naptans.add(naptan);
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		SIRIString ss = new SIRIString();
		
		System.out.println(ss.generateXml(naptans.stream()
												 .toArray(String[]::new)));
	}
}
