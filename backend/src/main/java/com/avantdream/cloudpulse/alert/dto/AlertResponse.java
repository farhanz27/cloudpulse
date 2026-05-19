package com.avantdream.cloudpulse.alert.dto;

import com.avantdream.cloudpulse.alert.entity.Alert;

import java.time.Instant;
import java.util.UUID;

public record AlertResponse(
        Integer id,
        UUID serviceId,
        String alertType,
        String message,
        boolean notified,
        boolean acknowledged,
        Instant createdAt,
        String serviceName
) {
    public static AlertResponse from(Alert a, String serviceName) {
        return new AlertResponse(a.getId(), a.getServiceId(), a.getAlertType(),
                a.getMessage(), a.isNotified(), a.isAcknowledged(), a.getCreatedAt(), serviceName);
    }
}
