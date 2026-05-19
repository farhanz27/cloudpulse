package com.avantdream.cloudpulse.monitor.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.avantdream.cloudpulse.integration.entity.Integration;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "services")
public class Monitor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 2048)
    private String url;

    @Column(name = "check_interval_seconds", nullable = false)
    private int checkIntervalSeconds = 60;

    @Column(name = "timeout_seconds", nullable = false)
    private int timeoutSeconds = 10;

    @Column(name = "expected_status_code", nullable = false)
    private int expectedStatusCode = 200;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "keep_alive", nullable = false)
    private boolean keepAlive = false;

    @Column(name = "latency_threshold_ms", nullable = false)
    private int latencyThresholdMs = 5000;

    @Column(name = "notify_email", length = 255)
    private String notifyEmail;

    @Column(name = "ssl_valid_until")
    private Instant sslValidUntil;

    @Column(name = "domain_valid_until")
    private Instant domainValidUntil;

    @Column(name = "muted_until")
    private Instant mutedUntil;

    @Column(name = "consecutive_failures", nullable = false)
    private int consecutiveFailures = 0;

    @Column(name = "region", length = 255)
    private String region;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "monitor_integrations",
        joinColumns = @JoinColumn(name = "monitor_id"),
        inverseJoinColumns = @JoinColumn(name = "integration_id")
    )
    private List<Integration> integrations = new ArrayList<>();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public int getCheckIntervalSeconds() { return checkIntervalSeconds; }
    public void setCheckIntervalSeconds(int v) { this.checkIntervalSeconds = v; }
    public int getTimeoutSeconds() { return timeoutSeconds; }
    public void setTimeoutSeconds(int v) { this.timeoutSeconds = v; }
    public int getExpectedStatusCode() { return expectedStatusCode; }
    public void setExpectedStatusCode(int v) { this.expectedStatusCode = v; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public boolean isKeepAlive() { return keepAlive; }
    public void setKeepAlive(boolean keepAlive) { this.keepAlive = keepAlive; }
    public int getLatencyThresholdMs() { return latencyThresholdMs; }
    public void setLatencyThresholdMs(int v) { this.latencyThresholdMs = v; }
    public String getNotifyEmail() { return notifyEmail; }
    public void setNotifyEmail(String notifyEmail) { this.notifyEmail = notifyEmail; }
    public Instant getSslValidUntil() { return sslValidUntil; }
    public void setSslValidUntil(Instant v) { this.sslValidUntil = v; }
    public Instant getDomainValidUntil() { return domainValidUntil; }
    public void setDomainValidUntil(Instant v) { this.domainValidUntil = v; }
    public Instant getMutedUntil() { return mutedUntil; }
    public void setMutedUntil(Instant v) { this.mutedUntil = v; }
    public int getConsecutiveFailures() { return consecutiveFailures; }
    public void setConsecutiveFailures(int v) { this.consecutiveFailures = v; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public List<Integration> getIntegrations() { return integrations; }
    public void setIntegrations(List<Integration> integrations) { this.integrations = integrations; }

    public boolean isMuted() {
        return mutedUntil != null && mutedUntil.isAfter(Instant.now());
    }

    public List<Integer> getIntegrationIds() {
        return integrations.stream().map(Integration::getId).toList();
    }
}
