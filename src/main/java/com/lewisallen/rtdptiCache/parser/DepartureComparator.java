package com.lewisallen.rtdptiCache.parser;

import org.json.JSONObject;

import java.util.Comparator;

public class DepartureComparator implements Comparator<JSONObject> {

    @Override
    public int compare(JSONObject o1, JSONObject o2) {
        try {
            return Integer.compare(getDepartureSeconds(o1), getDepartureSeconds(o2));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int getDepartureSeconds(JSONObject o) {
        return Integer.parseInt(o.getJSONObject("MonitoredVehicleJourney")
                .getJSONObject("MonitoredCall")
                .get("DepartureSeconds").toString());
    }

}
