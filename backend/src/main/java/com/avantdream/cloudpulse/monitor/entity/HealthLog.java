package com.avantdream.cloudpulse.monitor.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "health_logs")
public class HealthLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    @Column(nullable = false, length = 10)
    private String status;

    @Column(name = "response_time_ms")
    private Double responseTimeMs;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "error_message", length = 1024)
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "checked_at", nullable = false, updatable = false)
    private Instant checkedAt;

    public HealthLog() {}

    public HealthLog(UUID serviceId, String status, Double responseTimeMs, Integer statusCode, String errorMessage) {
        this.serviceId = serviceId;
        this.status = status;
        this.responseTimeMs = responseTimeMs;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public Long getId() { return id; }
    public UUID getServiceId() { return serviceId; }
    public void setServiceId(UUID serviceId) { this.serviceId = serviceId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(Double v) { this.responseTimeMs = v; }
    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public Instant getCheckedAt() { return checkedAt; }
    public void setCheckedAt(Instant checkedAt) { this.checkedAt = checkedAt; }
}
