package com.avantdream.cloudpulse.monitor.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Service
public class GeoService {

    private static final Logger log = LoggerFactory.getLogger(GeoService.class);

    private static final List<Map.Entry<String, List<String>>> PROVIDERS = List.of(
            Map.entry("AWS", List.of("amazon", "aws")),
            Map.entry("GCP", List.of("google")),
            Map.entry("Azure", List.of("microsoft", "azure")),
            Map.entry("Cloudflare", List.of("cloudflare")),
            Map.entry("DigitalOcean", List.of("digitalocean")),
            Map.entry("Akamai", List.of("akamai", "linode")),
            Map.entry("Hetzner", List.of("hetzner")),
            Map.entry("OVH", List.of("ovh")),
            Map.entry("Vultr", List.of("vultr")),
            Map.entry("Fastly", List.of("fastly")),
            Map.entry("Vercel", List.of("vercel"))
    );

    private static final List<String> PRIVATE_PREFIXES = List.of(
            "10.", "172.", "192.168.", "127.", "0:0:0:0:0:0:0:1"
    );

    private final RestClient restClient;

    public GeoService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String detectRegion(String url) {
        try {
            URL parsed = new URL(url);
            String hostname = parsed.getHost();
            if (hostname == null || hostname.isBlank()) return null;

            String ip = InetAddress.getByName(hostname).getHostAddress();
            if (PRIVATE_PREFIXES.stream().anyMatch(ip::startsWith)) return null;

            JsonNode data = restClient.get()
                    .uri(URI.create("http://ip-api.com/json/" + ip + "?fields=status,countryCode,regionName,city,org,isp"))
                    .retrieve()
                    .body(JsonNode.class);

            if (data == null || !"success".equals(data.path("status").asText())) return null;

            String org = data.path("org").asText("");
            String isp = data.path("isp").asText("");
            String provider = detectProvider(org, isp);

            String city = data.path("city").asText("");
            String regionName = data.path("regionName").asText("");
            String country = data.path("countryCode").asText("");
            String locationPart = city.isBlank() ? regionName : city;
            String location = (locationPart.isBlank() ? "" : locationPart) +
                    (country.isBlank() ? "" : (locationPart.isBlank() ? country : ", " + country));

            if (provider != null && !location.isBlank()) return provider + " • " + location;
            return provider != null ? provider : (location.isBlank() ? null : location);

        } catch (Exception ex) {
            log.debug("Region detection failed for {}: {}", url, ex.getMessage());
            return null;
        }
    }

    private String detectProvider(String org, String isp) {
        String text = (org + " " + isp).toLowerCase();
        for (Map.Entry<String, List<String>> entry : PROVIDERS) {
            for (String kw : entry.getValue()) {
                if (text.contains(kw)) return entry.getKey();
            }
        }
        return null;
    }
}
