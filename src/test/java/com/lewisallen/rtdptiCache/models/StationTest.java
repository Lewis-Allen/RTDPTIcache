package com.lewisallen.rtdptiCache.models;

import com.lewisallen.rtdptiCache.models.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StationTest
{

    @Test
    void stationTest()
    {
        Station stationDefaultConstructor = new Station();
        Station station = new Station("Moulsecoomb", "MCB");

        String stationName = station.getStationName();
        String crsCode = station.getCrsCode();

        Assertions.assertEquals(stationName, "Moulsecoomb");
        Assertions.assertEquals(crsCode, "MCB");
    }
}
