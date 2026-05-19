package com.avantdream.cloudpulse.incident.controller;

import org.springframework.web.bind.annotation.*;

import com.avantdream.cloudpulse.incident.service.IncidentService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public Map<String, Object> list(
            @RequestParam(required = false) UUID serviceId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        return incidentService.listIncidents(serviceId, status, limit, offset);
    }
}
