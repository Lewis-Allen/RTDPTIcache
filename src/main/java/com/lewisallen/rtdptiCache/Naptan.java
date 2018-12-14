package com.lewisallen.rtdptiCache;

public class Naptan {
	
	private String systemCodeNumber;
	private String longDescription;
	private String identifier;
	
	public Naptan(String systemCodeNumber, String longDescription, String identifier){
		this.systemCodeNumber = systemCodeNumber;
		this.longDescription = longDescription;
		this.identifier = identifier;
	}

	public String getSystemCodeNumber() {
		return systemCodeNumber;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public String getIdentifier() {
		return identifier;
	}
}
