package com.avantdream.cloudpulse.metrics.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avantdream.cloudpulse.alert.entity.Alert;
import com.avantdream.cloudpulse.alert.repository.AlertRepository;
import com.avantdream.cloudpulse.monitor.repository.HealthLogRepository;
import com.avantdream.cloudpulse.monitor.repository.MonitorRepository;
import com.avantdream.cloudpulse.shared.exception.ResourceNotFoundException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class MetricsService {

    private final JdbcTemplate jdbc;
    private final MonitorRepository monitorRepository;
    private final HealthLogRepository healthLogRepository;
    private final AlertRepository alertRepository;

    public MetricsService(JdbcTemplate jdbc, MonitorRepository monitorRepository,
                          HealthLogRepository healthLogRepository, AlertRepository alertRepository) {
        this.jdbc = jdbc;
        this.monitorRepository = monitorRepository;
        this.healthLogRepository = healthLogRepository;
        this.alertRepository = alertRepository;
    }

    public Map<String, Object> getUptimeBars(int days, UUID serviceId) {
        Instant since = Instant.now().minus(days, ChronoUnit.DAYS);

        String sql = """
                SELECT service_id::text,
                       to_char(to_timestamp(floor(extract(epoch from checked_at) / 86400.0) * 86400.0) AT TIME ZONE 'UTC', 'YYYY-MM-DD') as day,
                       count(*) as total,
                       count(*) filter (where status = 'UP') as up_count
                FROM health_logs
                WHERE checked_at >= ?
                """ +
                (serviceId != null ? " AND service_id = ?" : "") +
                " GROUP BY service_id, day ORDER BY service_id, day";

        List<Object> params = new ArrayList<>();
        params.add(java.sql.Timestamp.from(since));
        if (serviceId != null) params.add(serviceId);

        Map<String, Map<String, Double>> lookup = new HashMap<>();
        jdbc.query(sql, params.toArray(), rs -> {
            String sid = rs.getString("service_id");
            String day = rs.getString("day");
            long total = rs.getLong("total");
            long upCount = rs.getLong("up_count");
            lookup.computeIfAbsent(sid, k -> new HashMap<>())
                    .put(day, total > 0 ? Math.round(upCount * 100.0 / total * 10) / 10.0 : 0.0);
        });

        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> dateList = new ArrayList<>();
        for (int i = days - 1; i >= 0; i--) {
            dateList.add(today.minusDays(i).format(fmt));
        }

        Map<String, List<Double>> services = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, Double>> entry : lookup.entrySet()) {
            List<Double> bars = dateList.stream()
                    .map(d -> entry.getValue().get(d))
                    .toList();
            services.put(entry.getKey(), bars);
        }

        return Map.of("dates", dateList, "services", services);
    }

    public Map<String, Object> getLastIncidents() {
        var alerts = alertRepository.findAllDowntimeAndRecovery();

        Map<String, List<Alert>> byService = new LinkedHashMap<>();
        for (var alert : alerts) {
            byService.computeIfAbsent(alert.getServiceId().toString(), k -> new ArrayList<>()).add(alert);
        }

        Map<String, Object> out = new LinkedHashMap<>();
        for (Map.Entry<String, List<Alert>> entry : byService.entrySet()) {
            Instant currentDown = null;
            Map<String, Object> lastIncident = null;
            for (var alert : entry.getValue()) {
                if ("DOWNTIME".equals(alert.getAlertType()) && currentDown == null) {
                    currentDown = alert.getCreatedAt();
                } else if ("RECOVERY".equals(alert.getAlertType()) && currentDown != null) {
                    double duration = (alert.getCreatedAt().toEpochMilli() - currentDown.toEpochMilli()) / 1000.0;
                    lastIncident = Map.of("last_down_at", currentDown.toString(), "duration_seconds", duration);
                    currentDown = null;
                }
            }
            if (currentDown != null) {
                lastIncident = new LinkedHashMap<>();
                ((Map<String, Object>) lastIncident).put("last_down_at", currentDown.toString());
                ((Map<String, Object>) lastIncident).put("duration_seconds", null);
            }
            if (lastIncident != null) out.put(entry.getKey(), lastIncident);
        }
        return out;
    }

    public Map<String, Object> getHealthLogs(UUID serviceId, int limit, int offset) {
        monitorRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Monitor not found"));
        long total = healthLogRepository.countByServiceId(serviceId);
        var items = healthLogRepository.findByServiceIdOrderByCheckedAtDesc(serviceId,
                org.springframework.data.domain.PageRequest.of(0, offset + limit));
        var page = items.stream().skip(offset).toList();
        return Map.of("total", total, "items", page.stream()
                .map(h -> Map.of(
                        "id", h.getId(),
                        "service_id", h.getServiceId().toString(),
                        "status", h.getStatus(),
                        "response_time_ms", h.getResponseTimeMs() != null ? h.getResponseTimeMs() : (Object) null,
                        "status_code", h.getStatusCode() != null ? h.getStatusCode() : (Object) null,
                        "error_message", h.getErrorMessage() != null ? h.getErrorMessage() : (Object) null,
                        "checked_at", h.getCheckedAt().toString()
                )).toList());
    }

    public List<Map<String, Object>> getServiceIncidents(UUID serviceId, int limit) {
        monitorRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Monitor not found"));

        var alerts = alertRepository.findDowntimeAndRecoveryByService(serviceId);
        List<Map<String, Object>> incidents = new ArrayList<>();
        Instant currentDown = null;
        for (var alert : alerts) {
            if ("DOWNTIME".equals(alert.getAlertType()) && currentDown == null) {
                currentDown = alert.getCreatedAt();
            } else if ("RECOVERY".equals(alert.getAlertType()) && currentDown != null) {
                double duration = (alert.getCreatedAt().toEpochMilli() - currentDown.toEpochMilli()) / 1000.0;
                Map<String, Object> inc = new LinkedHashMap<>();
                inc.put("started_at", currentDown.toString());
                inc.put("recovered_at", alert.getCreatedAt().toString());
                inc.put("duration_seconds", duration);
                incidents.add(inc);
                currentDown = null;
            }
        }
        if (currentDown != null) {
            double duration = (Instant.now().toEpochMilli() - currentDown.toEpochMilli()) / 1000.0;
            Map<String, Object> inc = new LinkedHashMap<>();
            inc.put("started_at", currentDown.toString());
            inc.put("recovered_at", null);
            inc.put("duration_seconds", duration);
            incidents.add(inc);
        }
        Collections.reverse(incidents);
        return incidents.stream().limit(limit).toList();
    }

    public Map<String, Object> getReliability(UUID serviceId) {
        monitorRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Monitor not found"));

        var alerts = alertRepository.findDowntimeAndRecoveryByService(serviceId);
        List<Double> recoveredDurations = new ArrayList<>();
        Instant currentDown = null;
        boolean openIncident = false;

        for (var alert : alerts) {
            if ("DOWNTIME".equals(alert.getAlertType()) && currentDown == null) {
                currentDown = alert.getCreatedAt();
                openIncident = true;
            } else if ("RECOVERY".equals(alert.getAlertType()) && currentDown != null) {
                recoveredDurations.add((alert.getCreatedAt().toEpochMilli() - currentDown.toEpochMilli()) / 1000.0);
                currentDown = null;
                openIncident = false;
            }
        }

        Double mttr = recoveredDurations.isEmpty() ? null
                : Math.round(recoveredDurations.stream().mapToDouble(d -> d).average().orElse(0) * 10) / 10.0;
        int totalIncidents = recoveredDurations.size() + (openIncident ? 1 : 0);

        Instant since30d = Instant.now().minus(30, ChronoUnit.DAYS);
        long incidents30d = alerts.stream()
                .filter(a -> "DOWNTIME".equals(a.getAlertType()) && a.getCreatedAt().isAfter(since30d))
                .count();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("mttr_seconds", mttr);
        result.put("total_incidents", totalIncidents);
        result.put("incidents_last_30d", incidents30d);
        return result;
    }

    public Map<String, Object> getMetrics(UUID serviceId, int rangeHours, int resolutionMinutes) {
        monitorRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Monitor not found"));

        Instant since = Instant.now().minus(rangeHours, ChronoUnit.HOURS);
        long bucketSec = (long) resolutionMinutes * 60;

        String sql = """
                SELECT to_timestamp(floor(extract(epoch from checked_at) / ?) * ?) as bucket,
                       avg(response_time_ms) as avg_rt,
                       min(response_time_ms) as min_rt,
                       max(response_time_ms) as max_rt,
                       count(*) as total,
                       count(*) filter (where status = 'UP') as up_count
                FROM health_logs
                WHERE service_id = ? AND checked_at >= ?
                GROUP BY bucket
                ORDER BY bucket
                """;

        List<Map<String, Object>> data = jdbc.query(sql,
                new Object[]{bucketSec, bucketSec, serviceId, java.sql.Timestamp.from(since)},
                (rs, i) -> {
                    long total = rs.getLong("total");
                    long upCount = rs.getLong("up_count");
                    double uptime = total > 0 ? Math.round(upCount * 100.0 / total * 100) / 100.0 : 0.0;
                    Map<String, Object> point = new LinkedHashMap<>();
                    point.put("timestamp", rs.getTimestamp("bucket") != null ? rs.getTimestamp("bucket").toInstant().toString() : null);
                    double avgRt = rs.getDouble("avg_rt");
                    point.put("avg_response_time_ms", rs.wasNull() ? null : Math.round(avgRt * 100) / 100.0);
                    double minRt = rs.getDouble("min_rt");
                    point.put("min_response_time_ms", rs.wasNull() ? null : Math.round(minRt * 100) / 100.0);
                    double maxRt = rs.getDouble("max_rt");
                    point.put("max_response_time_ms", rs.wasNull() ? null : Math.round(maxRt * 100) / 100.0);
                    point.put("uptime_percent", uptime);
                    point.put("total_checks", total);
                    return point;
                });

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("service_id", serviceId.toString());
        result.put("range_hours", rangeHours);
        result.put("resolution_minutes", resolutionMinutes);
        result.put("data", data);
        return result;
    }
}
