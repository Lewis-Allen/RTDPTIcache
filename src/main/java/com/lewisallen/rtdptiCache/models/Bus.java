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

    // TODO: add a comment to say why we need empty ctor and also in other models
    public Bus()
    {
    }

    public Bus(String systemCodeNumber, String longDescription, String identifier)
    {
        this.systemCodeNumber = systemCodeNumber;
        this.longDescription = longDescription;
        this.identifier = identifier;
    }

    public String getSystemCodeNumber()
    {
        return systemCodeNumber;
    }

    public String getLongDescription()
    {
        return longDescription;
    }

    public String getIdentifier()
    {
        return identifier;
    }
}
