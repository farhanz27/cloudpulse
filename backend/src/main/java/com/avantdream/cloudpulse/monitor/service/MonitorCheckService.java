package com.avantdream.cloudpulse.monitor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avantdream.cloudpulse.monitor.entity.HealthLog;
import com.avantdream.cloudpulse.monitor.entity.Monitor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class MonitorCheckService {

    private static final Logger log = LoggerFactory.getLogger(MonitorCheckService.class);

    private final HttpClient httpClient;

    public MonitorCheckService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HealthLog check(Monitor monitor) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(monitor.getUrl()))
                .timeout(Duration.ofSeconds(monitor.getTimeoutSeconds()))
                .GET()
                .build();

        long start = System.nanoTime();
        try {
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            double latencyMs = (System.nanoTime() - start) / 1_000_000.0;
            int code = response.statusCode();

            String status;
            if (code == monitor.getExpectedStatusCode()) {
                status = "UP";
            } else if (code < 500) {
                status = "DEGRADED";
            } else {
                status = "DOWN";
            }

            return new HealthLog(monitor.getId(), status, latencyMs, code, null);

        } catch (Exception ex) {
            return new HealthLog(monitor.getId(), "DOWN", null, null,
                    truncate(ex.getMessage(), 1024));
        }
    }

    private String truncate(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }
}
