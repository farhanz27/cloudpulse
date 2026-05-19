package com.avantdream.cloudpulse.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.avantdream.cloudpulse.integration.entity.Integration;

import java.util.List;
import java.util.Optional;

@Repository
public interface IntegrationRepository extends JpaRepository<Integration, Integer> {
    List<Integration> findAllByOrderByCreatedAtAsc();
    Optional<Integration> findByType(String type);
}
