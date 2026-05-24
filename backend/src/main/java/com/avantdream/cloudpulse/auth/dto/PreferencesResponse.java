package com.avantdream.cloudpulse.auth.dto;

import com.avantdream.cloudpulse.auth.entity.UserPreferences;

public record PreferencesResponse(
        boolean emailAlerts,
        boolean recoveryNotifications,
        int defaultIntervalSeconds,
        int defaultTimeoutSeconds,
        int defaultLatencyThresholdMs
) {
    public static PreferencesResponse from(UserPreferences p) {
        return new PreferencesResponse(
                p.isEmailAlerts(),
                p.isRecoveryNotifications(),
                p.getDefaultIntervalSeconds(),
                p.getDefaultTimeoutSeconds(),
                p.getDefaultLatencyThresholdMs()
        );
    }

    public static PreferencesResponse defaults() {
        return new PreferencesResponse(true, true, 60, 10, 5000);
    }
}
