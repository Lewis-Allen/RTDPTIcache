package com.lewisallen.rtdptiCache.models;

import com.lewisallen.rtdptiCache.models.Bus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BusTest
{

    private Bus bus;

    @BeforeAll
    void setup()
    {
        bus = new Bus("123456789", "England", "opp");
    }

    @Test
    void codeRetrieveTest()
    {
        bus.getSystemCodeNumber();
    }

    @Test
    void identifierRetrieveTest()
    {
        bus.getIdentifier();
    }

    @Test
    void longDescriptionRetrieveTest()
    {
        bus.getLongDescription();
    }

    @Test
    void testDefaultConstructor() { Bus bus = new Bus(); }

}
