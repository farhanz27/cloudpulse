package com.avantdream.cloudpulse.integration.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.avantdream.cloudpulse.integration.dto.*;
import com.avantdream.cloudpulse.integration.service.IntegrationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/integrations")
public class IntegrationController {

    private final IntegrationService integrationService;

    public IntegrationController(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    @GetMapping
    public List<IntegrationResponse> list() {
        return integrationService.list();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IntegrationResponse create(@Valid @RequestBody IntegrationRequest req) {
        return integrationService.create(req);
    }

    @PatchMapping("/{id}")
    public IntegrationResponse update(@PathVariable int id, @RequestBody IntegrationUpdateRequest req) {
        return integrationService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        integrationService.delete(id);
    }

    @PostMapping("/{id}/test")
    public ResponseEntity<Map<String, Boolean>> test(@PathVariable int id) {
        try {
            integrationService.test(id);
            return ResponseEntity.ok(Map.of("ok", true));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(502).body(Map.of("ok", false));
        }
    }
}
