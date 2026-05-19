package com.avantdream.cloudpulse.telegram.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avantdream.cloudpulse.shared.config.AppProperties;
import com.avantdream.cloudpulse.shared.exception.ResourceNotFoundException;
import com.avantdream.cloudpulse.telegram.entity.TelegramPendingLink;
import com.avantdream.cloudpulse.telegram.repository.TelegramPendingLinkRepository;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Map;

@Service
@Transactional
public class TelegramService {

    private static final int LINK_TTL_MINUTES = 15;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final TelegramPendingLinkRepository linkRepository;
    private final AppProperties props;

    public TelegramService(TelegramPendingLinkRepository linkRepository, AppProperties props) {
        this.linkRepository = linkRepository;
        this.props = props;
    }

    public Map<String, Object> startLink() {
        if (props.getTelegram().getBotToken().isBlank()) {
            throw new IllegalStateException("Telegram bot is not configured on this server");
        }
        byte[] bytes = new byte[18];
        RANDOM.nextBytes(bytes);
        String code = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        Instant expiresAt = Instant.now().plus(LINK_TTL_MINUTES, ChronoUnit.MINUTES);

        TelegramPendingLink link = new TelegramPendingLink();
        link.setCode(code);
        link.setExpiresAt(expiresAt);
        linkRepository.save(link);

        return Map.of(
                "code", code,
                "bot_username", props.getTelegram().getBotUsername(),
                "expires_in_minutes", LINK_TTL_MINUTES
        );
    }

    @Transactional(readOnly = true)
    public Map<String, Object> linkStatus(String code) {
        TelegramPendingLink link = linkRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Link code not found"));

        if (link.getExpiresAt().isBefore(Instant.now())) {
            throw new ResourceNotFoundException("Link code expired");
        }

        return Map.of(
                "linked", link.getIntegrationId() != null,
                "integration_id", link.getIntegrationId() != null ? link.getIntegrationId() : (Object) null
        );
    }
}
