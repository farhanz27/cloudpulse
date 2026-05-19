package com.avantdream.cloudpulse.statuspage.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record StatusPageRequest(
        @NotBlank String name,
        @NotBlank String slug,
        String description,
        List<UUID> serviceIds
) {
    public StatusPageRequest {
        if (serviceIds == null) serviceIds = List.of();
    }
}
