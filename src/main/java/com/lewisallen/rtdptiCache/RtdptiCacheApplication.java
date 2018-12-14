package com.lewisallen.rtdptiCache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.lewisallen.rtdptiCache.caches.NaPTANCache;
import com.lewisallen.rtdptiCache.caches.SIRICache;
import com.lewisallen.rtdptiCache.db.NaptanDatabase;
import com.lewisallen.rtdptiCache.parser.SIRIResponseParser;
import com.lewisallen.rtdptiCache.requests.SIRIRequester;
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
		catch (SQLException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		SIRIString ss = new SIRIString();
		
		String req = ss.generateXml(naptans.stream()
										   .toArray(String[]::new));
		
		SIRIRequester requester = new SIRIRequester();
		
		NaPTANCache.naptanCache.put("149000006061", new Naptan("149000006061","Example Location One", "opp"));
		NaPTANCache.naptanCache.put("149000006062", new Naptan("149000006062","Example Location Two", "adj"));
		SIRIResponseParser parser = new SIRIResponseParser();
		parser.parse(requester.makeSIRIRequest(req));
	
		System.out.println(SIRICache.getSiriJson(naptans.stream().toArray(String[]::new)));
	}
}
