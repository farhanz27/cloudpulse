package com.avantdream.cloudpulse.monitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query(value = "SELECT DISTINCT ON (service_id) * FROM health_logs WHERE service_id IN :ids ORDER BY service_id, checked_at DESC", nativeQuery = true)
    List<HealthLog> findLatestPerServiceIds(@Param("ids") List<UUID> ids);

    @Query("SELECT h.serviceId, COUNT(h), SUM(CASE WHEN h.status = 'UP' THEN 1 ELSE 0 END) " +
           "FROM HealthLog h WHERE h.serviceId IN :ids AND h.checkedAt >= :since GROUP BY h.serviceId")
    List<Object[]> findUptimeStatsSince(@Param("ids") List<UUID> ids, @Param("since") Instant since);

    @Query("SELECT h.serviceId, MAX(h.checkedAt) FROM HealthLog h WHERE h.serviceId IN :ids GROUP BY h.serviceId")
    List<Object[]> findLastCheckedAtForServices(@Param("ids") List<UUID> ids);

    @Query("SELECT COUNT(h) FROM HealthLog h WHERE h.serviceId = :sid AND h.checkedAt >= :since")
    long countByServiceIdSince(@Param("sid") UUID sid, @Param("since") Instant since);

    @Query("SELECT COUNT(h) FROM HealthLog h WHERE h.serviceId = :sid AND h.checkedAt >= :since AND h.status = 'UP'")
    long countUpByServiceIdSince(@Param("sid") UUID sid, @Param("since") Instant since);

    @Query(value = """
            SELECT DATE(checked_at AT TIME ZONE 'UTC') AS day,
                   COUNT(*) AS total,
                   COUNT(CASE WHEN status = 'UP' THEN 1 END) AS up_count
            FROM health_logs
            WHERE service_id = :sid AND checked_at >= :since
            GROUP BY DATE(checked_at AT TIME ZONE 'UTC')
            """, nativeQuery = true)
    List<Object[]> findDailyUptime(@Param("sid") UUID sid, @Param("since") Instant since);

    long countByServiceId(UUID serviceId);

    List<HealthLog> findByServiceIdOrderByCheckedAtDesc(UUID serviceId,
            org.springframework.data.domain.Pageable pageable);

    @Modifying
    @Query("DELETE FROM HealthLog h")
    void deleteAllLogs();
}
