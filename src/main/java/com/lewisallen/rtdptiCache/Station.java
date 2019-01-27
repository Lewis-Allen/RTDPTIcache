package com.lewisallen.rtdptiCache;

public class Station {

    private String stationName,
                   crsCode;

    public Station(String stationName, String crsCode){
        this.stationName = stationName;
        this.crsCode = crsCode;
    }

    public String getStationName() {
        return stationName;
    }

    public String getCrsCode() {
        return crsCode;
    }
}
