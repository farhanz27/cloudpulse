package com.avantdream.cloudpulse.usage;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avantdream.cloudpulse.integration.repository.IntegrationRepository;
import com.avantdream.cloudpulse.monitor.repository.MonitorRepository;
import com.avantdream.cloudpulse.shared.plan.FreePlan;
import com.avantdream.cloudpulse.statuspage.repository.StatusPageRepository;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class UsageService {

    private final MonitorRepository monitorRepository;
    private final StatusPageRepository statusPageRepository;
    private final IntegrationRepository integrationRepository;

    public UsageService(MonitorRepository monitorRepository,
                        StatusPageRepository statusPageRepository,
                        IntegrationRepository integrationRepository) {
        this.monitorRepository = monitorRepository;
        this.statusPageRepository = statusPageRepository;
        this.integrationRepository = integrationRepository;
    }

    public Map<String, Object> getUsage() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("plan", "free");
        result.put("monitors",     quota(monitorRepository.count(),     FreePlan.MONITOR_LIMIT));
        result.put("status_pages", quota(statusPageRepository.count(),  FreePlan.STATUS_PAGE_LIMIT));
        result.put("integrations", quota(integrationRepository.count(), FreePlan.INTEGRATION_LIMIT));
        result.put("min_check_interval_seconds", FreePlan.MIN_CHECK_INTERVAL_S);
        return result;
    }

    private Map<String, Object> quota(long used, int limit) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("used", used);
        m.put("limit", limit);
        return m;
    }
}
