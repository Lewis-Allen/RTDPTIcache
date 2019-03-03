package com.lewisallen.rtdptiCache.requests;

import com.lewisallen.rtdptiCache.AppConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SIRIRequester
{
    private String uri;

    public SIRIRequester()
    {
        this.uri = AppConfig.siriUri;
    }

    /**
     * Getter for URI.
     *
     * @return URI
     */
    public String getUri()
    {
        return uri;
    }

    /**
     * Makes a request to the SIRI service using the provided XML.
     *
     * @param xml XML to be placed in the body of the POST request.
     * @return SIRI response.
     */
    public ResponseEntity<String> makeSIRIRequest(String xml)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);

        HttpEntity<String> request = new HttpEntity<>(xml, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(this.uri, request, String.class);
    }
}
