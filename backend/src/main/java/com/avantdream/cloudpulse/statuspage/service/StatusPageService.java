package com.avantdream.cloudpulse.statuspage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avantdream.cloudpulse.monitor.entity.HealthLog;
import com.avantdream.cloudpulse.monitor.entity.Monitor;
import com.avantdream.cloudpulse.monitor.repository.HealthLogRepository;
import com.avantdream.cloudpulse.monitor.repository.MonitorRepository;
import com.avantdream.cloudpulse.shared.exception.ConflictException;
import com.avantdream.cloudpulse.shared.exception.ResourceNotFoundException;
import com.avantdream.cloudpulse.statuspage.dto.*;
import com.avantdream.cloudpulse.statuspage.entity.StatusPage;
import com.avantdream.cloudpulse.statuspage.entity.StatusPageEntry;
import com.avantdream.cloudpulse.statuspage.repository.StatusPageRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StatusPageService {

    private final StatusPageRepository statusPageRepository;
    private final MonitorRepository monitorRepository;
    private final HealthLogRepository healthLogRepository;

    public StatusPageService(StatusPageRepository statusPageRepository,
                              MonitorRepository monitorRepository,
                              HealthLogRepository healthLogRepository) {
        this.statusPageRepository = statusPageRepository;
        this.monitorRepository = monitorRepository;
        this.healthLogRepository = healthLogRepository;
    }

    @Transactional(readOnly = true)
    public List<StatusPageResponse> list() {
        return statusPageRepository.findAllWithEntries().stream()
                .map(StatusPageResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getById(UUID id) {
        StatusPage page = statusPageRepository.findByIdWithEntries(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status page not found"));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", page.getId().toString());
        result.put("name", page.getName());
        result.put("slug", page.getSlug());
        result.put("description", page.getDescription());
        result.put("created_at", page.getCreatedAt().toString());
        result.put("service_ids", page.getEntries().stream()
                .sorted(Comparator.comparingInt(StatusPageEntry::getDisplayOrder))
                .map(e -> e.getServiceId().toString())
                .toList());
        return result;
    }

    @Transactional(readOnly = true)
    public PublicStatusPageResponse getPublic(String slug) {
        StatusPage page = statusPageRepository.findBySlugWithEntries(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Status page not found"));

        List<StatusPageEntry> sorted = page.getEntries().stream()
                .sorted(Comparator.comparingInt(StatusPageEntry::getDisplayOrder))
                .toList();

        List<UUID> serviceIds = sorted.stream().map(StatusPageEntry::getServiceId).toList();
        Map<UUID, Monitor> monitorMap = monitorRepository.findAllById(serviceIds).stream()
                .collect(Collectors.toMap(Monitor::getId, m -> m));

        List<PublicServiceStatus> statuses = serviceIds.stream()
                .map(monitorMap::get)
                .filter(m -> m != null && m.isActive())
                .map(this::buildServiceStatus)
                .toList();

        return new PublicStatusPageResponse(page.getId(), page.getName(), page.getSlug(),
                page.getDescription(), overallStatus(statuses), statuses);
    }

    public StatusPageResponse create(StatusPageRequest req) {
        if (statusPageRepository.existsBySlug(req.slug())) {
            throw new ConflictException("Slug already in use");
        }
        StatusPage page = new StatusPage();
        page.setName(req.name());
        page.setSlug(req.slug());
        page.setDescription(req.description());
        page = statusPageRepository.save(page);

        addEntries(page, req.serviceIds());
        return StatusPageResponse.from(statusPageRepository.save(page));
    }

    public StatusPageResponse update(UUID id, StatusPageUpdateRequest req) {
        StatusPage page = statusPageRepository.findByIdWithEntries(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status page not found"));

        if (req.name() != null) page.setName(req.name());
        if (req.description() != null) page.setDescription(req.description());
        if (req.slug() != null && !req.slug().equals(page.getSlug())) {
            if (statusPageRepository.existsBySlugAndIdNot(req.slug(), id)) {
                throw new ConflictException("Slug already in use");
            }
            page.setSlug(req.slug());
        }
        if (req.serviceIds() != null) {
            page.getEntries().clear();
            addEntries(page, req.serviceIds());
        }
        return StatusPageResponse.from(statusPageRepository.save(page));
    }

    public void delete(UUID id) {
        StatusPage page = statusPageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status page not found"));
        statusPageRepository.delete(page);
    }

    private void addEntries(StatusPage page, List<UUID> serviceIds) {
        for (int i = 0; i < serviceIds.size(); i++) {
            page.getEntries().add(new StatusPageEntry(page, serviceIds.get(i), i));
        }
    }

    private PublicServiceStatus buildServiceStatus(Monitor monitor) {
        HealthLog latest = healthLogRepository
                .findTopByServiceIdOrderByCheckedAtDesc(monitor.getId())
                .orElse(null);

        Instant since24h = Instant.now().minus(24, ChronoUnit.HOURS);
        long total = healthLogRepository.countByServiceIdSince(monitor.getId(), since24h);
        long upCount = healthLogRepository.countUpByServiceIdSince(monitor.getId(), since24h);
        Double uptime24h = total > 0 ? Math.round(upCount * 100.0 / total * 100) / 100.0 : null;

        // 30-day bars
        Instant since30d = Instant.now().minus(30, ChronoUnit.DAYS);
        // We'll just return null bars for each missing day
        List<String> dates = new ArrayList<>();
        List<Double> bars = new ArrayList<>();
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 29; i >= 0; i--) {
            dates.add(today.minusDays(i).format(fmt));
            bars.add(null);
        }

        return new PublicServiceStatus(monitor.getId(), monitor.getName(), monitor.getUrl(),
                latest != null ? latest.getStatus() : null, uptime24h, bars, dates);
    }

    private String overallStatus(List<PublicServiceStatus> services) {
        if (services.isEmpty()) return "UNKNOWN";
        List<String> statuses = services.stream()
                .map(PublicServiceStatus::currentStatus)
                .filter(Objects::nonNull)
                .toList();
        if (statuses.isEmpty()) return "UNKNOWN";
        if (statuses.contains("DOWN")) return "OUTAGE";
        if (statuses.contains("DEGRADED")) return "DEGRADED";
        return "OPERATIONAL";
    }
}
