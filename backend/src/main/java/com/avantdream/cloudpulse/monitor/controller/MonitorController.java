package com.avantdream.cloudpulse.monitor.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.avantdream.cloudpulse.monitor.dto.*;
import com.avantdream.cloudpulse.monitor.service.MonitorService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/services")
public class MonitorController {

    private final MonitorService monitorService;

    public MonitorController(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @GetMapping
    public List<MonitorWithStatusResponse> list() {
        return monitorService.listAll();
    }

    @GetMapping("/{id}")
    public MonitorWithStatusResponse get(@PathVariable UUID id) {
        return monitorService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MonitorResponse create(@Valid @RequestBody MonitorRequest req) {
        return monitorService.create(req);
    }

    @PatchMapping("/{id}")
    public MonitorResponse update(@PathVariable UUID id, @RequestBody MonitorUpdateRequest req) {
        return monitorService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        monitorService.delete(id);
    }

    @PostMapping("/{id}/check")
    public MonitorWithStatusResponse checkNow(@PathVariable UUID id) {
        return monitorService.checkNow(id);
    }

    @PostMapping("/{id}/test-notification")
    public Map<String, Object> testNotification(@PathVariable UUID id) {
        int sent = monitorService.sendTestNotification(id);
        return Map.of("ok", sent > 0, "channels_notified", sent);
    }

    @PostMapping("/{id}/mute")
    public MonitorWithStatusResponse mute(@PathVariable UUID id, @RequestBody MuteRequest req) {
        return monitorService.mute(id, req);
    }
}
