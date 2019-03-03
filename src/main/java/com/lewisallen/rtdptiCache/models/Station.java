package com.lewisallen.rtdptiCache.models;

public class Station
{
    private String stationName,
            crsCode;

    public Station(String stationName, String crsCode)
    {
        this.stationName = stationName;
        this.crsCode = crsCode;
    }

    /**
     * Getter for Station Name.
     *
     * @return Station Name
     */
    public String getStationName()
    {
        return stationName;
    }

    /**
     * Getter for CRS Code.
     *
     * @return CRS Code
     */
    public String getCrsCode()
    {
        return crsCode;
    }
}
