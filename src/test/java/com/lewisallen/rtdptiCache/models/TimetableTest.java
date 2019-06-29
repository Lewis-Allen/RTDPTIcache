package com.lewisallen.rtdptiCache.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimetableTest
{
    @Test
    void testTimetable()
    {
        Timetable timetable = new Timetable("Hello World", "1,2,3");
        timetable.getId();
        timetable.getCreatedDate();

        timetable.setName("Goodbye World");
        timetable.setData("2,3,4");
        timetable.setLastUsedDate(LocalDateTime.MAX);

        Assertions.assertAll(
                () -> assertEquals(timetable.getName(), "Goodbye World"),
                () -> assertEquals(timetable.getData(), "2,3,4"),
                () -> assertEquals(timetable.getLastUsedDate(),LocalDateTime.MAX),
                () -> assertTrue(timetable.toString().contains("Goodbye World"))
        );
    }
}