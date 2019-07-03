package com.lewisallen.rtdptiCache.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "naptan")
public class Bus
{
    @Id
    @Column(name = "systemcodenumber")
    private String systemCodeNumber;

    @Column(name = "longdescription")
    private String longDescription;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "retrieve")
    private int retrieve;

    public Bus()
    {
    }

    public Bus(String systemCodeNumber, String longDescription, String identifier)
    {
        this.systemCodeNumber = systemCodeNumber;
        this.longDescription = longDescription;
        this.identifier = identifier;
    }

    /**
     * Getter for System Code Number.
     *
     * @return System Code Number
     */
    public String getSystemCodeNumber()
    {
        return systemCodeNumber;
    }

    /**
     * Getter for Long Description
     *
     * @return Long Description
     */
    public String getLongDescription()
    {
        return longDescription;
    }

    /**
     * Getter for Identifier
     *
     * @return Identifier
     */
    public String getIdentifier()
    {
        return identifier;
    }
}
