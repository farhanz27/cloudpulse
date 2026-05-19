package com.avantdream.cloudpulse.metrics.controller;

import org.springframework.web.bind.annotation.*;

import com.avantdream.cloudpulse.metrics.service.MetricsService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/uptime-bars")
    public Map<String, Object> uptimeBars(
            @RequestParam(defaultValue = "45") int days,
            @RequestParam(required = false) UUID serviceId) {
        return metricsService.getUptimeBars(days, serviceId);
    }

    @GetMapping("/last-incidents")
    public Map<String, Object> lastIncidents() {
        return metricsService.getLastIncidents();
    }

    @GetMapping("/{serviceId}/logs")
    public Map<String, Object> healthLogs(
            @PathVariable UUID serviceId,
            @RequestParam(defaultValue = "100") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        return metricsService.getHealthLogs(serviceId, limit, offset);
    }

    @GetMapping("/{serviceId}/incidents")
    public List<Map<String, Object>> serviceIncidents(
            @PathVariable UUID serviceId,
            @RequestParam(defaultValue = "20") int limit) {
        return metricsService.getServiceIncidents(serviceId, limit);
    }

    @GetMapping("/{serviceId}/reliability")
    public Map<String, Object> reliability(@PathVariable UUID serviceId) {
        return metricsService.getReliability(serviceId);
    }

    @GetMapping("/{serviceId}")
    public Map<String, Object> metrics(
            @PathVariable UUID serviceId,
            @RequestParam(defaultValue = "24") int rangeHours,
            @RequestParam(defaultValue = "5") int resolutionMinutes) {
        return metricsService.getMetrics(serviceId, rangeHours, resolutionMinutes);
    }
}
