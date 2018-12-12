package com.lewisallen.rtdptiCache.tests;

import org.junit.Test;

import com.lewisallen.rtdptiCache.requests.SIRIString;

public class SIRIStringTest {

	@Test
	public void testXmlBuilder(){
		String[] naptans = new String[]{"123456789", "987654321"};
		SIRIString xmlString = new SIRIString();
		
		xmlString.generateXml(naptans);
		
		for(String s : naptans){
			assert(xmlString.getXml().contains(s));
		}
	}
}
