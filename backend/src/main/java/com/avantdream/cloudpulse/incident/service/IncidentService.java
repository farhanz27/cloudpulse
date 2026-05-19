package com.avantdream.cloudpulse.incident.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avantdream.cloudpulse.alert.entity.Alert;
import com.avantdream.cloudpulse.alert.repository.AlertRepository;
import com.avantdream.cloudpulse.monitor.repository.MonitorRepository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class IncidentService {

    private final AlertRepository alertRepository;
    private final MonitorRepository monitorRepository;

    public IncidentService(AlertRepository alertRepository, MonitorRepository monitorRepository) {
        this.alertRepository = alertRepository;
        this.monitorRepository = monitorRepository;
    }

    public Map<String, Object> listIncidents(UUID serviceId, String status, int limit, int offset) {
        List<Alert> alerts = alertRepository.findDowntimeAndRecoveryFiltered(serviceId);

        Map<UUID, String> nameMap = monitorRepository.findAll().stream()
                .collect(Collectors.toMap(m -> m.getId(), m -> m.getName()));

        // Group by service
        Map<UUID, List<Alert>> byService = new LinkedHashMap<>();
        for (Alert a : alerts) {
            byService.computeIfAbsent(a.getServiceId(), k -> new ArrayList<>()).add(a);
        }

        List<Map<String, Object>> incidents = new ArrayList<>();
        for (Map.Entry<UUID, List<Alert>> entry : byService.entrySet()) {
            UUID sid = entry.getKey();
            String serviceName = nameMap.getOrDefault(sid, "Unknown");
            List<Alert> serviceAlerts = entry.getValue();

            Alert currentDown = null;
            for (Alert alert : serviceAlerts) {
                if ("DOWNTIME".equals(alert.getAlertType()) && currentDown == null) {
                    currentDown = alert;
                } else if ("RECOVERY".equals(alert.getAlertType()) && currentDown != null) {
                    double duration = (alert.getCreatedAt().toEpochMilli() - currentDown.getCreatedAt().toEpochMilli()) / 1000.0;
                    incidents.add(buildIncident(currentDown.getId(), sid, serviceName,
                            currentDown.getCreatedAt(), alert.getCreatedAt(), duration, "resolved"));
                    currentDown = null;
                }
            }
            if (currentDown != null) {
                double duration = (Instant.now().toEpochMilli() - currentDown.getCreatedAt().toEpochMilli()) / 1000.0;
                incidents.add(buildIncident(currentDown.getId(), sid, serviceName,
                        currentDown.getCreatedAt(), null, duration, "open"));
            }
        }

        // Filter by status
        List<Map<String, Object>> filtered = (status != null)
                ? incidents.stream().filter(i -> status.equals(i.get("status"))).toList()
                : incidents;

        // Sort newest first
        filtered = new ArrayList<>(filtered);
        filtered.sort((a, b) -> ((String) b.get("started_at")).compareTo((String) a.get("started_at")));

        int total = filtered.size();
        List<Map<String, Object>> page = filtered.stream().skip(offset).limit(limit).toList();
        return Map.of("total", total, "items", page);
    }

    private Map<String, Object> buildIncident(int id, UUID serviceId, String serviceName,
                                               Instant startedAt, Instant recoveredAt,
                                               double duration, String status) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", id);
        m.put("service_id", serviceId.toString());
        m.put("service_name", serviceName);
        m.put("started_at", startedAt.toString());
        m.put("recovered_at", recoveredAt != null ? recoveredAt.toString() : null);
        m.put("duration_seconds", duration);
        m.put("status", status);
        return m;
    }
}
