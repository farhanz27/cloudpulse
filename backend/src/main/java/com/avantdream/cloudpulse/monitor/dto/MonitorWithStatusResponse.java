package com.avantdream.cloudpulse.monitor.dto;

import com.avantdream.cloudpulse.monitor.entity.Monitor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MonitorWithStatusResponse(
        UUID id,
        String name,
        String url,
        int checkIntervalSeconds,
        int timeoutSeconds,
        int expectedStatusCode,
        boolean isActive,
        boolean keepAlive,
        int latencyThresholdMs,
        String notifyEmail,
        Instant sslValidUntil,
        Instant domainValidUntil,
        Instant mutedUntil,
        int consecutiveFailures,
        String region,
        List<Integer> integrationIds,
        Instant createdAt,
        Instant updatedAt,
        String currentStatus,
        Double lastResponseTimeMs,
        Double uptimePercent,
        Instant lastCheckedAt
) {
    public static MonitorWithStatusResponse from(Monitor m, String currentStatus,
                                                  Double lastResponseTimeMs,
                                                  Double uptimePercent,
                                                  Instant lastCheckedAt) {
        return new MonitorWithStatusResponse(
                m.getId(), m.getName(), m.getUrl(),
                m.getCheckIntervalSeconds(), m.getTimeoutSeconds(), m.getExpectedStatusCode(),
                m.isActive(), m.isKeepAlive(), m.getLatencyThresholdMs(),
                m.getNotifyEmail(), m.getSslValidUntil(), m.getDomainValidUntil(),
                m.getMutedUntil(), m.getConsecutiveFailures(), m.getRegion(),
                m.getIntegrationIds(), m.getCreatedAt(), m.getUpdatedAt(),
                currentStatus, lastResponseTimeMs, uptimePercent, lastCheckedAt
        );
    }
}
