package com.lewisallen.rtdptiCache.parser;

import java.time.OffsetDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.scenario.effect.Offset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.ResponseEntity;

import com.lewisallen.rtdptiCache.caches.NaPTANCache;
import com.lewisallen.rtdptiCache.caches.SIRICache;

public class SIRIResponseParser {
	
    /**
     * Parses a response from the SIRI service and updates the cache.
     * @param response A response received from the SIRI service.
     */
	public void parse(ResponseEntity<String> response){
		JSONObject siriResponse = XML.toJSONObject(response.getBody());

		// To Do - Add a catch here. Consider when a catch might be thrown. No stop visits for all bus locations requested?
		JSONArray monitoredStops = siriResponse.getJSONObject("Siri")
					.getJSONObject("ServiceDelivery")
					.getJSONObject("StopMonitoringDelivery")
					.getJSONArray("MonitoredStopVisit");
		
		 List<JSONObject> monitoredStopsList = new ArrayList<JSONObject>();
	        
         // Convert JSONArray to standard List<JSONObject>
         for(int i = 0; i < monitoredStops.length(); i++){
         	monitoredStopsList.add(monitoredStops.getJSONObject(i));
         }
         
         // Go through the JSON list and group items by into lists by their MonitoringRef
         Map<Object, List<JSONObject>> groupedList = monitoredStopsList.parallelStream()
         											      			   .collect(Collectors.groupingByConcurrent(a -> 
         											      			 		{
         											      			 			try {
         											      			 				return a.get("MonitoringRef").toString();
         											      			 			} catch (JSONException e) {
         											      			 				e.printStackTrace();
         											      			 			}
         											      			 			return a;
         											      			 		}));
         
         // Create a cache to hold list of stops alongside other info.
         Map<Object, JSONObject> cache = new HashMap<Object, JSONObject>();
         
         for(Object naptanKey : groupedList.keySet()){
     
             // Sort list of stops by ascending expected arrival time.
        	 Collections.sort(groupedList.get(naptanKey), new JSONSorter());
        	 
        	 // Remove irrelevant info from json
        	 List<JSONObject> trimmedList = groupedList.get(naptanKey)
        	 										   .stream()
        	 										   .map((JSONObject j) -> removeFields(j))
					 								   .map((JSONObject j) -> addDeparture(j))
			 										   .collect(Collectors.toList());

        	 // Add a "Departure" field to JSON
			 trimmedList = trimmedList.stream()
					                  .collect(Collectors.toList());
        	 
        	 // Create JSON objects to store in cache.
        	 JSONObject jsonToCache = new JSONObject();

        	 // Store some name information from the NaPTAN Cache alongside the stop visits.
        	 jsonToCache.put("StopName", NaPTANCache.naptanCache.get(naptanKey).getLongDescription());
        	 jsonToCache.put("Identifier", NaPTANCache.naptanCache.get(naptanKey).getIdentifier());
        	 jsonToCache.put("MonitoredStopVisits", trimmedList);

        	 // Add final object to local cache.
        	 cache.put(naptanKey, jsonToCache);
         }
         
         // Copy the local cache to the global cache.
         SIRICache.siriCache = cache;
	}
	
	private JSONObject removeFields(JSONObject j){
		j.remove("RecordedAtTime");
  		j.remove("MonitoringRef");
  		
  		j.getJSONObject("MonitoredVehicleJourney").remove("DirectionRef");
  		j.getJSONObject("MonitoredVehicleJourney").remove("Monitored");
  		j.getJSONObject("MonitoredVehicleJourney").remove("VehicleRef");
  		j.getJSONObject("MonitoredVehicleJourney").remove("OperatorRef");
  		j.getJSONObject("MonitoredVehicleJourney").remove("FramedVehicleJourneyRef");
  		j.getJSONObject("MonitoredVehicleJourney").remove("DirectionRef");
  		j.getJSONObject("MonitoredVehicleJourney").remove("DirectionName");
  		j.getJSONObject("MonitoredVehicleJourney").remove("LineRef");
  		return j;
	}

	private JSONObject addDeparture(JSONObject j){
		long departureSeconds = 0;
		// Check if stop has expected departure time
		if(j.getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").has("ExpectedDepartureTime"))
		{
			try
			{
				OffsetDateTime expectedDepartureTime = OffsetDateTime.parse(j.getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").get("ExpectedDepartureTime").toString());
				departureSeconds = OffsetDateTime.now().until(expectedDepartureTime, ChronoUnit.SECONDS);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if(j.getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").has("AimedDepartureTime")) {
			try
			{
				OffsetDateTime aimedDepartureTime = OffsetDateTime.parse(j.getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").get("AimedDepartureTime").toString());
				departureSeconds = OffsetDateTime.now().until(aimedDepartureTime, ChronoUnit.SECONDS);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		j.getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").put("DepartureSeconds", departureSeconds);
		return j;
	}
}
