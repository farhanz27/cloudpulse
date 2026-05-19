package com.avantdream.cloudpulse.monitor.dto;

import com.avantdream.cloudpulse.monitor.entity.HealthLog;

import java.time.Instant;
import java.util.UUID;

public record HealthLogResponse(
        Long id,
        UUID serviceId,
        String status,
        Double responseTimeMs,
        Integer statusCode,
        String errorMessage,
        Instant checkedAt
) {
    public static HealthLogResponse from(HealthLog h) {
        return new HealthLogResponse(h.getId(), h.getServiceId(), h.getStatus(),
                h.getResponseTimeMs(), h.getStatusCode(), h.getErrorMessage(), h.getCheckedAt());
    }
}
