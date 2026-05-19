package com.avantdream.cloudpulse.integration.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record IntegrationRequest(
        @NotBlank String type,
        @NotBlank String name,
        Map<String, Object> config,
        boolean enabled
) {
    public IntegrationRequest {
        if (config == null) config = Map.of();
    }
}
