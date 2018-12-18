package com.lewisallen.rtdptiCache.tests;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.lewisallen.rtdptiCache.caches.SIRICache;
import com.lewisallen.rtdptiCache.parser.SIRIResponseParser;

public class SIRIResponseParserTest {
	
	@Test
	public void SIRIParserTest(){
		ResponseEntity<String> res = new ResponseEntity<String>("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Siri xmlns=\"http://www.siri.org.uk/siri\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.siri.org.uk/schema/1.3/siri.xsd\"  version=\"1.3\">   <ServiceDelivery>       <ResponseTimestamp>2018-12-15T02:19:23+00:00</ResponseTimestamp>       <StopMonitoringDelivery version=\"1.3\">           <ResponseTimestamp>2018-12-15T02:19:23+00:00</ResponseTimestamp><MonitoredStopVisit><RecordedAtTime>2018-12-15T02:19:23+00:00</RecordedAtTime><MonitoringRef>149000006061</MonitoringRef><MonitoredVehicleJourney><LineRef>N25</LineRef><DirectionRef>A</DirectionRef><FramedVehicleJourneyRef><DataFrameRef>2018-12-14</DataFrameRef> <DatedVehicleJourneyRef>2040-00007-1</DatedVehicleJourneyRef></FramedVehicleJourneyRef><PublishedLineName>N25</PublishedLineName><DirectionName>Old Steine</DirectionName><OperatorRef>BH</OperatorRef><DestinationName>Old Steine</DestinationName><Monitored>true</Monitored><VehicleRef>426</VehicleRef><MonitoredCall><AimedArrivalTime>2018-12-15T02:19:47+00:00</AimedArrivalTime><ExpectedArrivalTime>2018-12-15T02:19:04+00:00</ExpectedArrivalTime><AimedDepartureTime>2018-12-15T02:19:47+00:00</AimedDepartureTime><ExpectedDepartureTime>2018-12-15T02:19:04+00:00</ExpectedDepartureTime></MonitoredCall></MonitoredVehicleJourney></MonitoredStopVisit><MonitoredStopVisit><RecordedAtTime>2018-12-15T02:19:23+00:00</RecordedAtTime><MonitoringRef>149000006061</MonitoringRef><MonitoredVehicleJourney><LineRef>N25</LineRef><DirectionRef>A</DirectionRef><FramedVehicleJourneyRef><DataFrameRef>2018-12-14</DataFrameRef> <DatedVehicleJourneyRef>2041-00007-1</DatedVehicleJourneyRef></FramedVehicleJourneyRef><PublishedLineName>N25</PublishedLineName><DirectionName>Old Steine</DirectionName><OperatorRef>BH</OperatorRef><DestinationName>Old Steine</DestinationName><Monitored>true</Monitored><VehicleRef>424</VehicleRef><MonitoredCall><AimedArrivalTime>2018-12-15T02:27:47+00:00</AimedArrivalTime><ExpectedArrivalTime>2018-12-15T02:28:33+00:00</ExpectedArrivalTime><AimedDepartureTime>2018-12-15T02:27:47+00:00</AimedDepartureTime><ExpectedDepartureTime>2018-12-15T02:28:36+00:00</ExpectedDepartureTime></MonitoredCall></MonitoredVehicleJourney></MonitoredStopVisit><MonitoredStopVisit><RecordedAtTime>2018-12-15T02:19:23+00:00</RecordedAtTime><MonitoringRef>149000006061</MonitoringRef><MonitoredVehicleJourney><LineRef>N25</LineRef><DirectionRef>A</DirectionRef><FramedVehicleJourneyRef><DataFrameRef>2018-12-14</DataFrameRef> <DatedVehicleJourneyRef>2042-00007-1</DatedVehicleJourneyRef></FramedVehicleJourneyRef><PublishedLineName>N25</PublishedLineName><DirectionName>Old Steine</DirectionName><OperatorRef>BH</OperatorRef><DestinationName>Old Steine</DestinationName><Monitored>true</Monitored><VehicleRef>423</VehicleRef><MonitoredCall><AimedArrivalTime>2018-12-15T02:37:47+00:00</AimedArrivalTime><ExpectedArrivalTime>2018-12-15T02:38:27+00:00</ExpectedArrivalTime><AimedDepartureTime>2018-12-15T02:37:47+00:00</AimedDepartureTime><ExpectedDepartureTime>2018-12-15T02:38:27+00:00</ExpectedDepartureTime></MonitoredCall></MonitoredVehicleJourney></MonitoredStopVisit><MonitoredStopVisit><RecordedAtTime>2018-12-15T02:19:23+00:00</RecordedAtTime><MonitoringRef>149000006061</MonitoringRef><MonitoredVehicleJourney><LineRef>N25</LineRef><DirectionRef>A</DirectionRef><FramedVehicleJourneyRef><DataFrameRef>2018-12-14</DataFrameRef> <DatedVehicleJourneyRef>2043-00007-1</DatedVehicleJourneyRef></FramedVehicleJourneyRef><PublishedLineName>N25</PublishedLineName><DirectionName>Old Steine</DirectionName><OperatorRef>BH</OperatorRef><DestinationName>Old Steine</DestinationName><Monitored>true</Monitored><VehicleRef>438</VehicleRef><MonitoredCall><AimedArrivalTime>2018-12-15T02:45:47+00:00</AimedArrivalTime><ExpectedArrivalTime>2018-12-15T02:46:25+00:00</ExpectedArrivalTime><AimedDepartureTime>2018-12-15T02:45:47+00:00</AimedDepartureTime><ExpectedDepartureTime>2018-12-15T02:46:25+00:00</ExpectedDepartureTime></MonitoredCall></MonitoredVehicleJourney></MonitoredStopVisit><MonitoredStopVisit><RecordedAtTime>2018-12-15T02:19:23+00:00</RecordedAtTime><MonitoringRef>149000006061</MonitoringRef><MonitoredVehicleJourney><LineRef>N25</LineRef><DirectionRef>A</DirectionRef><FramedVehicleJourneyRef><DataFrameRef>2018-12-14</DataFrameRef> <DatedVehicleJourneyRef>2044-00007-1</DatedVehicleJourneyRef></FramedVehicleJourneyRef><PublishedLineName>N25</PublishedLineName><DirectionName>Old Steine</DirectionName><OperatorRef>BH</OperatorRef><DestinationName>Old Steine</DestinationName><Monitored>true</Monitored><VehicleRef>437</VehicleRef><MonitoredCall><AimedArrivalTime>2018-12-15T02:57:47+00:00</AimedArrivalTime><ExpectedArrivalTime>2018-12-15T02:57:47+00:00</ExpectedArrivalTime><AimedDepartureTime>2018-12-15T02:57:47+00:00</AimedDepartureTime><ExpectedDepartureTime>2018-12-15T02:57:47+00:00</ExpectedDepartureTime></MonitoredCall></MonitoredVehicleJourney></MonitoredStopVisit><MonitoredStopVisit><RecordedAtTime>2018-12-15T02:19:23+00:00</RecordedAtTime><MonitoringRef>149000006061</MonitoringRef><MonitoredVehicleJourney><LineRef>N25</LineRef><DirectionRef>A</DirectionRef><FramedVehicleJourneyRef><DataFrameRef>2018-12-14</DataFrameRef> <DatedVehicleJourneyRef>2046-00007-1</DatedVehicleJourneyRef></FramedVehicleJourneyRef><PublishedLineName>N25</PublishedLineName><DirectionName>Old Steine</DirectionName><OperatorRef>BH</OperatorRef><DestinationName>Old Steine</DestinationName><Monitored>true</Monitored><VehicleRef>675</VehicleRef><MonitoredCall><AimedArrivalTime>2018-12-15T03:07:47+00:00</AimedArrivalTime><ExpectedArrivalTime>2018-12-15T03:07:47+00:00</ExpectedArrivalTime><AimedDepartureTime>2018-12-15T03:07:47+00:00</AimedDepartureTime><ExpectedDepartureTime>2018-12-15T03:07:47+00:00</ExpectedDepartureTime></MonitoredCall></MonitoredVehicleJourney></MonitoredStopVisit><MonitoredStopVisit><RecordedAtTime>2018-12-15T02:19:23+00:00</RecordedAtTime><MonitoringRef>149000006061</MonitoringRef><MonitoredVehicleJourney><LineRef>N25</LineRef><DirectionRef>A</DirectionRef><FramedVehicleJourneyRef><DataFrameRef>2018-12-14</DataFrameRef> <DatedVehicleJourneyRef>2047-00007-1</DatedVehicleJourneyRef></FramedVehicleJourneyRef><PublishedLineName>N25</PublishedLineName><DirectionName>Old Steine</DirectionName><OperatorRef>BH</OperatorRef><DestinationName>Old Steine</DestinationName><Monitored>true</Monitored><VehicleRef>421</VehicleRef><MonitoredCall><AimedArrivalTime>2018-12-15T03:17:47+00:00</AimedArrivalTime><ExpectedArrivalTime>2018-12-15T03:17:47+00:00</ExpectedArrivalTime><AimedDepartureTime>2018-12-15T03:17:47+00:00</AimedDepartureTime><ExpectedDepartureTime>2018-12-15T03:17:47+00:00</ExpectedDepartureTime></MonitoredCall></MonitoredVehicleJourney></MonitoredStopVisit>       </StopMonitoringDelivery>   </ServiceDelivery></Siri>", HttpStatus.OK);
		SIRIResponseParser parser = new SIRIResponseParser();
		parser.parse(res);
		
		JSONObject j = new JSONObject(SIRICache.getSiriJson(new String[]{"149000006061"}));
		int length = j.getJSONObject("payload").getJSONObject("149000006061").getJSONArray("MonitoredStopVisits").length();
		
		assertEquals(length, 7);
	}
}