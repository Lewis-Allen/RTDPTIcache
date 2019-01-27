package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.jobs.ScheduledTasks;
import org.junit.Test;

public class ScheduledTaskTest {
    
    @Test
    public void testUpdateNaPTANCache(){
        ScheduledTasks tasks = new ScheduledTasks();
        tasks.updateNaPTANCache();
    }

    @Test
    public void testUpdateTrainStationCache(){
        ScheduledTasks tasks = new ScheduledTasks();
        tasks.updateStationCache();
    }
}
