package com.lewisallen.rtdptiCache.tests;

import org.junit.Test;

import com.lewisallen.rtdptiCache.jobs.ScheduledTasks;

public class ScheduledTaskTest {
    
    @Test
    public void testUpdateNaPTANCache(){
        ScheduledTasks tasks = new ScheduledTasks();
        tasks.updateNaPTANCache();
    }
}
