package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.requests.SIRIRequester;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class SIRIRequesterTest
{
    @Value("${rtdpti.siriuri}")
    private String siriuri;

    /*
    @Test
    void testEnv()
    {
        Dotenv env = Dotenv.load();

        SIRIRequester requester = new SIRIRequester(siriuri);
        Assertions.assertEquals(requester.getUri(), env.get("SIRI_URI"));
        Assertions.assertTrue(!StringUtils.isEmpty(requester.getUri()));
    }

     */

    @Test
    void testSiriRequest()
    {
        SIRIRequester siriRequest = new SIRIRequester(siriuri);
        ResponseEntity<String> response = siriRequest.makeSIRIRequest("<ServiceDelivery></ServiceDelivery>");

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
