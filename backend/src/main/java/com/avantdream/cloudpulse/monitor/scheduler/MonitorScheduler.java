package com.avantdream.cloudpulse.monitor.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.avantdream.cloudpulse.alert.service.AlertingService;
import com.avantdream.cloudpulse.monitor.entity.HealthLog;
import com.avantdream.cloudpulse.monitor.entity.Monitor;
import com.avantdream.cloudpulse.monitor.repository.HealthLogRepository;
import com.avantdream.cloudpulse.monitor.repository.MonitorRepository;
import com.avantdream.cloudpulse.monitor.service.DomainCheckService;
import com.avantdream.cloudpulse.monitor.service.MonitorCheckService;
import com.avantdream.cloudpulse.monitor.service.SslCheckService;
import com.avantdream.cloudpulse.shared.config.AppProperties;

import java.time.Instant;
import java.util.*;

@Component
public class MonitorScheduler {

    private static final Logger log = LoggerFactory.getLogger(MonitorScheduler.class);
    private static final int MIN_INTERVAL_SECONDS = 5;

    private final MonitorRepository monitorRepository;
    private final HealthLogRepository healthLogRepository;
    private final MonitorCheckService checkService;
    private final AlertingService alertingService;
    private final SslCheckService sslCheckService;
    private final DomainCheckService domainCheckService;
    private final AppProperties props;

    public MonitorScheduler(MonitorRepository monitorRepository,
                             HealthLogRepository healthLogRepository,
                             MonitorCheckService checkService,
                             AlertingService alertingService,
                             SslCheckService sslCheckService,
                             DomainCheckService domainCheckService,
                             AppProperties props) {
        this.monitorRepository = monitorRepository;
        this.healthLogRepository = healthLogRepository;
        this.checkService = checkService;
        this.alertingService = alertingService;
        this.sslCheckService = sslCheckService;
        this.domainCheckService = domainCheckService;
        this.props = props;
    }

    @Scheduled(fixedRateString = "${cloudpulse.scheduler.tick-ms:5000}")
    @Transactional
    public void runHealthChecks() {
        List<Monitor> monitors = monitorRepository.findAllActiveWithIntegrations();
        if (monitors.isEmpty()) return;

        Instant now = Instant.now();

        // Fetch last checked timestamps for all monitors
        List<UUID> ids = monitors.stream().map(Monitor::getId).toList();
        List<Object[]> rows = healthLogRepository.findLastCheckedAtForServices(ids);
        Map<UUID, Instant> lastChecked = new HashMap<>();
        for (Object[] row : rows) {
            lastChecked.put((UUID) row[0], (Instant) row[1]);
        }

        List<Monitor> due = monitors.stream()
                .filter(m -> isDue(m, lastChecked.get(m.getId()), now))
                .toList();

        if (due.isEmpty()) return;

        for (Monitor monitor : due) {
            try {
                HealthLog healthLog = checkService.check(monitor);
                healthLogRepository.save(healthLog);
                alertingService.evaluate(monitor, healthLog);
                monitorRepository.save(monitor);
            } catch (Exception ex) {
                log.error("Health check failed for monitor {}: {}", monitor.getName(), ex.getMessage());
            }
        }

        log.info("Health checks completed for {} monitor(s)", due.size());
    }

    @Scheduled(fixedRate = 300_000)
    public void runKeepAlivePings() {
        List<Monitor> monitors = monitorRepository.findAllActiveKeepAlive();
        if (monitors.isEmpty()) return;
        for (Monitor monitor : monitors) {
            checkService.keepAlivePing(monitor);
        }
        log.debug("Keep-alive pings sent to {} monitors", monitors.size());
    }

    @Scheduled(fixedRate = 43_200_000)
    @Transactional
    public void runSslChecks() {
        List<Monitor> monitors = monitorRepository.findAllActiveWithIntegrations();
        int updated = 0;
        for (Monitor monitor : monitors) {
            Instant expiry = sslCheckService.checkExpiry(monitor.getUrl());
            if (expiry != null) {
                monitor.setSslValidUntil(expiry);
                monitorRepository.save(monitor);
                updated++;
            }
        }
        log.info("SSL checks complete: {}/{} updated", updated, monitors.size());
    }

    @Scheduled(fixedRate = 86_400_000)
    @Transactional
    public void runDomainChecks() {
        List<Monitor> monitors = monitorRepository.findAllActiveWithIntegrations();
        int updated = 0;
        for (Monitor monitor : monitors) {
            Instant expiry = domainCheckService.checkExpiry(monitor.getUrl());
            if (expiry != null) {
                monitor.setDomainValidUntil(expiry);
                monitorRepository.save(monitor);
                updated++;
            }
        }
        log.info("Domain checks complete: {}/{} updated", updated, monitors.size());
    }

    private boolean isDue(Monitor monitor, Instant lastCheckedAt, Instant now) {
        if (lastCheckedAt == null) return true;
        int interval = Math.max(monitor.getCheckIntervalSeconds(), MIN_INTERVAL_SECONDS);
        return now.minusSeconds(interval).isAfter(lastCheckedAt) || now.minusSeconds(interval).equals(lastCheckedAt);
    }
}
