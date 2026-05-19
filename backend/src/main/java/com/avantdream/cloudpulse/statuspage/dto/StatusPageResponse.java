package com.avantdream.cloudpulse.statuspage.dto;

import com.avantdream.cloudpulse.statuspage.entity.StatusPage;

import java.time.Instant;
import java.util.UUID;

public record StatusPageResponse(
        UUID id,
        String name,
        String slug,
        String description,
        int serviceCount,
        Instant createdAt
) {
    public static StatusPageResponse from(StatusPage p) {
        return new StatusPageResponse(p.getId(), p.getName(), p.getSlug(), p.getDescription(),
                p.getEntries().size(), p.getCreatedAt());
    }
}
