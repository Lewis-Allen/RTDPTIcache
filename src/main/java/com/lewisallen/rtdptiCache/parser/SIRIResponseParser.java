package com.lewisallen.rtdptiCache.parser;

import com.lewisallen.rtdptiCache.caches.NaPTANCache;
import com.lewisallen.rtdptiCache.caches.SIRICache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SIRIResponseParser {

    private OffsetDateTime responseTime;
    /**
     * Parses a response from the SIRI service and updates the cache.
     * @param response An XML response received from the SIRI service.
     */
    public void parse(ResponseEntity<String> response){
        JSONObject siriResponse = XML.toJSONObject(response.getBody());
        Map<Object, JSONObject> cache = new HashMap<Object, JSONObject>();

        // Store the time of response for later calculation of time until departure.
        this.responseTime = OffsetDateTime.parse(siriResponse.getJSONObject("Siri")
                .getJSONObject("ServiceDelivery")
                .get("ResponseTimestamp").toString());

        // Check if there are any visits, if not then we can simply wipe the cache.
        if(siriResponse.getJSONObject("Siri")
                .getJSONObject("ServiceDelivery")
                .getJSONObject("StopMonitoringDelivery")
                .has("MonitoredStopVisit"))
        {
            JSONArray monitoredStops;
            List<JSONObject> monitoredStopsList = new ArrayList<JSONObject>();

            // Handle when only one monitored stop is available (it is returned as a JSONObject rather than a JSONArray).
            if(siriResponse.getJSONObject("Siri")
                    .getJSONObject("ServiceDelivery")
                    .getJSONObject("StopMonitoringDelivery")
                    .get("MonitoredStopVisit") instanceof JSONArray)
            {
                // If there are multiple stops, we get all stops as a JSONArray, then add each to a ArrayList.
                monitoredStops = siriResponse.getJSONObject("Siri")
                        .getJSONObject("ServiceDelivery")
                        .getJSONObject("StopMonitoringDelivery")
                        .getJSONArray("MonitoredStopVisit");

                // Convert JSONArray to standard List<JSONObject>
                for(int i = 0; i < monitoredStops.length(); i++){
                    monitoredStopsList.add(monitoredStops.getJSONObject(i));
                }
            }
            else
            {
                // If there is only a single stop, we get the singular stop JSONObject and add it to an ArrayList.
                JSONObject stop = siriResponse.getJSONObject("Siri")
                        .getJSONObject("ServiceDelivery")
                        .getJSONObject("StopMonitoringDelivery")
                        .getJSONObject("MonitoredStopVisit");

                monitoredStopsList.add(stop);
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
            for(Object naptanKey : groupedList.keySet()){

                // Remove irrelevant info from json and add departure. Sort stops by departure time.
                List<JSONObject> trimmedList = groupedList.get(naptanKey)
                        .stream()
                        .map((JSONObject j) -> removeFields(j))
                        .map((JSONObject j) -> addDeparture(j))
                        .sorted(new DepartureComparator())
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
                departureSeconds = responseTime.until(expectedDepartureTime, ChronoUnit.SECONDS);
            }
            catch (Exception ex)
            {
                try {
                    // Failed to parse ExpectedDepartureTime, so try AimedDepartureTime.
                    OffsetDateTime aimedDepartureTime = OffsetDateTime.parse(j.getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").get("AimedDepartureTime").toString());
                    departureSeconds = responseTime.until(aimedDepartureTime, ChronoUnit.SECONDS);
                } catch (Exception exBoth){
                    exBoth.printStackTrace();
                }
            }
        }
        else if(j.getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").has("AimedDepartureTime")) {
            try
            {
                OffsetDateTime aimedDepartureTime = OffsetDateTime.parse(j.getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").get("AimedDepartureTime").toString());
                departureSeconds = responseTime.until(aimedDepartureTime, ChronoUnit.SECONDS);
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
