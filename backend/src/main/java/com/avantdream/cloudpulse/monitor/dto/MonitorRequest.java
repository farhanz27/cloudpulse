package com.avantdream.cloudpulse.monitor.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MonitorRequest(
        @NotBlank String name,
        @NotBlank String url,
        @Min(5) int checkIntervalSeconds,
        @Min(1) int timeoutSeconds,
        int expectedStatusCode,
        boolean active,
        boolean keepAlive,
        @Min(100) int latencyThresholdMs,
        String notifyEmail,
        List<Integer> integrationIds
) {
    public MonitorRequest {
        if (checkIntervalSeconds == 0) checkIntervalSeconds = 60;
        if (timeoutSeconds == 0) timeoutSeconds = 10;
        if (expectedStatusCode == 0) expectedStatusCode = 200;
        if (latencyThresholdMs == 0) latencyThresholdMs = 5000;
        if (integrationIds == null) integrationIds = List.of();
    }
}
