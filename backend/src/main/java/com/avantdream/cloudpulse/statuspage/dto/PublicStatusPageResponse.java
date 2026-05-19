package com.avantdream.cloudpulse.statuspage.dto;

import java.util.List;
import java.util.UUID;

public record PublicStatusPageResponse(
        UUID id,
        String name,
        String slug,
        String description,
        String overallStatus,
        List<PublicServiceStatus> services
) {}
