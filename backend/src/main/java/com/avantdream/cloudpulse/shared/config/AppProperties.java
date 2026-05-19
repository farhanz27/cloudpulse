package com.avantdream.cloudpulse.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloudpulse")
public class AppProperties {

    private Jwt jwt = new Jwt();
    private Alerting alerting = new Alerting();
    private Scheduler scheduler = new Scheduler();
    private Telegram telegram = new Telegram();
    private Resend resend = new Resend();
    private Alert alert = new Alert();
    private Webhook webhook = new Webhook();
    private Cors cors = new Cors();

    public Jwt getJwt() { return jwt; }
    public void setJwt(Jwt jwt) { this.jwt = jwt; }
    public Alerting getAlerting() { return alerting; }
    public void setAlerting(Alerting alerting) { this.alerting = alerting; }
    public Scheduler getScheduler() { return scheduler; }
    public void setScheduler(Scheduler scheduler) { this.scheduler = scheduler; }
    public Telegram getTelegram() { return telegram; }
    public void setTelegram(Telegram telegram) { this.telegram = telegram; }
    public Resend getResend() { return resend; }
    public void setResend(Resend resend) { this.resend = resend; }
    public Alert getAlert() { return alert; }
    public void setAlert(Alert alert) { this.alert = alert; }
    public Webhook getWebhook() { return webhook; }
    public void setWebhook(Webhook webhook) { this.webhook = webhook; }
    public Cors getCors() { return cors; }
    public void setCors(Cors cors) { this.cors = cors; }

    public static class Jwt {
        private String secret = "change-me-in-production-must-be-at-least-32-chars";
        private int expirationHours = 24;
        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
        public int getExpirationHours() { return expirationHours; }
        public void setExpirationHours(int expirationHours) { this.expirationHours = expirationHours; }
    }

    public static class Alerting {
        private int consecutiveFailuresThreshold = 3;
        public int getConsecutiveFailuresThreshold() { return consecutiveFailuresThreshold; }
        public void setConsecutiveFailuresThreshold(int v) { this.consecutiveFailuresThreshold = v; }
    }

    public static class Scheduler {
        private long tickMs = 5000;
        public long getTickMs() { return tickMs; }
        public void setTickMs(long tickMs) { this.tickMs = tickMs; }
    }

    public static class Telegram {
        private String botToken = "";
        private String botUsername = "";
        public String getBotToken() { return botToken; }
        public void setBotToken(String botToken) { this.botToken = botToken; }
        public String getBotUsername() { return botUsername; }
        public void setBotUsername(String botUsername) { this.botUsername = botUsername; }
    }

    public static class Resend {
        private String apiKey = "";
        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    }

    public static class Alert {
        private String emailFrom = "CloudPulse <alerts@cloudpulse.dev>";
        private String emailTo = "";
        public String getEmailFrom() { return emailFrom; }
        public void setEmailFrom(String emailFrom) { this.emailFrom = emailFrom; }
        public String getEmailTo() { return emailTo; }
        public void setEmailTo(String emailTo) { this.emailTo = emailTo; }
    }

    public static class Webhook {
        private String url = "";
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }

    public static class Cors {
        private String allowedOrigins = "http://localhost:3000,http://localhost:5173";
        public String getAllowedOrigins() { return allowedOrigins; }
        public void setAllowedOrigins(String allowedOrigins) { this.allowedOrigins = allowedOrigins; }
        public String[] getAllowedOriginsArray() {
            return allowedOrigins.split(",");
        }
    }
}
