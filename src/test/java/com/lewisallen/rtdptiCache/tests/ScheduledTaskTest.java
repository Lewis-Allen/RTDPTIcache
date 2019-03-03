package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.AppConfig;
import com.lewisallen.rtdptiCache.jobs.ScheduledTasks;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScheduledTaskTest
{

    @BeforeAll
    void setup()
    {
        new AppConfig();
    }

    @Test
    void testUpdateNaPTANCache()
    {
        ScheduledTasks tasks = new ScheduledTasks();
        tasks.updateNaPTANCache();
    }

    @Test
    public void testUpdateTrainStationCache()
    {
        ScheduledTasks tasks = new ScheduledTasks();
        tasks.updateStationCache();
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    void testUpdateSIRICache()
    {
        ScheduledTasks tasks = new ScheduledTasks();
        tasks.updateSIRICache();
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "CI", matches = "true")
    void ciTestUpdateSIRICache()
    {
        AppConfig.siriUri = System.getenv("SIRI_URI");
        testUpdateSIRICache();
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    void testUpdateStationDeparturesCache()
    {
        ScheduledTasks tasks = new ScheduledTasks();
        tasks.updateTrainsDepartureCache();
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "CI", matches = "true")
    void ciTestUpdateStationDeparturesCache()
    {
        AppConfig.ldbToken = System.getenv("LDB_TOKEN");
        testUpdateStationDeparturesCache();
    }

    // All at once.
    @Test
    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    void testUpdateAllCaches()
    {
        ScheduledTasks tasks = new ScheduledTasks();
        tasks.updateCaches();
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "CI", matches = "true")
    void ciTestUpdateAllCaches()
    {
        AppConfig.siriUri = System.getenv("SIRI_URI");
        AppConfig.ldbToken = System.getenv("LDB_TOKEN");
        testUpdateAllCaches();
    }
}
