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

    public String getStationName()
    {
        return stationName;
    }

    public String getCrsCode()
    {
        return crsCode;
    }
}
