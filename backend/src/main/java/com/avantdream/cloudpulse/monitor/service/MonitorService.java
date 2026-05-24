package com.avantdream.cloudpulse.monitor.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avantdream.cloudpulse.alert.service.AlertingService;
import com.avantdream.cloudpulse.integration.entity.Integration;
import com.avantdream.cloudpulse.integration.repository.IntegrationRepository;

import java.util.ArrayList;
import com.avantdream.cloudpulse.monitor.dto.*;
import com.avantdream.cloudpulse.monitor.entity.HealthLog;
import com.avantdream.cloudpulse.monitor.entity.Monitor;
import com.avantdream.cloudpulse.monitor.repository.HealthLogRepository;
import com.avantdream.cloudpulse.monitor.repository.MonitorRepository;
import com.avantdream.cloudpulse.shared.exception.BadRequestException;
import com.avantdream.cloudpulse.shared.exception.QuotaExceededException;
import com.avantdream.cloudpulse.shared.exception.ResourceNotFoundException;
import com.avantdream.cloudpulse.shared.plan.FreePlan;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class MonitorService {

    private final MonitorRepository monitorRepository;
    private final HealthLogRepository healthLogRepository;
    private final IntegrationRepository integrationRepository;
    private final MonitorCheckService checkService;
    private final AlertingService alertingService;
    private final GeoService geoService;

    public MonitorService(MonitorRepository monitorRepository,
                          HealthLogRepository healthLogRepository,
                          IntegrationRepository integrationRepository,
                          MonitorCheckService checkService,
                          AlertingService alertingService,
                          GeoService geoService) {
        this.monitorRepository = monitorRepository;
        this.healthLogRepository = healthLogRepository;
        this.integrationRepository = integrationRepository;
        this.checkService = checkService;
        this.alertingService = alertingService;
        this.geoService = geoService;
    }

    @Transactional(readOnly = true)
    public List<MonitorWithStatusResponse> listAll() {
        List<Monitor> monitors = monitorRepository.findAllByOrderByNameAsc();
        if (monitors.isEmpty()) return List.of();

        List<UUID> ids = monitors.stream().map(Monitor::getId).toList();
        Instant since24h = Instant.now().minus(24, ChronoUnit.HOURS);

        Map<UUID, HealthLog> latestByService = healthLogRepository.findLatestPerServiceIds(ids)
                .stream().collect(Collectors.toMap(HealthLog::getServiceId, h -> h));

        Map<UUID, long[]> statsById = new HashMap<>();
        for (Object[] row : healthLogRepository.findUptimeStatsSince(ids, since24h)) {
            UUID sid = (UUID) row[0];
            long total   = ((Number) row[1]).longValue();
            long upCount = ((Number) row[2]).longValue();
            statsById.put(sid, new long[]{total, upCount});
        }

        return monitors.stream().map(m -> {
            HealthLog latest = latestByService.get(m.getId());
            long[] stats = statsById.getOrDefault(m.getId(), new long[]{0L, 0L});
            Double uptimePct = stats[0] > 0
                    ? Math.round((stats[1] * 100.0 / stats[0]) * 100.0) / 100.0
                    : null;
            return MonitorWithStatusResponse.from(
                    m,
                    latest != null ? latest.getStatus() : null,
                    latest != null ? latest.getResponseTimeMs() : null,
                    uptimePct,
                    latest != null ? latest.getCheckedAt() : null);
        }).toList();
    }

    @Transactional(readOnly = true)
    public MonitorWithStatusResponse getById(UUID id) {
        Monitor monitor = requireMonitor(id);
        return enrich(monitor);
    }

    public MonitorResponse create(MonitorRequest req) {
        if (monitorRepository.count() >= FreePlan.MONITOR_LIMIT) {
            throw new QuotaExceededException(
                "Free plan limit reached: " + FreePlan.MONITOR_LIMIT + " monitors maximum");
        }
        Monitor monitor = new Monitor();
        applyRequest(monitor, req);
        monitor.setRegion(geoService.detectRegion(req.url()));
        setIntegrations(monitor, req.integrationIds());
        return MonitorResponse.from(monitorRepository.save(monitor));
    }

    public MonitorResponse update(UUID id, MonitorUpdateRequest req) {
        Monitor monitor = requireMonitor(id);
        if (req.name() != null) monitor.setName(req.name());
        if (req.url() != null) {
            monitor.setUrl(req.url());
            monitor.setRegion(geoService.detectRegion(req.url()));
        }
        if (req.checkIntervalSeconds() != null) {
            if (req.checkIntervalSeconds() < FreePlan.MIN_CHECK_INTERVAL_S) {
                throw new BadRequestException(
                    "Minimum check interval is " + FreePlan.MIN_CHECK_INTERVAL_S / 60 + " minutes on the free plan");
            }
            monitor.setCheckIntervalSeconds(req.checkIntervalSeconds());
        }
        if (req.timeoutSeconds() != null) monitor.setTimeoutSeconds(req.timeoutSeconds());
        if (req.expectedStatusCode() != null) monitor.setExpectedStatusCode(req.expectedStatusCode());
        if (req.isActive() != null) monitor.setActive(req.isActive());
        if (req.latencyThresholdMs() != null) monitor.setLatencyThresholdMs(req.latencyThresholdMs());
        if (req.notifyEmail() != null) monitor.setNotifyEmail(req.notifyEmail().isBlank() ? null : req.notifyEmail());
        if (req.integrationIds() != null) setIntegrations(monitor, req.integrationIds());
        if (req.ipVersion() != null) monitor.setIpVersion(req.ipVersion());
        return MonitorResponse.from(monitorRepository.save(monitor));
    }

    public void delete(UUID id) {
        Monitor monitor = requireMonitor(id);
        monitorRepository.delete(monitor);
    }

    public MonitorWithStatusResponse checkNow(UUID id) {
        Monitor monitor = monitorRepository.findAllActiveWithIntegrations().stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Monitor not found"));
        HealthLog log = checkService.check(monitor);
        healthLogRepository.save(log);
        alertingService.evaluate(monitor, log);
        monitorRepository.save(monitor);
        return enrich(monitor);
    }

    public int sendTestNotification(UUID id) {
        Monitor monitor = monitorRepository.findAllActiveWithIntegrations().stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Monitor not found"));
        return alertingService.sendTestNotification(monitor);
    }

    public MonitorWithStatusResponse mute(UUID id, MuteRequest req) {
        Monitor monitor = requireMonitor(id);
        if (req.minutes() != null && req.minutes() > 0) {
            monitor.setMutedUntil(Instant.now().plus(req.minutes(), ChronoUnit.MINUTES));
        } else {
            monitor.setMutedUntil(null);
        }
        return enrich(monitorRepository.save(monitor));
    }

    private MonitorWithStatusResponse enrich(Monitor monitor) {
        HealthLog latest = healthLogRepository
                .findTopByServiceIdOrderByCheckedAtDesc(monitor.getId())
                .orElse(null);

        Instant since24h = Instant.now().minus(24, ChronoUnit.HOURS);
        long total = healthLogRepository.countByServiceIdSince(monitor.getId(), since24h);
        long upCount = healthLogRepository.countUpByServiceIdSince(monitor.getId(), since24h);
        Double uptimePct = total > 0 ? Math.round((upCount * 100.0 / total) * 100.0) / 100.0 : null;

        return MonitorWithStatusResponse.from(
                monitor,
                latest != null ? latest.getStatus() : null,
                latest != null ? latest.getResponseTimeMs() : null,
                uptimePct,
                latest != null ? latest.getCheckedAt() : null
        );
    }

    private void applyRequest(Monitor monitor, MonitorRequest req) {
        monitor.setName(req.name());
        monitor.setUrl(req.url());
        if (req.checkIntervalSeconds() < FreePlan.MIN_CHECK_INTERVAL_S) {
            throw new BadRequestException(
                "Minimum check interval is " + FreePlan.MIN_CHECK_INTERVAL_S / 60 + " minutes on the free plan");
        }
        monitor.setCheckIntervalSeconds(req.checkIntervalSeconds());
        monitor.setTimeoutSeconds(req.timeoutSeconds());
        monitor.setExpectedStatusCode(req.expectedStatusCode());
        monitor.setActive(req.isActive());
        monitor.setLatencyThresholdMs(req.latencyThresholdMs());
        monitor.setNotifyEmail(req.notifyEmail());
        monitor.setIpVersion(req.ipVersion());
    }

    private void setIntegrations(Monitor monitor, List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            monitor.setIntegrations(new ArrayList<>());
        } else {
            monitor.setIntegrations(new ArrayList<>(integrationRepository.findAllById(ids)));
        }
    }

    public void clearAllHealthLogs() {
        healthLogRepository.deleteAllLogs();
    }

    private Monitor requireMonitor(UUID id) {
        return monitorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monitor not found"));
    }
}
