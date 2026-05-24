package com.avantdream.cloudpulse.monitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.avantdream.cloudpulse.monitor.entity.Monitor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, UUID> {

    List<Monitor> findAllByOrderByNameAsc();

    @Query("SELECT m FROM Monitor m LEFT JOIN FETCH m.integrations WHERE m.active = true")
    List<Monitor> findAllActiveWithIntegrations();

}
