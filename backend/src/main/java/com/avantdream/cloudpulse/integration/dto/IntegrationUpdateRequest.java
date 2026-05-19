package com.avantdream.cloudpulse.integration.dto;

import java.util.Map;

public record IntegrationUpdateRequest(
        String name,
        Boolean enabled,
        Map<String, Object> config
) {}
