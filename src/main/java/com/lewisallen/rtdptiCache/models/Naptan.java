package com.lewisallen.rtdptiCache.models;

public class Naptan
{
    private String systemCodeNumber;
    private String longDescription;
    private String identifier;

    public Naptan(String systemCodeNumber, String longDescription, String identifier)
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
