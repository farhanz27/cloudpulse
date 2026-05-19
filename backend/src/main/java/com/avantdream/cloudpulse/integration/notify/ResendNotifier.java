package com.avantdream.cloudpulse.integration.notify;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.avantdream.cloudpulse.shared.config.AppProperties;

import java.util.Map;

@Component
public class ResendNotifier {

    private static final Logger log = LoggerFactory.getLogger(ResendNotifier.class);
    private final RestClient restClient;
    private final AppProperties props;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResendNotifier(RestClient restClient, AppProperties props) {
        this.restClient = restClient;
        this.props = props;
    }

    public boolean send(String to, String subject, String body) {
        String apiKey = props.getResend().getApiKey();
        if (apiKey == null || apiKey.isBlank()) return false;
        if (to == null || to.isBlank()) return false;
        try {
            String payload = objectMapper.writeValueAsString(Map.of(
                    "from", props.getAlert().getEmailFrom(),
                    "to", new String[]{to},
                    "subject", subject,
                    "text", body
            ));
            restClient.post()
                    .uri("https://api.resend.com/emails")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (Exception ex) {
            log.debug("Resend delivery failed to {}: {}", to, ex.getMessage());
            return false;
        }
    }
}
