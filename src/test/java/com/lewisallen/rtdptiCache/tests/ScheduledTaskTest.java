package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.jobs.ScheduledTasks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class ScheduledTaskTest
{
    @Autowired
    private ScheduledTasks tasks;

    @Test
    void testUpdateNaPTANCache()
    {
        tasks.updateBusCodesCache();
    }

    @Test
    void testUpdateTrainStationCache()
    {
        tasks.updateStationCache();
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    void testUpdateSIRICache()
    {
        tasks.updateSIRICache();
    }

    @Test
    void testUpdateStationDeparturesCache()
    {
        tasks.updateTrainsDepartureCache();
    }

    // All at once.
    @Test
    void testUpdateAllCaches()
    {
        tasks.updateCaches();
    }

    @Test
    void testUpdateBuses()
    {
        tasks.updateBusCaches();
    }

    @Test
    void testUpdateTrains()
    {
        tasks.updateTrainCaches();
    }
}
