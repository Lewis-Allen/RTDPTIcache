package com.lewisallen.rtdptiCache.requests;

import com.lewisallen.rtdptiCache.AppConfig;

public class SIRIRequester {
	
	private String uri;
	
	public SIRIRequester() {
		this.uri = (AppConfig.siriUri);
	}

	public String getUri() {
		return uri;
	}
}
