package com.avantdream.cloudpulse.alert.controller;

import org.springframework.web.bind.annotation.*;

import com.avantdream.cloudpulse.alert.dto.AlertResponse;
import com.avantdream.cloudpulse.alert.service.AlertService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public List<AlertResponse> list(
            @RequestParam(required = false) UUID serviceId,
            @RequestParam(required = false) Boolean acknowledged,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        return alertService.list(serviceId, acknowledged, limit, offset);
    }

    @PostMapping("/acknowledge-all")
    public Map<String, Boolean> acknowledgeAll(
            @RequestParam(required = false) UUID serviceId,
            @RequestParam(required = false) String alertType) {
        alertService.acknowledgeAll(serviceId, alertType);
        return Map.of("ok", true);
    }

    @PatchMapping("/{alertId}/acknowledge")
    public AlertResponse acknowledge(@PathVariable int alertId) {
        return alertService.acknowledge(alertId);
    }
}
