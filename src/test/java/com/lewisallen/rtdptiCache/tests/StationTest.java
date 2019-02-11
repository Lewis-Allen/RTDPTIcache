package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.Station;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StationTest {

    @Test
    public void stationTest(){
        Station station = new Station("Moulsecoomb", "MCB");

        String stationName = station.getStationName();
        String crsCode = station.getCrsCode();

        assertEquals(stationName, "Moulsecoomb");
        assertEquals(crsCode, "MCB");
    }
}
