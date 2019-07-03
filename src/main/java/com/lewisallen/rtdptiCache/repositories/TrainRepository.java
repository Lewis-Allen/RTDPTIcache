package com.lewisallen.rtdptiCache.repositories;

import com.lewisallen.rtdptiCache.models.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainRepository extends JpaRepository<Station, String>
{
    List<Station> findByRetrieve(int retrieve);
}
