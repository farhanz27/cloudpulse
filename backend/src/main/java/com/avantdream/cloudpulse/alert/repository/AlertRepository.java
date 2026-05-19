package com.avantdream.cloudpulse.alert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.avantdream.cloudpulse.alert.entity.Alert;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {

    @Query("SELECT a FROM Alert a WHERE " +
           "(:serviceId IS NULL OR a.serviceId = :serviceId) AND " +
           "(:acknowledged IS NULL OR a.acknowledged = :acknowledged) " +
           "ORDER BY a.createdAt DESC")
    List<Alert> findFiltered(@Param("serviceId") UUID serviceId,
                              @Param("acknowledged") Boolean acknowledged,
                              org.springframework.data.domain.Pageable pageable);

    @Modifying
    @Query("UPDATE Alert a SET a.acknowledged = true WHERE a.acknowledged = false " +
           "AND (:serviceId IS NULL OR a.serviceId = :serviceId) " +
           "AND (:alertType IS NULL OR a.alertType = :alertType)")
    int acknowledgeAll(@Param("serviceId") UUID serviceId, @Param("alertType") String alertType);

    @Query("SELECT a FROM Alert a WHERE a.serviceId = :sid AND a.alertType IN ('DOWNTIME', 'RECOVERY') ORDER BY a.serviceId, a.createdAt ASC")
    List<Alert> findDowntimeAndRecoveryByService(@Param("sid") UUID sid);

    @Query("SELECT a FROM Alert a WHERE a.alertType IN ('DOWNTIME', 'RECOVERY') ORDER BY a.serviceId, a.createdAt ASC")
    List<Alert> findAllDowntimeAndRecovery();

    @Query("SELECT a FROM Alert a WHERE " +
           "a.alertType IN ('DOWNTIME', 'RECOVERY') AND " +
           "(:serviceId IS NULL OR a.serviceId = :serviceId) " +
           "ORDER BY a.serviceId, a.createdAt ASC")
    List<Alert> findDowntimeAndRecoveryFiltered(@Param("serviceId") UUID serviceId);
}
