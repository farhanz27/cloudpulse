package com.avantdream.cloudpulse.auth.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_preferences")
public class UserPreferences {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "email_alerts", nullable = false)
    private boolean emailAlerts = true;

    @Column(name = "recovery_notifications", nullable = false)
    private boolean recoveryNotifications = true;

    @Column(name = "default_interval_seconds", nullable = false)
    private int defaultIntervalSeconds = 60;

    @Column(name = "default_timeout_seconds", nullable = false)
    private int defaultTimeoutSeconds = 10;

    @Column(name = "default_latency_threshold_ms", nullable = false)
    private int defaultLatencyThresholdMs = 5000;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public boolean isEmailAlerts() { return emailAlerts; }
    public void setEmailAlerts(boolean emailAlerts) { this.emailAlerts = emailAlerts; }
    public boolean isRecoveryNotifications() { return recoveryNotifications; }
    public void setRecoveryNotifications(boolean recoveryNotifications) { this.recoveryNotifications = recoveryNotifications; }
    public int getDefaultIntervalSeconds() { return defaultIntervalSeconds; }
    public void setDefaultIntervalSeconds(int defaultIntervalSeconds) { this.defaultIntervalSeconds = defaultIntervalSeconds; }
    public int getDefaultTimeoutSeconds() { return defaultTimeoutSeconds; }
    public void setDefaultTimeoutSeconds(int defaultTimeoutSeconds) { this.defaultTimeoutSeconds = defaultTimeoutSeconds; }
    public int getDefaultLatencyThresholdMs() { return defaultLatencyThresholdMs; }
    public void setDefaultLatencyThresholdMs(int defaultLatencyThresholdMs) { this.defaultLatencyThresholdMs = defaultLatencyThresholdMs; }
}
