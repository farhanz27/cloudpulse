package com.avantdream.cloudpulse.integration.notify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailNotifier {

    private static final Logger log = LoggerFactory.getLogger(EmailNotifier.class);
    private final JavaMailSender mailSender;

    public EmailNotifier(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean send(Map<String, Object> config, String subject, String body) {
        try {
            String to = (String) config.get("to");
            String from = config.containsKey("from") ? (String) config.get("from") : null;
            if (to == null || to.isBlank()) return false;

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            if (from != null) msg.setFrom(from);
            mailSender.send(msg);
            return true;
        } catch (Exception ex) {
            log.debug("Email delivery failed: {}", ex.getMessage());
            return false;
        }
    }

    public boolean sendTo(String to, String from, String subject, String body) {
        return send(Map.of("to", to, "from", from), subject, body);
    }
}
