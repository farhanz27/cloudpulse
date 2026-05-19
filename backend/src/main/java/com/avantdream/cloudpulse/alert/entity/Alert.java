package com.avantdream.cloudpulse.alert.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    @Column(name = "alert_type", nullable = false, length = 50)
    private String alertType;

    @Column(nullable = false, length = 2048)
    private String message;

    @Column(nullable = false)
    private boolean notified = false;

    @Column(nullable = false)
    private boolean acknowledged = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Alert() {}

    public Alert(UUID serviceId, String alertType, String message) {
        this.serviceId = serviceId;
        this.alertType = alertType;
        this.message = message;
    }

    public Integer getId() { return id; }
    public UUID getServiceId() { return serviceId; }
    public void setServiceId(UUID serviceId) { this.serviceId = serviceId; }
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isNotified() { return notified; }
    public void setNotified(boolean notified) { this.notified = notified; }
    public boolean isAcknowledged() { return acknowledged; }
    public void setAcknowledged(boolean acknowledged) { this.acknowledged = acknowledged; }
    public Instant getCreatedAt() { return createdAt; }
}
