package com.avantdream.cloudpulse.statuspage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.avantdream.cloudpulse.statuspage.entity.StatusPage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatusPageRepository extends JpaRepository<StatusPage, UUID> {

    @Query("SELECT p FROM StatusPage p LEFT JOIN FETCH p.entries ORDER BY p.name")
    List<StatusPage> findAllWithEntries();

    @Query("SELECT p FROM StatusPage p LEFT JOIN FETCH p.entries WHERE p.slug = :slug")
    Optional<StatusPage> findBySlugWithEntries(String slug);

    @Query("SELECT p FROM StatusPage p LEFT JOIN FETCH p.entries WHERE p.id = :id")
    Optional<StatusPage> findByIdWithEntries(UUID id);

    boolean existsBySlug(String slug);
    boolean existsBySlugAndIdNot(String slug, UUID id);
}
