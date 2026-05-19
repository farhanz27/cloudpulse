package com.avantdream.cloudpulse.integration.notify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.avantdream.cloudpulse.integration.entity.Integration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class AlertingDispatcher {

    private static final Logger log = LoggerFactory.getLogger(AlertingDispatcher.class);

    private final TelegramNotifier telegramNotifier;
    private final WebhookNotifier webhookNotifier;
    private final SlackNotifier slackNotifier;
    private final TeamsNotifier teamsNotifier;
    private final ResendNotifier resendNotifier;

    public AlertingDispatcher(TelegramNotifier telegramNotifier, WebhookNotifier webhookNotifier,
                               SlackNotifier slackNotifier, TeamsNotifier teamsNotifier,
                               ResendNotifier resendNotifier) {
        this.telegramNotifier = telegramNotifier;
        this.webhookNotifier = webhookNotifier;
        this.slackNotifier = slackNotifier;
        this.teamsNotifier = teamsNotifier;
        this.resendNotifier = resendNotifier;
    }

    public boolean dispatch(Integration intg, String message, String alertType) {
        Map<String, Object> cfg = intg.getConfig();
        String type = intg.getType().toUpperCase();

        String eventsFilter = (String) cfg.get("events");
        if (eventsFilter != null && !eventsFilter.isBlank() && !"TEST".equals(alertType)) {
            List<String> allowed = Arrays.stream(eventsFilter.split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).toList();
            if (!allowed.contains(alertType)) return false;
        }

        String customText = (String) cfg.get("custom_text");
        String finalMsg = (customText != null && !customText.isBlank())
                ? message + "\n" + customText : message;

        try {
            return switch (type) {
                case "TELEGRAM" -> telegramNotifier.send(
                        String.valueOf(cfg.getOrDefault("chat_id", "")), finalMsg);
                case "DISCORD", "WEBHOOK" -> webhookNotifier.send(
                        (String) cfg.getOrDefault("url", ""), finalMsg);
                case "SLACK" -> slackNotifier.send(
                        (String) cfg.getOrDefault("url", ""), finalMsg);
                case "TEAMS" -> teamsNotifier.send(
                        (String) cfg.getOrDefault("url", ""), finalMsg);
                case "EMAIL" -> {
                    String to = (String) cfg.getOrDefault("to", "");
                    yield resendNotifier.send(to, "[CloudPulse] Alert", finalMsg);
                }
                default -> false;
            };
        } catch (Exception ex) {
            log.warn("Failed to dispatch integration {}: {}", intg.getId(), ex.getMessage());
            return false;
        }
    }
}
