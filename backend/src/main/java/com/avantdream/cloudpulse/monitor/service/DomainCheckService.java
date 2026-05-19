package com.avantdream.cloudpulse.monitor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DomainCheckService {

    private static final Logger log = LoggerFactory.getLogger(DomainCheckService.class);

    private static final List<DateTimeFormatter> FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd-MMM-yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MMM dd yyyy")
    );

    // Matches lines like: "Expiry Date: 2025-04-01" or "Registry Expiry Date: 2025-04-01T00:00:00Z"
    private static final Pattern EXPIRY_PATTERN = Pattern.compile(
            "(?i)(?:expir[a-z ]+date|paid-till|expiry date)[:\\s]+([0-9T\\-/A-Za-z: .]+)",
            Pattern.CASE_INSENSITIVE
    );

    public Instant checkExpiry(String url) {
        try {
            URL parsed = new URL(url);
            String host = parsed.getHost();
            if (host == null || host.isBlank()) return null;
            // Strip www
            String domain = host.startsWith("www.") ? host.substring(4) : host;

            String whoisResponse = queryWhois(domain);
            if (whoisResponse == null) return null;

            return parseExpiry(whoisResponse);
        } catch (Exception ex) {
            log.debug("Domain WHOIS failed for {}: {}", url, ex.getMessage());
            return null;
        }
    }

    private String queryWhois(String domain) {
        try (Socket socket = new Socket("whois.iana.org", 43);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            socket.setSoTimeout(10_000);
            out.println(domain);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append('\n');
            }

            // If IANA returns a referral, follow it once
            String response = sb.toString();
            String referral = extractReferral(response);
            if (referral != null) {
                return queryWhoisServer(referral, domain);
            }
            return response;
        } catch (Exception ex) {
            log.debug("WHOIS query failed for {}: {}", domain, ex.getMessage());
            return null;
        }
    }

    private String queryWhoisServer(String server, String domain) {
        try (Socket socket = new Socket(server, 43);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            socket.setSoTimeout(10_000);
            out.println(domain);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        } catch (Exception ex) {
            log.debug("WHOIS referral query failed for {}/{}: {}", server, domain, ex.getMessage());
            return null;
        }
    }

    private String extractReferral(String response) {
        Pattern p = Pattern.compile("(?i)refer:\\s*(\\S+)");
        Matcher m = p.matcher(response);
        return m.find() ? m.group(1).trim() : null;
    }

    private Instant parseExpiry(String whois) {
        Matcher m = EXPIRY_PATTERN.matcher(whois);
        while (m.find()) {
            String raw = m.group(1).trim();
            // Try ISO instant first
            try {
                return Instant.parse(raw);
            } catch (Exception ignored) {}
            // Try date formatters
            String datePart = raw.contains("T") ? raw.substring(0, raw.indexOf('T')) : raw.trim();
            for (DateTimeFormatter fmt : FORMATTERS) {
                try {
                    return LocalDate.parse(datePart, fmt).atStartOfDay().toInstant(ZoneOffset.UTC);
                } catch (DateTimeParseException ignored) {}
            }
        }
        return null;
    }
}
