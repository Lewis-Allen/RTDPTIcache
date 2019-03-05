package com.lewisallen.rtdptiCache.parser;

import com.lewisallen.rtdptiCache.caches.NaPTANCache;
import com.lewisallen.rtdptiCache.caches.SIRICache;
import com.lewisallen.rtdptiCache.logging.ErrorHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SIRIResponseParser
{

    private OffsetDateTime responseTime;

    /**
     * Parses a response from the SIRI service and updates the cache.
     *
     * @param response An XML response received from the SIRI service.
     */
    public void parse(ResponseEntity<String> response)
    {
        JSONObject siriResponse = XML.toJSONObject(response.getBody());
        Map<Object, JSONObject> cache = new HashMap<Object, JSONObject>();

        // Store the time of response for later calculation of time until departure.
        this.responseTime = OffsetDateTime.parse(responsePathTransverser(siriResponse, "ServiceDelivery")
                .get("ResponseTimestamp").toString());

        // Check if there are any visits, if not then we can simply wipe the cache.
        if (responsePathTransverser(siriResponse, "StopMonitoringDelivery").has("MonitoredStopVisit"))
        {
            JSONArray monitoredStops;
            List<JSONObject> monitoredStopsList = new ArrayList<>();

            // Handle when only one monitored stop is available (it is returned as a JSONObject rather than a JSONArray).
            if (responsePathTransverser(siriResponse, "StopMonitoringDelivery")
                    .get("MonitoredStopVisit") instanceof JSONArray)
            {
                // If there are multiple stops, we get all stops as a JSONArray, then add each to a ArrayList.
                monitoredStops = responsePathTransverser(siriResponse, "StopMonitoringDelivery")
                        .getJSONArray("MonitoredStopVisit");

                // Convert JSONArray to standard List<JSONObject>
                for (int i = 0; i < monitoredStops.length(); i++)
                {
                    monitoredStopsList.add(monitoredStops.getJSONObject(i));
                }
            }
            else
            {
                // If there is only a single stop, we get the singular stop JSONObject and add it to an ArrayList.
                JSONObject stop = responsePathTransverser(siriResponse, "MonitoredStopVisit");

                monitoredStopsList.add(stop);
            }

            // Go through the JSON list and group items by into lists by their MonitoringRef
            Map<String, List<JSONObject>> groupedList = monitoredStopsList.parallelStream()
                    .collect(Collectors.groupingBy(this::getMonitoringRef));

            // Create a cache to hold list of stops alongside other info.
            for (String naptanKey : groupedList.keySet())
            {

                // Remove irrelevant info from json and add departure. Sort stops by departure time.
                List<JSONObject> trimmedList = groupedList.get(naptanKey)
                        .stream()
                        .map(this::removeFields)
                        .map(this::addDeparture)
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

    /**
     * Removes unnessecary/unused specified fields from the provided JSONObject.
     *
     * @param j JSONObject to remove fields from.
     * @return Processed JSONObject.
     */
    private JSONObject removeFields(JSONObject j)
    {
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

    /**
     * Adds a seconds until departure field to the provided JSONObject
     *
     * @param j Stop visit JSONObject
     * @return Stop visit JSONObject with departure time key.
     */
    private JSONObject addDeparture(JSONObject j)
    {
        // Get the object that holds the time values.
        JSONObject parent = j.getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall");

        long departureSeconds = getDepartureSeconds(parent);

        j.getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").put("DepartureSeconds", departureSeconds);
        return j;
    }

    /**
     * Calculates the seconds until departure for a provided MonitoredCall JSONObject.
     * Attempts to get the departure seconds in the following order:
     * ExpectedDepartureTime > AimedDepartureTime > ExpectedArrivalTime > AimedArrivalTime
     *
     * @param json Monitored call JSONObject
     * @return Seconds until journey departure.
     */
    private long getDepartureSeconds(JSONObject json)
    {
        long departureSeconds = 0;

        // Check if stop has expected departure time
        try
        {
            departureSeconds = parseTime(json, "ExpectedDepartureTime");
        }
        catch (Exception e1)
        {
            try
            {
                departureSeconds = parseTime(json, "AimedDepartureTime");
            }
            catch (Exception e2)
            {
                try
                {
                    departureSeconds = parseTime(json, "ExpectedArrivalTime");
                }
                catch (Exception e3)
                {
                    try
                    {
                        departureSeconds = parseTime(json, "AimedArrivalTime");
                    }
                    catch (Exception e4)
                    {
                        String message = "Failed to parse any departure time in JSON: " + json.toString();
                        ErrorHandler.handle(e4, Level.SEVERE, message);
                    }
                }
            }
        }

        return departureSeconds;
    }

    /**
     * Given a Monitored Call object and a key, parses the time from the given key.
     *
     * @param json Monitored Call JSON Object
     * @param key  key to parse
     * @return Time in seconds until departure.
     */
    private long parseTime(JSONObject json, String key)
    {
        OffsetDateTime expectedDepartureTime = OffsetDateTime.parse(json.get(key).toString());
        return responseTime.until(expectedDepartureTime, ChronoUnit.SECONDS);
    }

    /**
     * Helper method to aid in transversing a SIRI response.
     *
     * @param siriResponse the full SIRI response
     * @param key          the key to look for in the response. If no key is supplied, Service Delivery will be returned.
     * @return JSONObject for key.
     */
    private JSONObject responsePathTransverser(JSONObject siriResponse, String key)
    {
        // We never need anything above the Siri and ServiceDelivery nodes.
        JSONObject result = siriResponse.getJSONObject("Siri")
                .getJSONObject("ServiceDelivery");

        /*
        result = key.equals("StopMonitoringDelivery") ? result.getJSONObject(key)
                : result.getJSONObject("StopMonitoringDelivery").getJSONObject(key);
                */
        if(key.equals("StopMonitoringDelivery"))
        {
            result = result.getJSONObject(key);
        }
        else if (key.equals("MonitoredStopVisit"))
        {
            result = result.getJSONObject("StopMonitoringDelivery")
                    .getJSONObject("MonitoredStopVisit");
        }
/*
        switch (key)
        {
            case "StopMonitoringDelivery":
                result = result.getJSONObject("StopMonitoringDelivery");
                break;

            case "MonitoredStopVisit":
                result = result.getJSONObject("StopMonitoringDelivery")
                        .getJSONObject("MonitoredStopVisit");
                break;
        }*/

        return result;
    }

    /**
     * Returns the monitoring reference key for the given JSON object.
     *
     * @param json JSON to parse
     * @return value of MontoringRef key.
     */
    private String getMonitoringRef(JSONObject json)
    {
        return json.get("MonitoringRef").toString();
    }
}
