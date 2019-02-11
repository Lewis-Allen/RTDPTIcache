package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.caches.SIRICache;
import com.lewisallen.rtdptiCache.jobs.ScheduledTasks;
import com.lewisallen.rtdptiCache.parser.DepartureComparator;
import com.lewisallen.rtdptiCache.parser.SIRIResponseParser;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SIRIResponseParserTest {

    /*
        NOTE: Several of these tests assume that stop "149000006061" has the retrieve flag set in the cache.
              These could do with being rewritten.
     */
    @BeforeAll
    void setup(){
        ScheduledTasks tasks = new ScheduledTasks();
        tasks.updateNaPTANCache();
    }

    @Test
    void SIRIParserTest(){
        // Parse a response with many stop visits.
        ResponseEntity<String> res = new ResponseEntity<String>("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Siri xmlns=\"http://www.siri.org.uk/siri\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.siri.org.uk/schema/1.3/siri.xsd\"  version=\"1.3\">   <ServiceDelivery>       <ResponseTimestamp>2018-12-15T02:19:23+00:00</ResponseTimestamp>       <StopMonitoringDelivery version=\"1.3\">           <ResponseTimestamp>2018-12-15T02:19:23+00:00</ResponseTimestamp><MonitoredStopVisit><RecordedAtTime>2018-12-15T02:19:23+00:00</RecordedAtTime><MonitoringRef>149000006061</MonitoringRef><MonitoredVehicleJourney><LineRef>N25</LineRef><DirectionRef>A</DirectionRef><FramedVehicleJourneyRef><DataFrameRef>2018-12-14</DataFrameRef> <DatedVehicleJourneyRef>2040-00007-1</DatedVehicleJourneyRef></FramedVehicleJourneyRef><PublishedLineName>N25</PublishedLineName><DirectionName>Old Steine</DirectionName><OperatorRef>BH</OperatorRef><DestinationName>Old Steine</DestinationName><Monitored>true</Monitored><VehicleRef>426</VehicleRef><MonitoredCall><AimedArrivalTime>2018-12-15T02:19:47+00:00</AimedArrivalTime><ExpectedArrivalTime>2018-12-15T02:19:04+00:00</ExpectedArrivalTime><AimedDepartureTime>2018-12-15T02:19:47+00:00</AimedDepartureTime><ExpectedDepartureTime>2018-12-15T02:19:04+00:00</ExpectedDepartureTime></MonitoredCall></MonitoredVehicleJourney></MonitoredStopVisit><MonitoredStopVisit><RecordedAtTime>2018-12-15T02:19:23+00:00</RecordedAtTime><MonitoringRef>149000006061</MonitoringRef><MonitoredVehicleJourney><LineRef>N25</LineRef><DirectionRef>A</DirectionRef><FramedVehicleJourneyRef><DataFrameRef>2018-12-14</DataFrameRef> <DatedVehicleJourneyRef>2041-00007-1</DatedVehicleJourneyRef></FramedVehicleJourneyRef><PublishedLineName>N25</PublishedLineName><DirectionName>Old Steine</DirectionName><OperatorRef>BH</OperatorRef><DestinationName>Old Steine</DestinationName><Monitored>true</Monitored><VehicleRef>424</VehicleRef><MonitoredCall><AimedArrivalTime>2018-12-15T02:27:47+00:00</AimedArrivalTime><ExpectedArrivalTime>2018-12-15T02:28:33+00:00</ExpectedArrivalTime><AimedDepartureTime>2018-12-15T02:27:47+00:00</AimedDepartureTime><ExpectedDepartureTime>2018-12-15T02:28:36+00:00</ExpectedDepartureTime></MonitoredCall></MonitoredVehicleJourney></MonitoredStopVisit><MonitoredStopVisit><RecordedAtTime>2018-12-15T02:19:23+00:00</RecordedAtTime><MonitoringRef>149000006061</MonitoringRef><MonitoredVehicleJourney><LineRef>N25</LineRef><DirectionRef>A</DirectionRef><FramedVehicleJourneyRef><DataFrameRef>2018-12-14</DataFrameRef> <DatedVehicleJourneyRef>2042-00007-1</DatedVehicleJourneyRef></FramedVehicleJourneyRef><PublishedLineName>N25</PublishedLineName><DirectionName>Old Steine</DirectionName><OperatorRef>BH</OperatorRef><DestinationName>Old Steine</DestinationName><Monitored>true</Monitored><VehicleRef>423</VehicleRef><MonitoredCall><AimedArrivalTime>2018-12-15T02:37:47+00:00</AimedArrivalTime><ExpectedArrivalTime>2018-12-15T02:38:27+00:00</ExpectedArrivalTime><AimedDepartureTime>2018-12-15T02:37:47+00:00</AimedDepartureTime><ExpectedDepartureTime>2018-12-15T02:38:27+00:00</ExpectedDepartureTime></MonitoredCall></MonitoredVehicleJourney></MonitoredStopVisit><MonitoredStopVisit><RecordedAtTime>2018-12-15T02:19:23+00:00</RecordedAtTime><MonitoringRef>149000006061</MonitoringRef><MonitoredVehicleJourney><LineRef>N25</LineRef><DirectionRef>A</DirectionRef><FramedVehicleJourneyRef><DataFrameRef>2018-12-14</DataFrameRef> <DatedVehicleJourneyRef>2043-00007-1</DatedVehicleJourneyRef></FramedVehicleJourneyRef><PublishedLineName>N25</PublishedLineName><DirectionName>Old Steine</DirectionName><OperatorRef>BH</OperatorRef><DestinationName>Old Steine</DestinationName><Monitored>true</Monitored><VehicleRef>438</VehicleRef><MonitoredCall><AimedArrivalTime>2018-12-15T02:45:47+00:00</AimedArrivalTime><ExpectedArrivalTime>2018-12-15T02:46:25+00:00</ExpectedArrivalTime><AimedDepartureTime>2018-12-15T02:45:47+00:00</AimedDepartureTime><ExpectedDepartureTime>2018-12-15T02:46:25+00:00</ExpectedDepartureTime></MonitoredCall></MonitoredVehicleJourney></MonitoredStopVisit><MonitoredStopVisit><RecordedAtTime>2018-12-15T02:19:23+00:00</RecordedAtTime><MonitoringRef>149000006061</MonitoringRef><MonitoredVehicleJourney><LineRef>N25</LineRef><DirectionRef>A</DirectionRef><FramedVehicleJourneyRef><DataFrameRef>2018-12-14</DataFrameRef> <DatedVehicleJourneyRef>2044-00007-1</DatedVehicleJourneyRef></FramedVehicleJourneyRef><PublishedLineName>N25</PublishedLineName><DirectionName>Old Steine</DirectionName><OperatorRef>BH</OperatorRef><DestinationName>Old Steine</DestinationName><Monitored>true</Monitored><VehicleRef>437</VehicleRef><MonitoredCall><AimedArrivalTime>2018-12-15T02:57:47+00:00</AimedArrivalTime><ExpectedArrivalTime>2018-12-15T02:57:47+00:00</ExpectedArrivalTime><AimedDepartureTime>2018-12-15T02:57:47+00:00</AimedDepartureTime><ExpectedDepartureTime>2018-12-15T02:57:47+00:00</ExpectedDepartureTime></MonitoredCall></MonitoredVehicleJourney></MonitoredStopVisit><MonitoredStopVisit><RecordedAtTime>2018-12-15T02:19:23+00:00</RecordedAtTime><MonitoringRef>149000006061</MonitoringRef><MonitoredVehicleJourney><LineRef>N25</LineRef><DirectionRef>A</DirectionRef><FramedVehicleJourneyRef><DataFrameRef>2018-12-14</DataFrameRef> <DatedVehicleJourneyRef>2046-00007-1</DatedVehicleJourneyRef></FramedVehicleJourneyRef><PublishedLineName>N25</PublishedLineName><DirectionName>Old Steine</DirectionName><OperatorRef>BH</OperatorRef><DestinationName>Old Steine</DestinationName><Monitored>true</Monitored><VehicleRef>675</VehicleRef><MonitoredCall><AimedArrivalTime>2018-12-15T03:07:47+00:00</AimedArrivalTime><ExpectedArrivalTime>2018-12-15T03:07:47+00:00</ExpectedArrivalTime><AimedDepartureTime>2018-12-15T03:07:47+00:00</AimedDepartureTime><ExpectedDepartureTime>2018-12-15T03:07:47+00:00</ExpectedDepartureTime></MonitoredCall></MonitoredVehicleJourney></MonitoredStopVisit><MonitoredStopVisit><RecordedAtTime>2018-12-15T02:19:23+00:00</RecordedAtTime><MonitoringRef>149000006061</MonitoringRef><MonitoredVehicleJourney><LineRef>N25</LineRef><DirectionRef>A</DirectionRef><FramedVehicleJourneyRef><DataFrameRef>2018-12-14</DataFrameRef> <DatedVehicleJourneyRef>2047-00007-1</DatedVehicleJourneyRef></FramedVehicleJourneyRef><PublishedLineName>N25</PublishedLineName><DirectionName>Old Steine</DirectionName><OperatorRef>BH</OperatorRef><DestinationName>Old Steine</DestinationName><Monitored>true</Monitored><VehicleRef>421</VehicleRef><MonitoredCall><AimedArrivalTime>2018-12-15T03:17:47+00:00</AimedArrivalTime><ExpectedArrivalTime>2018-12-15T03:17:47+00:00</ExpectedArrivalTime><AimedDepartureTime>2018-12-15T03:17:47+00:00</AimedDepartureTime><ExpectedDepartureTime>2018-12-15T03:17:47+00:00</ExpectedDepartureTime></MonitoredCall></MonitoredVehicleJourney></MonitoredStopVisit>       </StopMonitoringDelivery>   </ServiceDelivery></Siri>", HttpStatus.OK);
        SIRIResponseParser parser = new SIRIResponseParser();
        parser.parse(res);

        // Ensure the data has been added to the cache.
        Assertions.assertTrue(SIRICache.siriCache.containsKey("149000006061"));

        JSONObject j = new JSONObject(SIRICache.getSiriJson(new String[]{"149000006061"}).toString());
        int length = j.getJSONObject("busStops").getJSONObject("149000006061").getJSONArray("MonitoredStopVisits").length();

        Assertions.assertEquals(length, 7);
    }


    @Test
    void SingleStopVisitTest(){
        // Parse a response with only one stop visit.
        ResponseEntity<String> res = new ResponseEntity<String>("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Siri xmlns=\"http://www.siri.org.uk/siri\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.siri.org.uk/schema/1.3/siri.xsd\"  version=\"1.3\"><ServiceDelivery><ResponseTimestamp>2018-12-15T02:19:23+00:00</ResponseTimestamp><StopMonitoringDelivery version=\"1.3\"><ResponseTimestamp>2018-12-15T02:19:23+00:00</ResponseTimestamp><MonitoredStopVisit><RecordedAtTime>2018-12-15T02:19:23+00:00</RecordedAtTime><MonitoringRef>149000006061</MonitoringRef><MonitoredVehicleJourney><LineRef>N25</LineRef><DirectionRef>A</DirectionRef><FramedVehicleJourneyRef><DataFrameRef>2018-12-14</DataFrameRef><DatedVehicleJourneyRef>2040-00007-1</DatedVehicleJourneyRef></FramedVehicleJourneyRef><PublishedLineName>N25</PublishedLineName><DirectionName>Old Steine</DirectionName><OperatorRef>BH</OperatorRef><DestinationName>Old Steine</DestinationName><Monitored>true</Monitored><VehicleRef>426</VehicleRef><MonitoredCall><AimedArrivalTime>2018-12-15T02:19:47+00:00</AimedArrivalTime><ExpectedArrivalTime>2018-12-15T02:19:04+00:00</ExpectedArrivalTime><AimedDepartureTime>2018-12-15T02:19:47+00:00</AimedDepartureTime><ExpectedDepartureTime>2018-12-15T02:19:04+00:00</ExpectedDepartureTime></MonitoredCall></MonitoredVehicleJourney></MonitoredStopVisit></StopMonitoringDelivery></ServiceDelivery></Siri>", HttpStatus.OK);

        SIRIResponseParser parser = new SIRIResponseParser();
        parser.parse(res);

        // Check the data was successfully added to the cache.
        Assertions.assertTrue(SIRICache.siriCache.containsKey("149000006061"));

        // Ensure the data added is correct.
        JSONObject j = new JSONObject(SIRICache.getSiriJson(new String[]{"149000006061"}).toString());
        int length = j.getJSONObject("busStops").getJSONObject("149000006061").getJSONArray("MonitoredStopVisits").length();
        Assertions.assertEquals(length, 1);
    }

    @Test
    void NoStopVisitTest(){
        // Add some dummy data to the cache.
        SIRICache.siriCache.put("1", new JSONObject());

        // Parse a request containing no stop visits
        ResponseEntity<String> res = new ResponseEntity<String>("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?><Siri xmlns=\"http://www.siri.org.uk/siri\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.siri.org.uk/schema/1.3/siri.xsd\"  version=\"1.3\"><ServiceDelivery><ResponseTimestamp>2018-12-20T22:01:35+00:00</ResponseTimestamp><StopMonitoringDelivery version=\"1.3\"><ResponseTimestamp>2018-12-20T22:01:35+00:00</ResponseTimestamp><Note> There is no stop information available for this request</Note></StopMonitoringDelivery></ServiceDelivery></Siri>", HttpStatus.OK);

        SIRIResponseParser parser = new SIRIResponseParser();
        parser.parse(res);

        // Check cache has been wiped.
        Assertions.assertTrue(SIRICache.siriCache.isEmpty());
    }

    @Test
    void MissingExpectedDepartureTimeTest(){
        // Parse a request where the ExpectedDepartureTime is missing from the stop visit(s).
        ResponseEntity<String> res = new ResponseEntity<String>("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Siri xmlns=\"http://www.siri.org.uk/siri\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.siri.org.uk/schema/1.3/siri.xsd\"  version=\"1.3\"><ServiceDelivery><ResponseTimestamp>2018-12-15T02:19:23+00:00</ResponseTimestamp><StopMonitoringDelivery version=\"1.3\"><ResponseTimestamp>2018-12-15T02:19:23+00:00</ResponseTimestamp><MonitoredStopVisit><RecordedAtTime>2018-12-15T02:19:23+00:00</RecordedAtTime><MonitoringRef>149000006061</MonitoringRef><MonitoredVehicleJourney><LineRef>N25</LineRef><DirectionRef>A</DirectionRef><FramedVehicleJourneyRef><DataFrameRef>2018-12-14</DataFrameRef><DatedVehicleJourneyRef>2040-00007-1</DatedVehicleJourneyRef></FramedVehicleJourneyRef><PublishedLineName>N25</PublishedLineName><DirectionName>Old Steine</DirectionName><OperatorRef>BH</OperatorRef><DestinationName>Old Steine</DestinationName><Monitored>true</Monitored><VehicleRef>426</VehicleRef><MonitoredCall><AimedArrivalTime>2018-12-15T02:19:47+00:00</AimedArrivalTime><ExpectedArrivalTime>2018-12-15T02:19:04+00:00</ExpectedArrivalTime><AimedDepartureTime>2018-12-15T02:19:47+00:00</AimedDepartureTime></MonitoredCall></MonitoredVehicleJourney></MonitoredStopVisit></StopMonitoringDelivery></ServiceDelivery></Siri>", HttpStatus.OK);

        SIRIResponseParser parser = new SIRIResponseParser();
        parser.parse(res);

        Assertions.assertTrue(SIRICache.siriCache.containsKey("149000006061"));
    }

    @Test
    void TestBadJSON(){
        JSONObject o1 = new JSONObject();
        JSONObject o2 = new JSONObject();

        DepartureComparator sorter = new DepartureComparator();

        Assertions.assertEquals(sorter.compare(o1,o2), 0);
    }
}
