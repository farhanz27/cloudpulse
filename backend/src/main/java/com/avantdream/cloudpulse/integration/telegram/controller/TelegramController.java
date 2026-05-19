package com.avantdream.cloudpulse.integration.telegram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.avantdream.cloudpulse.shared.exception.ResourceNotFoundException;
import com.avantdream.cloudpulse.integration.telegram.service.TelegramService;

import java.util.Map;

@RestController
@RequestMapping("/api/telegram")
public class TelegramController {

    private final TelegramService telegramService;

    public TelegramController(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @PostMapping("/start-link")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Map<String, Object>> startLink() {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(telegramService.startLink());
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(503).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/link-status/{code}")
    public ResponseEntity<Map<String, Object>> linkStatus(@PathVariable String code) {
        try {
            return ResponseEntity.ok(telegramService.linkStatus(code));
        } catch (ResourceNotFoundException ex) {
            int status = ex.getMessage().contains("expired") ? 410 : 404;
            return ResponseEntity.status(status).body(Map.of("error", ex.getMessage()));
        }
    }
}
