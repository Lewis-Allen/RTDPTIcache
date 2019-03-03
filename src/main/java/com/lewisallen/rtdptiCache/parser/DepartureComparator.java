package com.lewisallen.rtdptiCache.parser;

import com.lewisallen.rtdptiCache.logging.ErrorHandler;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.logging.Level;

public class DepartureComparator implements Comparator<JSONObject>
{

    @Override
    public int compare(JSONObject o1, JSONObject o2)
    {
        try
        {
            return Integer.compare(getDepartureSeconds(o1), getDepartureSeconds(o2));
        }
        catch (Exception e)
        {
            String message = String.format("Error comparing departure seconds %s and %s", o1, o2);
            ErrorHandler.handle(e, Level.WARNING, message);
            return 0;
        }
    }

    private int getDepartureSeconds(JSONObject o)
    {
        return Integer.parseInt(o.getJSONObject("MonitoredVehicleJourney")
                .getJSONObject("MonitoredCall")
                .get("DepartureSeconds").toString());
    }

}
