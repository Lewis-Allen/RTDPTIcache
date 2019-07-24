package com.lewisallen.rtdptiCache.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Model for a naptan object.
 * Used by JPA to retrieve and save records to database.
 * This object contains a unique identifier for a bus stop.
 * Along with a description (name) and identifier (opposite, adjacent etc)
 */
@Entity
@Table(name = "naptan")
public class Bus {
    @Id
    @Column(name = "systemcodenumber")
    private String systemCodeNumber;

    @Column(name = "longdescription")
    private String longDescription;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "retrieve")
    private int retrieve;

    // Default constructor used by Spring
    public Bus() {
    }

    public Bus(String systemCodeNumber, String longDescription, String identifier) {
        this.systemCodeNumber = systemCodeNumber;
        this.longDescription = longDescription;
        this.identifier = identifier;
    }

    public String getSystemCodeNumber() {
        return systemCodeNumber;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getIdentifier() {
        return identifier;
    }
}
