package com.avantdream.cloudpulse.auth.dto;

public record UpdatePreferencesRequest(
        Boolean emailAlerts,
        Boolean recoveryNotifications,
        Integer defaultIntervalSeconds,
        Integer defaultTimeoutSeconds,
        Integer defaultLatencyThresholdMs
) {}
