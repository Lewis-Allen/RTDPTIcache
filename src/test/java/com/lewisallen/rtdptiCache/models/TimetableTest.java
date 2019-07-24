package com.lewisallen.rtdptiCache.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimetableTest {
    @Test
    void testTimetable() {
        Timetable timetable = new Timetable("Hello World", "1,2,3");

        Assertions.assertAll(
                () -> assertEquals(timetable.getName(), "Hello World"),
                () -> assertEquals(timetable.getData(), "1,2,3"),
                () -> assertTrue(timetable.toString().contains("Hello World")),
                () -> assertTrue(timetable.getCreatedDate().isBefore(LocalDateTime.now())),
                () -> assertTrue(timetable.getLastUsedDate().isBefore(LocalDateTime.now()))
        );
    }
}