package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.AppConfig;
import com.lewisallen.rtdptiCache.requests.SIRIRequester;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SIRIRequesterTest {

	@BeforeAll
	void setup(){
		new AppConfig();
	}

	@Test
	void testEnv(){
		Dotenv env = Dotenv.load();

		SIRIRequester requester = new SIRIRequester();
		Assertions.assertEquals(requester.getUri(), env.get("SIRI_URI"));
		Assertions.assertTrue(!StringUtils.isEmpty(requester.getUri()));
	}

	@Test
	@DisabledIfEnvironmentVariable(named="CI", matches="true")
	void testSiriRequest(){
		SIRIRequester siriRequest = new SIRIRequester();
		ResponseEntity<String> response = siriRequest.makeSIRIRequest("<ServiceDelivery></ServiceDelivery>");
		
	    Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	@EnabledIfEnvironmentVariable(named="CI", matches="true")
	void ciTestSiriRequest(){
		AppConfig.siriUri = System.getenv("SIRI_URI");
		testSiriRequest();
	}
}
