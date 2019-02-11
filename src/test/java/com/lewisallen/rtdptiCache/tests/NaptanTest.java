package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.Naptan;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NaptanTest {

	private Naptan naptan;
	
	@BeforeAll
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
