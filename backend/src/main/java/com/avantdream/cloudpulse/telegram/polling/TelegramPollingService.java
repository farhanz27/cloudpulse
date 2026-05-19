package com.avantdream.cloudpulse.telegram.polling;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.avantdream.cloudpulse.integration.entity.Integration;
import com.avantdream.cloudpulse.integration.repository.IntegrationRepository;
import com.avantdream.cloudpulse.shared.config.AppProperties;
import com.avantdream.cloudpulse.telegram.entity.TelegramPendingLink;
import com.avantdream.cloudpulse.telegram.repository.TelegramPendingLinkRepository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class TelegramPollingService implements SmartLifecycle {

    private static final Logger log = LoggerFactory.getLogger(TelegramPollingService.class);

    private final AppProperties props;
    private final TelegramPendingLinkRepository linkRepository;
    private final IntegrationRepository integrationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private volatile Thread pollingThread;
    private volatile boolean running = false;

    public TelegramPollingService(AppProperties props,
                                   TelegramPendingLinkRepository linkRepository,
                                   IntegrationRepository integrationRepository) {
        this.props = props;
        this.linkRepository = linkRepository;
        this.integrationRepository = integrationRepository;
    }

    @Override
    public void start() {
        if (props.getTelegram().getBotToken().isBlank()) {
            log.info("TELEGRAM_BOT_TOKEN not set — polling disabled");
            return;
        }
        running = true;
        pollingThread = Thread.ofVirtual().name("telegram-polling").start(this::pollLoop);
        log.info("Telegram polling started");
    }

    @Override
    public void stop() {
        running = false;
        if (pollingThread != null) {
            pollingThread.interrupt();
            pollingThread = null;
        }
        log.info("Telegram polling stopped");
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private void pollLoop() {
        String token = props.getTelegram().getBotToken();
        String baseUrl = "https://api.telegram.org/bot" + token + "/getUpdates";
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        long offset = 0;

        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                String url = baseUrl + "?offset=" + offset + "&timeout=30&allowed_updates=[\"message\"]";
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(Duration.ofSeconds(35))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonNode data = objectMapper.readTree(response.body());

                for (JsonNode update : data.path("result")) {
                    offset = update.path("update_id").asLong() + 1;
                    handleUpdate(update);
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception ex) {
                if (running) {
                    log.warn("Telegram polling error — retrying in 5s: {}", ex.getMessage());
                    try { Thread.sleep(5000); } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
    }

    @Transactional
    protected void handleUpdate(JsonNode update) {
        JsonNode message = update.path("message");
        if (message.isMissingNode()) message = update.path("channel_post");
        if (message.isMissingNode()) return;

        String text = message.path("text").asText("").strip();
        long chatId = message.path("chat").path("id").asLong();

        if (!text.startsWith("/start")) return;
        String[] parts = text.split("\\s+", 2);
        if (parts.length < 2) return;
        String code = parts[1].strip();

        Optional<TelegramPendingLink> linkOpt = linkRepository.findByCode(code);
        if (linkOpt.isEmpty()) return;

        TelegramPendingLink link = linkOpt.get();
        if (link.getExpiresAt().isBefore(Instant.now())) return;
        if (link.getIntegrationId() != null) return;

        Optional<Integration> existing = integrationRepository.findByType("TELEGRAM");
        Integration intg;
        if (existing.isPresent()) {
            intg = existing.get();
            Map<String, Object> newConfig = new HashMap<>(intg.getConfig());
            newConfig.put("chat_id", chatId);
            intg.setConfig(newConfig);
            intg.setEnabled(true);
        } else {
            intg = new Integration();
            intg.setType("TELEGRAM");
            intg.setName("Telegram");
            intg.setConfig(Map.of("chat_id", chatId));
            intg.setEnabled(true);
        }
        intg = integrationRepository.save(intg);

        link.setChatId(chatId);
        link.setIntegrationId(intg.getId());
        linkRepository.save(link);

        log.info("Telegram linked: chatId={} integrationId={}", chatId, intg.getId());
    }
}
