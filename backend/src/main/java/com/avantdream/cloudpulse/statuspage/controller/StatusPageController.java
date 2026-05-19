package com.avantdream.cloudpulse.statuspage.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.avantdream.cloudpulse.statuspage.dto.*;
import com.avantdream.cloudpulse.statuspage.service.StatusPageService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/status-pages")
public class StatusPageController {

    private final StatusPageService statusPageService;

    public StatusPageController(StatusPageService statusPageService) {
        this.statusPageService = statusPageService;
    }

    // Public — no auth (permitted in SecurityConfig)
    @GetMapping("/public/{slug}")
    public PublicStatusPageResponse getPublic(@PathVariable String slug) {
        return statusPageService.getPublic(slug);
    }

    @GetMapping
    public List<StatusPageResponse> list() {
        return statusPageService.list();
    }

    @GetMapping("/{id}")
    public Map<String, Object> get(@PathVariable UUID id) {
        return statusPageService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StatusPageResponse create(@Valid @RequestBody StatusPageRequest req) {
        return statusPageService.create(req);
    }

    @PatchMapping("/{id}")
    public StatusPageResponse update(@PathVariable UUID id, @RequestBody StatusPageUpdateRequest req) {
        return statusPageService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        statusPageService.delete(id);
    }
}
