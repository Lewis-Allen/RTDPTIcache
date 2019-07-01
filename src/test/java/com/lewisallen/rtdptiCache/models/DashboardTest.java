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
        dashboard.getId();
        dashboard.getCreatedDate();

        dashboard.setSwitchId(1L);
        dashboard.setData("Hello World");
        dashboard.setLastUsedDate(LocalDateTime.MAX);
        dashboard.setOverrideName("OverrideName2.0");
        dashboard.setTemplate("Big new template.");
        System.out.println(dashboard);

        Assertions.assertAll(
                () -> assertEquals(dashboard.getTemplate(), "Big new template."),
                () -> assertEquals(dashboard.getData(), "Hello World"),
                () -> assertEquals(dashboard.getLastUsedDate(), LocalDateTime.MAX),
                () -> assertEquals(dashboard.getOverrideName(), "OverrideName2.0"),
                () -> assertEquals(dashboard.getSwitchId(), new Long(1))
        );
    }
}