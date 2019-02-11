package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.requests.SIRIString;
import org.junit.jupiter.api.Test;

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
