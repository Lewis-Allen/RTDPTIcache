package com.lewisallen.rtdptiCache.requests;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SIRIString {

	// Xml string that will be sent to SIRI
	private String xml;
	
	private final String baseXml = "<Siri xmlns=\"http://www.siri.org.uk/siri\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.siri.org.uk/schema/1.3/siri.xsd\"  version=\"1.3\"><ServiceRequest></ServiceRequest></Siri>";
	private final String stopXml = "<StopMonitoringRequest version=\"1.3\"><PreviewInterval>PT60M</PreviewInterval></StopMonitoringRequest>";
	
	public SIRIString(){
		
	}
	
	public void generateXml(String[] naptans){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
		DocumentBuilder builder;  
		Document doc;
		try {  
		    builder = factory.newDocumentBuilder();
		    doc = builder.parse(new InputSource(new StringReader(baseXml)));

			Node serviceDelivery = doc.getDocumentElement().getElementsByTagName("ServiceRequest").item(0);
			
			for(String naptan : naptans)
			{
				serviceDelivery.appendChild(busStopNode(naptan, builder));
			}
			
			System.out.println(doc.toString());
		} catch (Exception e) {  
		    e.printStackTrace();  
		} 
	}
	
	private Node busStopNode(String naptan, DocumentBuilder builder) throws SAXException, IOException{
		Document doc = builder.parse(new InputSource(new StringReader(stopXml)));
		
		Document monitoringRef = builder.parse(new InputSource(new StringReader("<MonitoringRef></MonitoringRef>")));
		monitoringRef.setNodeValue(naptan);
		
		doc.getDocumentElement().appendChild(monitoringRef);
		return doc;
	}
}
