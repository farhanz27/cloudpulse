package com.avantdream.cloudpulse.alert.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.avantdream.cloudpulse.alert.entity.Alert;
import com.avantdream.cloudpulse.alert.repository.AlertRepository;
import com.avantdream.cloudpulse.integration.entity.Integration;
import com.avantdream.cloudpulse.integration.notify.AlertingDispatcher;
import com.avantdream.cloudpulse.integration.notify.ResendNotifier;
import com.avantdream.cloudpulse.integration.notify.EmailNotifier;
import com.avantdream.cloudpulse.integration.notify.WebhookNotifier;
import com.avantdream.cloudpulse.monitor.entity.HealthLog;
import com.avantdream.cloudpulse.monitor.entity.Monitor;
import com.avantdream.cloudpulse.shared.config.AppProperties;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class AlertingService {

    private static final Logger log = LoggerFactory.getLogger(AlertingService.class);

    private final AlertRepository alertRepository;
    private final AppProperties props;
    private final AlertingDispatcher dispatcher;
    private final WebhookNotifier webhookNotifier;
    private final EmailNotifier emailNotifier;
    private final ResendNotifier resendNotifier;

    public AlertingService(AlertRepository alertRepository, AppProperties props,
                           AlertingDispatcher dispatcher, WebhookNotifier webhookNotifier,
                           EmailNotifier emailNotifier, ResendNotifier resendNotifier) {
        this.alertRepository = alertRepository;
        this.props = props;
        this.dispatcher = dispatcher;
        this.webhookNotifier = webhookNotifier;
        this.emailNotifier = emailNotifier;
        this.resendNotifier = resendNotifier;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void evaluate(Monitor monitor, HealthLog healthLog) {
        int threshold = props.getAlerting().getConsecutiveFailuresThreshold();

        if ("DOWN".equals(healthLog.getStatus())) {
            monitor.setConsecutiveFailures(monitor.getConsecutiveFailures() + 1);
            if (monitor.getConsecutiveFailures() == threshold) {
                String msg = String.format(
                        "🔴 *%s* is DOWN\nURL: `%s`\nFailed %d consecutive checks\nError: %s",
                        monitor.getName(), monitor.getUrl(), threshold,
                        healthLog.getErrorMessage() != null ? healthLog.getErrorMessage() : "No response");
                Alert alert = alertRepository.save(new Alert(monitor.getId(), "DOWNTIME", msg));
                notify(alert, monitor, "DOWNTIME");
            }
        } else {
            if (monitor.getConsecutiveFailures() >= threshold) {
                double rt = healthLog.getResponseTimeMs() != null ? healthLog.getResponseTimeMs() : 0.0;
                String msg = String.format(
                        "🟢 *%s* has RECOVERED\nURL: `%s`\nResponse time: %.0fms",
                        monitor.getName(), monitor.getUrl(), rt);
                Alert alert = alertRepository.save(new Alert(monitor.getId(), "RECOVERY", msg));
                notify(alert, monitor, "RECOVERY");
            }
            monitor.setConsecutiveFailures(0);
        }

        if (!"DOWN".equals(healthLog.getStatus()) && healthLog.getResponseTimeMs() != null
                && healthLog.getResponseTimeMs() > monitor.getLatencyThresholdMs()) {
            String msg = String.format(
                    "🟡 *%s* has HIGH LATENCY\nURL: `%s`\nResponse time: %.0fms",
                    monitor.getName(), monitor.getUrl(), healthLog.getResponseTimeMs());
            Alert alert = alertRepository.save(new Alert(monitor.getId(), "HIGH_LATENCY", msg));
            notify(alert, monitor, "HIGH_LATENCY");
        }
    }

    public int sendTestNotification(Monitor monitor) {
        String now = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm 'UTC'")
                .withZone(java.time.ZoneOffset.UTC).format(Instant.now());
        String message = String.format(
                "🔔 *Test Notification*\nMonitor: *%s*\nURL: `%s`\nSent: %s",
                monitor.getName(), monitor.getUrl(), now);
        String subject = "[CloudPulse] Test Notification: " + monitor.getName();
        String cleanBody = message.replace("*", "").replace("`", "");

        int sent = 0;
        if (webhookNotifier.send(props.getWebhook().getUrl(), message)) sent++;

        for (String to : emailRecipients(monitor)) {
            if (sendEmail(to, subject, cleanBody)) sent++;
        }
        for (Integration intg : monitor.getIntegrations()) {
            if (intg.isEnabled() && dispatcher.dispatch(intg, message, "TEST")) sent++;
        }
        return sent;
    }

    private void notify(Alert alert, Monitor monitor, String alertType) {
        if (monitor.isMuted()) {
            alert.setNotified(false);
            return;
        }

        boolean notified = false;
        String subject = "[CloudPulse] " + alertType + ": " + monitor.getName();
        String cleanBody = alert.getMessage().replace("*", "").replace("`", "");

        notified |= webhookNotifier.send(props.getWebhook().getUrl(), alert.getMessage());

        for (String to : emailRecipients(monitor)) {
            notified |= sendEmail(to, subject, cleanBody);
        }
        for (Integration intg : monitor.getIntegrations()) {
            if (intg.isEnabled()) {
                notified |= dispatcher.dispatch(intg, alert.getMessage(), alertType);
            }
        }

        alert.setNotified(notified);
    }

    private Set<String> emailRecipients(Monitor monitor) {
        Set<String> recipients = new LinkedHashSet<>();
        if (monitor.getNotifyEmail() != null && !monitor.getNotifyEmail().isBlank()) {
            recipients.add(monitor.getNotifyEmail());
        }
        if (props.getAlert().getEmailTo() != null && !props.getAlert().getEmailTo().isBlank()) {
            recipients.add(props.getAlert().getEmailTo());
        }
        return recipients;
    }

    private boolean sendEmail(String to, String subject, String body) {
        if (to == null || to.isBlank()) return false;
        String apiKey = props.getResend().getApiKey();
        if (apiKey != null && !apiKey.isBlank()) {
            return resendNotifier.send(to, subject, body);
        }
        return emailNotifier.sendTo(to, props.getAlert().getEmailFrom(), subject, body);
    }
}
