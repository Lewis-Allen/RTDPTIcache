package com.lewisallen.rtdptiCache.cacheTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.util.StringUtils;

import com.lewisallen.rtdptiCache.AppConfig;
import com.lewisallen.rtdptiCache.requests.SIRIRequester;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvTest {
	
	@Test
	public void testEnv(){
		new AppConfig();
		
		Dotenv env = Dotenv.load();
		
		SIRIRequester requester = new SIRIRequester();
		assertEquals(requester.getUri(), env.get("SIRI_URI"));
		assert(!StringUtils.isEmpty(requester.getUri()));
	}
}
