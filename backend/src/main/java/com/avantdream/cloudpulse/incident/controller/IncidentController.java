package com.avantdream.cloudpulse.incident.controller;

import org.springframework.web.bind.annotation.*;

import com.avantdream.cloudpulse.incident.service.IncidentService;

import java.time.Instant;
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
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String since,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        Instant sinceInstant = since != null ? Instant.parse(since) : null;
        return incidentService.listIncidents(serviceId, status, search, sinceInstant, limit, offset);
    }
}
