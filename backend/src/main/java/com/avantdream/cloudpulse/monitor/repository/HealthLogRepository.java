package com.avantdream.cloudpulse.monitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.avantdream.cloudpulse.monitor.entity.HealthLog;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HealthLogRepository extends JpaRepository<HealthLog, Long> {

    Optional<HealthLog> findTopByServiceIdOrderByCheckedAtDesc(UUID serviceId);

    @Query("SELECT h.serviceId, MAX(h.checkedAt) FROM HealthLog h WHERE h.serviceId IN :ids GROUP BY h.serviceId")
    List<Object[]> findLastCheckedAtForServices(@Param("ids") List<UUID> ids);

    @Query("SELECT COUNT(h) FROM HealthLog h WHERE h.serviceId = :sid AND h.checkedAt >= :since")
    long countByServiceIdSince(@Param("sid") UUID sid, @Param("since") Instant since);

    @Query("SELECT COUNT(h) FROM HealthLog h WHERE h.serviceId = :sid AND h.checkedAt >= :since AND h.status = 'UP'")
    long countUpByServiceIdSince(@Param("sid") UUID sid, @Param("since") Instant since);

    long countByServiceId(UUID serviceId);

    List<HealthLog> findByServiceIdOrderByCheckedAtDesc(UUID serviceId,
            org.springframework.data.domain.Pageable pageable);
}
