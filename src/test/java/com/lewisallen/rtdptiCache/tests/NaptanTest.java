package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.models.Naptan;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NaptanTest
{

    private Naptan naptan;

    @BeforeAll
    void setup()
    {
        naptan = new Naptan("123456789", "England", "opp");
    }

    @Test
    void codeRetrieveTest()
    {
        naptan.getSystemCodeNumber();
    }

    @Test
    void identifierRetrieveTest()
    {
        naptan.getIdentifier();
    }

    @Test
    void longDescriptionRetrieveTest()
    {
        naptan.getLongDescription();
    }

}
