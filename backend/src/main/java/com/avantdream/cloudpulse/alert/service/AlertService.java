package com.avantdream.cloudpulse.alert.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avantdream.cloudpulse.alert.dto.AlertResponse;
import com.avantdream.cloudpulse.alert.entity.Alert;
import com.avantdream.cloudpulse.alert.repository.AlertRepository;
import com.avantdream.cloudpulse.monitor.repository.MonitorRepository;
import com.avantdream.cloudpulse.shared.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlertService {

    private final AlertRepository alertRepository;
    private final MonitorRepository monitorRepository;

    public AlertService(AlertRepository alertRepository, MonitorRepository monitorRepository) {
        this.alertRepository = alertRepository;
        this.monitorRepository = monitorRepository;
    }

    @Transactional(readOnly = true)
    public List<AlertResponse> list(UUID serviceId, Boolean acknowledged, int limit, int offset) {
        List<Alert> alerts = alertRepository.findFiltered(serviceId, acknowledged,
                PageRequest.of(0, offset + limit));

        // Build service name map
        Map<UUID, String> names = monitorRepository.findAll().stream()
                .collect(Collectors.toMap(m -> m.getId(), m -> m.getName()));

        return alerts.stream()
                .skip(offset)
                .limit(limit)
                .map(a -> AlertResponse.from(a, names.get(a.getServiceId())))
                .toList();
    }

    public int acknowledgeAll(UUID serviceId, String alertType) {
        return alertRepository.acknowledgeAll(serviceId, alertType);
    }

    public AlertResponse acknowledge(int alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));
        alert.setAcknowledged(true);
        alertRepository.save(alert);

        String name = monitorRepository.findById(alert.getServiceId())
                .map(m -> m.getName()).orElse(null);
        return AlertResponse.from(alert, name);
    }
}
