package com.lewisallen.rtdptiCache.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

import com.lewisallen.rtdptiCache.AppConfig;
import com.lewisallen.rtdptiCache.requests.SIRIRequester;

import io.github.cdimascio.dotenv.Dotenv;

public class SIRIRequesterTest {
	
	
	@Before
	public void setup(){
		new AppConfig();
	}
	
	
	@Test
	public void testEnv(){
		Dotenv env = Dotenv.load();
		
		SIRIRequester requester = new SIRIRequester();
		assertEquals(requester.getUri(), env.get("SIRI_URI"));
		assert(!StringUtils.isEmpty(requester.getUri()));
	}
	
	/*
	
	@Test
	public void testSiriRequest(){
		SIRIRequester siriRequest = new SIRIRequester();
		ResponseEntity<String> response = siriRequest.makeSIRIRequest("<ServiceDelivery></ServiceDelivery>");
		
	    assertEquals(response.getStatusCode(),HttpStatus.OK);
	}
	*/
}