package com.lewisallen.rtdptiCache.repositories;

import com.lewisallen.rtdptiCache.models.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusRepository extends JpaRepository<Bus, String> {
    List<Bus> findByRetrieve(int retrieve);
}
