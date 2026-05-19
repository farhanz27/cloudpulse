package com.avantdream.cloudpulse.integration.dto;

import com.avantdream.cloudpulse.integration.entity.Integration;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public record IntegrationResponse(
        Integer id,
        String type,
        String name,
        Map<String, Object> config,
        boolean enabled,
        Instant createdAt
) {
    private static final Set<String> SECRET_KEYS = Set.of(
            "token", "bot_token", "api_key", "password", "secret", "webhook_url"
    );
    private static final String MASK = "••••••••";

    public static IntegrationResponse from(Integration intg) {
        return new IntegrationResponse(intg.getId(), intg.getType(), intg.getName(),
                maskConfig(intg.getConfig()), intg.isEnabled(), intg.getCreatedAt());
    }

    private static Map<String, Object> maskConfig(Map<String, Object> config) {
        Map<String, Object> masked = new HashMap<>();
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            String key = entry.getKey().toLowerCase();
            // Mask secret-looking keys but not chat_id, url (needed for display)
            boolean isSecret = SECRET_KEYS.stream().anyMatch(key::contains);
            masked.put(entry.getKey(), isSecret ? MASK : entry.getValue());
        }
        return masked;
    }
}
