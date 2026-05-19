package com.avantdream.cloudpulse.integration.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avantdream.cloudpulse.integration.dto.*;
import com.avantdream.cloudpulse.integration.entity.Integration;
import com.avantdream.cloudpulse.integration.notify.*;
import com.avantdream.cloudpulse.integration.repository.IntegrationRepository;
import com.avantdream.cloudpulse.shared.exception.ResourceNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class IntegrationService {

    private static final String MASK = "••••••••";

    private final IntegrationRepository integrationRepository;
    private final TelegramNotifier telegramNotifier;
    private final WebhookNotifier webhookNotifier;
    private final SlackNotifier slackNotifier;
    private final TeamsNotifier teamsNotifier;
    private final EmailNotifier emailNotifier;
    private final AlertingDispatcher alertingDispatcher;

    public IntegrationService(IntegrationRepository integrationRepository,
                               TelegramNotifier telegramNotifier,
                               WebhookNotifier webhookNotifier,
                               SlackNotifier slackNotifier,
                               TeamsNotifier teamsNotifier,
                               EmailNotifier emailNotifier,
                               AlertingDispatcher alertingDispatcher) {
        this.integrationRepository = integrationRepository;
        this.telegramNotifier = telegramNotifier;
        this.webhookNotifier = webhookNotifier;
        this.slackNotifier = slackNotifier;
        this.teamsNotifier = teamsNotifier;
        this.emailNotifier = emailNotifier;
        this.alertingDispatcher = alertingDispatcher;
    }

    @Transactional(readOnly = true)
    public List<IntegrationResponse> list() {
        return integrationRepository.findAllByOrderByCreatedAtAsc().stream()
                .map(IntegrationResponse::from)
                .toList();
    }

    public IntegrationResponse create(IntegrationRequest req) {
        Integration intg = new Integration();
        intg.setType(req.type().toUpperCase());
        intg.setName(req.name());
        intg.setConfig(req.config() != null ? req.config() : Map.of());
        intg.setEnabled(req.enabled());
        return IntegrationResponse.from(integrationRepository.save(intg));
    }

    public IntegrationResponse update(int id, IntegrationUpdateRequest req) {
        Integration intg = requireIntegration(id);
        if (req.name() != null) intg.setName(req.name());
        if (req.enabled() != null) intg.setEnabled(req.enabled());
        if (req.config() != null) {
            // Merge config, skipping values that equal the mask sentinel
            Map<String, Object> merged = new HashMap<>(intg.getConfig());
            for (Map.Entry<String, Object> entry : req.config().entrySet()) {
                if (!MASK.equals(entry.getValue())) {
                    merged.put(entry.getKey(), entry.getValue());
                }
            }
            intg.setConfig(merged);
        }
        return IntegrationResponse.from(integrationRepository.save(intg));
    }

    public void delete(int id) {
        Integration intg = requireIntegration(id);
        integrationRepository.delete(intg);
    }

    public void test(int id) {
        Integration intg = requireIntegration(id);
        String message = String.format("✅ *CloudPulse* — connection verified\nIntegration: *%s* is working correctly.", intg.getName());
        boolean ok = alertingDispatcher.dispatch(intg, message, "TEST");
        if (!ok) {
            throw new IllegalStateException("Test notification failed — check your config");
        }
    }

    private Integration requireIntegration(int id) {
        return integrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Integration not found"));
    }
}
