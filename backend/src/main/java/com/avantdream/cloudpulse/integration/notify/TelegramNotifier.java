package com.avantdream.cloudpulse.integration.notify;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.avantdream.cloudpulse.shared.config.AppProperties;

import java.util.Map;

@Component
public class TelegramNotifier {

    private static final Logger log = LoggerFactory.getLogger(TelegramNotifier.class);
    private final RestClient restClient;
    private final AppProperties props;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TelegramNotifier(RestClient restClient, AppProperties props) {
        this.restClient = restClient;
        this.props = props;
    }

    public boolean send(String chatId, String message) {
        if (chatId == null || chatId.isBlank()) return false;
        String token = props.getTelegram().getBotToken();
        if (token == null || token.isBlank()) return false;

        try {
            String body = objectMapper.writeValueAsString(Map.of(
                    "chat_id", chatId,
                    "text", message,
                    "parse_mode", "Markdown"
            ));
            restClient.post()
                    .uri("https://api.telegram.org/bot" + token + "/sendMessage")
                    .header("Content-Type", "application/json")
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (Exception ex) {
            log.debug("Telegram delivery failed to chat {}: {}", chatId, ex.getMessage());
            return false;
        }
    }
}
