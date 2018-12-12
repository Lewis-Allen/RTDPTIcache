package com.lewisallen.rtdptiCache.requests;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.lewisallen.rtdptiCache.AppConfig;

public class SIRIRequester {
	
	private String uri;
	
	public SIRIRequester() {
		this.uri = AppConfig.siriUri;
	}

	public String getUri() {
		return uri;
	}
	
	public ResponseEntity<String> makeSIRIRequest(String xml){
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_XML);
	    
	    String body = xml;
	    
	    HttpEntity<String> request = new HttpEntity<String>(body, headers);
	    
	    RestTemplate restTemplate = new RestTemplate();
	    ResponseEntity<String> response = restTemplate.postForEntity(this.uri, request, String.class);
		return response;
	}
}
