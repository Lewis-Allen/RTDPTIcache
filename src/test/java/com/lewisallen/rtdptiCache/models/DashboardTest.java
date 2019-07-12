package com.lewisallen.rtdptiCache.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DashboardTest
{
    @Test
    void testDashboard()
    {
        Dashboard dashboard = new Dashboard("Template", "OverrideName", "Data", 2L);

        dashboard.setSwitchId(1L);
        dashboard.setLastUsedDate(LocalDateTime.MAX);

        Assertions.assertAll(
                () -> assertEquals(dashboard.getTemplate(), "Template"),
                () -> assertEquals(dashboard.getData(), "Data"),
                () -> assertEquals(dashboard.getOverrideName(), "OverrideName"),
                () -> assertEquals(dashboard.getSwitchId(), new Long(1)),
                () -> assertTrue(dashboard.toString().contains("Data"))
        );
    }
}