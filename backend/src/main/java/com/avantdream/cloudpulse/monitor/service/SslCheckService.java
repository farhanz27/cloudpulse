package com.avantdream.cloudpulse.monitor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.time.Instant;

@Service
public class SslCheckService {

    private static final Logger log = LoggerFactory.getLogger(SslCheckService.class);

    public Instant checkExpiry(String url) {
        try {
            URL parsed = new URL(url);
            if (!"https".equalsIgnoreCase(parsed.getProtocol())) return null;

            String host = parsed.getHost();
            int port = parsed.getPort() == -1 ? 443 : parsed.getPort();

            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            try (SSLSocket socket = (SSLSocket) factory.createSocket(host, port)) {
                socket.setSoTimeout(10_000);
                socket.startHandshake();
                X509Certificate cert = (X509Certificate) socket.getSession().getPeerCertificates()[0];
                return cert.getNotAfter().toInstant();
            }
        } catch (Exception ex) {
            log.debug("SSL check failed for {}: {}", url, ex.getMessage());
            return null;
        }
    }
}
