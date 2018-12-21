package com.lewisallen.rtdptiCache.parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.util.Comparator;

public class JSONSorter implements Comparator<JSONObject> {

	@Override
	public int compare(JSONObject o1, JSONObject o2) {
		try 
		{
			int retVal = OffsetDateTime.parse(o1.getJSONObject("MonitoredVehicleJourney")
				  								.getJSONObject("MonitoredCall")
				  								.get("ExpectedArrivalTime")
				  								.toString()
				  								.replace("Z", "")
										 	)
								   	   .compareTo(OffsetDateTime.parse(o2.getJSONObject("MonitoredVehicleJourney")
				 				  		    						 	 .getJSONObject("MonitoredCall")
				 				  		    						 	 .get("ExpectedArrivalTime")
				 				  		    						 	 .toString()
				 				  		    						 	 .replace("Z", "")
				 						  						  	  )
								   			   	 );
		// Debug Info
		/*System.out.println(String.format("Comparing %s to %s - Result: %s", o1.getJSONObject("MonitoredVehicleJourney")
				  .getJSONObject("MonitoredCall")
				  .get("ExpectedArrivalTime")
				  .toString()
				  .replace("Z", ""), o2.getJSONObject("MonitoredVehicleJourney")
				  .getJSONObject("MonitoredCall")
				  .get("ExpectedArrivalTime")
				  .toString()
				  .replace("Z", ""),
				  retVal == 1 ? "after" : retVal == -1 ? "before" : "same"));
		*/

			return retVal;
		} 
		catch (JSONException e)
		{
			return 0;
		}
	}

}
