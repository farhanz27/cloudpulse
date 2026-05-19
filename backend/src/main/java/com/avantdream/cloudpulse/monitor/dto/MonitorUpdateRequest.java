package com.avantdream.cloudpulse.monitor.dto;

import java.util.List;

public record MonitorUpdateRequest(
        String name,
        String url,
        Integer checkIntervalSeconds,
        Integer timeoutSeconds,
        Integer expectedStatusCode,
        Boolean isActive,
        Boolean keepAlive,
        Integer latencyThresholdMs,
        String notifyEmail,
        List<Integer> integrationIds
) {}
