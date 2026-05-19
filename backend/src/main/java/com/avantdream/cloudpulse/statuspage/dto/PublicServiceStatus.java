package com.avantdream.cloudpulse.statuspage.dto;

import java.util.List;
import java.util.UUID;

public record PublicServiceStatus(
        UUID id,
        String name,
        String url,
        String currentStatus,
        Double uptimePercent,
        List<Double> uptimeBars,
        List<String> uptimeDates
) {}
