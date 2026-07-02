package com.avantdream.cloudpulse.alert.service;

import com.avantdream.cloudpulse.alert.entity.Alert;
import com.avantdream.cloudpulse.alert.repository.AlertRepository;
import com.avantdream.cloudpulse.integration.notify.AlertingDispatcher;
import com.avantdream.cloudpulse.integration.notify.ResendNotifier;
import com.avantdream.cloudpulse.monitor.entity.HealthLog;
import com.avantdream.cloudpulse.monitor.entity.Monitor;
import com.avantdream.cloudpulse.shared.config.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertingServiceTest {

    @Mock AlertRepository alertRepository;
    @Mock AlertingDispatcher dispatcher;
    @Mock ResendNotifier resendNotifier;

    AlertingService service;
    final AppProperties props = new AppProperties(); // default threshold = 3

    @BeforeEach
    void setUp() {
        service = new AlertingService(alertRepository, props, dispatcher, resendNotifier);
        lenient().when(alertRepository.save(any(Alert.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    private Monitor monitor(int consecutiveFailures) {
        Monitor m = new Monitor();
        m.setId(UUID.randomUUID());
        m.setName("API");
        m.setUrl("https://api.example.com");
        m.setConsecutiveFailures(consecutiveFailures);
        return m;
    }

    private HealthLog down() {
        return new HealthLog(null, "DOWN", null, null, "Connection refused");
    }

    private HealthLog up(double responseTimeMs) {
        return new HealthLog(null, "UP", responseTimeMs, 200, null);
    }

    @Test
    void firesDowntimeAlertOnReachingThreshold() {
        Monitor m = monitor(2); // one more failure hits the threshold of 3

        service.evaluate(m, down());

        ArgumentCaptor<Alert> saved = ArgumentCaptor.forClass(Alert.class);
        verify(alertRepository).save(saved.capture());
        assertThat(saved.getValue().getAlertType()).isEqualTo("DOWNTIME");
        assertThat(m.getConsecutiveFailures()).isEqualTo(3);
    }

    @Test
    void doesNotFireBeforeThreshold() {
        Monitor m = monitor(0);

        service.evaluate(m, down());

        verify(alertRepository, never()).save(any());
        assertThat(m.getConsecutiveFailures()).isEqualTo(1);
    }

    @Test
    void firesRecoveryAndResetsFailuresWhenBackUp() {
        Monitor m = monitor(3); // was in DOWNTIME

        service.evaluate(m, up(120.0));

        ArgumentCaptor<Alert> saved = ArgumentCaptor.forClass(Alert.class);
        verify(alertRepository).save(saved.capture());
        assertThat(saved.getValue().getAlertType()).isEqualTo("RECOVERY");
        assertThat(m.getConsecutiveFailures()).isZero();
    }
}
