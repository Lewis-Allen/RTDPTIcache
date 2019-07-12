package com.lewisallen.rtdptiCache.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * model for a station configuration.
 * Used by JPA to retrieve and save records to database.
 */
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

    // Default constructor used by Spring
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
