package com.avantdream.cloudpulse.alert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.avantdream.cloudpulse.alert.entity.Alert;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {

    @Query(value = "SELECT * FROM alerts WHERE " +
           "(CAST(:serviceId AS uuid) IS NULL OR service_id = CAST(:serviceId AS uuid)) AND " +
           "(CAST(:acknowledged AS boolean) IS NULL OR acknowledged = CAST(:acknowledged AS boolean)) AND " +
           "(CAST(:since AS timestamptz) IS NULL OR created_at >= CAST(:since AS timestamptz)) " +
           "ORDER BY created_at DESC LIMIT :limit OFFSET :offset",
           nativeQuery = true)
    List<Alert> findFiltered(@Param("serviceId") UUID serviceId,
                              @Param("acknowledged") Boolean acknowledged,
                              @Param("since") java.time.Instant since,
                              @Param("limit") int limit,
                              @Param("offset") int offset);

    @Modifying
    @Query("UPDATE Alert a SET a.acknowledged = true WHERE a.acknowledged = false " +
           "AND (:serviceId IS NULL OR a.serviceId = :serviceId) " +
           "AND (:alertType IS NULL OR a.alertType = :alertType)")
    int acknowledgeAll(@Param("serviceId") UUID serviceId, @Param("alertType") String alertType);

    @Query("SELECT a FROM Alert a WHERE a.serviceId = :sid AND a.alertType IN ('DOWNTIME', 'RECOVERY') ORDER BY a.serviceId, a.createdAt ASC")
    List<Alert> findDowntimeAndRecoveryByService(@Param("sid") UUID sid);

    @Query("SELECT a FROM Alert a WHERE a.alertType IN ('DOWNTIME', 'RECOVERY') AND a.createdAt >= :since ORDER BY a.serviceId, a.createdAt ASC")
    List<Alert> findAllDowntimeAndRecovery(@Param("since") java.time.Instant since);

    @Query(value = "SELECT * FROM alerts WHERE " +
           "alert_type IN ('DOWNTIME', 'RECOVERY') AND " +
           "(CAST(:serviceId AS uuid) IS NULL OR service_id = CAST(:serviceId AS uuid)) AND " +
           "(CAST(:since AS timestamptz) IS NULL OR created_at >= CAST(:since AS timestamptz)) " +
           "ORDER BY service_id, created_at ASC", nativeQuery = true)
    List<Alert> findDowntimeAndRecoveryFiltered(@Param("serviceId") UUID serviceId,
                                                @Param("since") Instant since);
}
