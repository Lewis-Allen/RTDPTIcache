package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.Naptan;
import org.junit.Before;
import org.junit.Test;

public class NaptanTest {

	private Naptan naptan;
	
	@Before
	public void setup(){
		naptan = new Naptan("123456789", "England", "opp");
	}
	
	@Test
	public void codeRetrieveTest(){
		naptan.getSystemCodeNumber();
	}
	
	@Test
	public void identifierRetrieveTest(){
		naptan.getIdentifier();
	}
	
	@Test
	public void longDescriptionRetrieveTest(){
		naptan.getLongDescription();
	}
	
}
