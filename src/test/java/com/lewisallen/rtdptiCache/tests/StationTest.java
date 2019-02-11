package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StationTest {

    @Test
    public void stationTest(){
        Station station = new Station("Moulsecoomb", "MCB");

        String stationName = station.getStationName();
        String crsCode = station.getCrsCode();

        Assertions.assertEquals(stationName, "Moulsecoomb");
        Assertions.assertEquals(crsCode, "MCB");
    }
}
