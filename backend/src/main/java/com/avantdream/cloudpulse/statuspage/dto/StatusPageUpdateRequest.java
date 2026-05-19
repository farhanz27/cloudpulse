package com.avantdream.cloudpulse.statuspage.dto;

import java.util.List;
import java.util.UUID;

public record StatusPageUpdateRequest(
        String name,
        String slug,
        String description,
        List<UUID> serviceIds
) {}
