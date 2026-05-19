package com.avantdream.cloudpulse.integration.notify;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class WebhookNotifier {

    private static final Logger log = LoggerFactory.getLogger(WebhookNotifier.class);
    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WebhookNotifier(RestClient restClient) {
        this.restClient = restClient;
    }

    public boolean send(String webhookUrl, String message) {
        if (webhookUrl == null || webhookUrl.isBlank()) return false;
        try {
            String body = objectMapper.writeValueAsString(Map.of("text", message, "content", message));
            restClient.post()
                    .uri(webhookUrl)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (Exception ex) {
            log.debug("Webhook delivery failed to {}: {}", webhookUrl, ex.getMessage());
            return false;
        }
    }
}
