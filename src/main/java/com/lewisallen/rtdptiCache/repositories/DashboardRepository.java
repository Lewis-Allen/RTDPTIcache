package com.lewisallen.rtdptiCache.repositories;

import com.lewisallen.rtdptiCache.models.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    Optional<Dashboard> findDashboardByData(String dashboarddata);
}