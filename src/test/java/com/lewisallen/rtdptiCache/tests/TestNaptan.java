package com.lewisallen.rtdptiCache.tests;

import org.junit.Before;
import org.junit.Test;

import com.lewisallen.rtdptiCache.Naptan;

public class TestNaptan {

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
