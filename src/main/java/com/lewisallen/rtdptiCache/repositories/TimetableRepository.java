package com.lewisallen.rtdptiCache.repositories;

import com.lewisallen.rtdptiCache.models.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    Optional<Timetable> findTimetableByData(String timetabledata);

    List<Timetable> findAllByOrderByLastUsedDateDesc();
}