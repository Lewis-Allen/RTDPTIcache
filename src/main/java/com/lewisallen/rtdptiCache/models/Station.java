package com.lewisallen.rtdptiCache.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stations")
public class Station
{
    @Column(name = "stationname")
    private String stationName;

    @Id
    @Column(name = "crscode")
    private String crsCode;

    @Column(name = "retrieve")
    private int retrieve;

    public Station()
    {
    }

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
