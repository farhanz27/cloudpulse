package com.avantdream.cloudpulse.integration.notify;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class TeamsNotifier {

    private static final Logger log = LoggerFactory.getLogger(TeamsNotifier.class);
    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TeamsNotifier(RestClient restClient) {
        this.restClient = restClient;
    }

    public boolean send(String webhookUrl, String message) {
        if (webhookUrl == null || webhookUrl.isBlank()) return false;
        try {
            Map<String, Object> body = Map.of(
                    "type", "message",
                    "attachments", List.of(Map.of(
                            "contentType", "application/vnd.microsoft.card.adaptive",
                            "content", Map.of(
                                    "type", "AdaptiveCard",
                                    "body", List.of(Map.of("type", "TextBlock", "text", message))
                            )
                    ))
            );
            restClient.post()
                    .uri(webhookUrl)
                    .header("Content-Type", "application/json")
                    .body(objectMapper.writeValueAsString(body))
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (Exception ex) {
            log.debug("Teams delivery failed: {}", ex.getMessage());
            return false;
        }
    }
}
