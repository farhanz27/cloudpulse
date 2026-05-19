-- CloudPulse schema
-- Run once on a fresh database. Migrations are documented as ALTER TABLE comments below.

CREATE TABLE IF NOT EXISTS users (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    hashed_password VARCHAR(255) NOT NULL,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS services (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name                    VARCHAR(255) NOT NULL,
    url                     VARCHAR(2048) NOT NULL,
    check_interval_seconds  INT NOT NULL DEFAULT 60,
    timeout_seconds         INT NOT NULL DEFAULT 10,
    expected_status_code    INT NOT NULL DEFAULT 200,
    is_active               BOOLEAN NOT NULL DEFAULT TRUE,
    keep_alive              BOOLEAN NOT NULL DEFAULT FALSE,
    latency_threshold_ms    INT NOT NULL DEFAULT 5000,
    notify_email            VARCHAR(255),
    ssl_valid_until         TIMESTAMPTZ,
    domain_valid_until      TIMESTAMPTZ,
    muted_until             TIMESTAMPTZ,
    consecutive_failures    INT NOT NULL DEFAULT 0,
    region                  VARCHAR(255),
    created_at              TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS integrations (
    id          SERIAL PRIMARY KEY,
    type        VARCHAR(50) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    config      JSONB NOT NULL DEFAULT '{}',
    enabled     BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS monitor_integrations (
    monitor_id      UUID REFERENCES services(id) ON DELETE CASCADE,
    integration_id  INT  REFERENCES integrations(id) ON DELETE CASCADE,
    PRIMARY KEY (monitor_id, integration_id)
);

CREATE TABLE IF NOT EXISTS health_logs (
    id              BIGSERIAL PRIMARY KEY,
    service_id      UUID NOT NULL REFERENCES services(id) ON DELETE CASCADE,
    status          VARCHAR(10) NOT NULL,
    response_time_ms FLOAT,
    status_code     INT,
    error_message   VARCHAR(1024),
    checked_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_health_logs_service_time ON health_logs (service_id, checked_at);

CREATE TABLE IF NOT EXISTS alerts (
    id           SERIAL PRIMARY KEY,
    service_id   UUID NOT NULL REFERENCES services(id) ON DELETE CASCADE,
    alert_type   VARCHAR(50) NOT NULL,
    message      VARCHAR(2048) NOT NULL,
    notified     BOOLEAN NOT NULL DEFAULT FALSE,
    acknowledged BOOLEAN NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS status_pages (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255) NOT NULL,
    slug        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(1024),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS status_page_services (
    status_page_id  UUID REFERENCES status_pages(id) ON DELETE CASCADE,
    service_id      UUID REFERENCES services(id) ON DELETE CASCADE,
    display_order   INT NOT NULL DEFAULT 0,
    PRIMARY KEY (status_page_id, service_id)
);

CREATE TABLE IF NOT EXISTS telegram_pending_links (
    id              SERIAL PRIMARY KEY,
    code            VARCHAR(64) NOT NULL UNIQUE,
    chat_id         BIGINT,
    integration_id  INT REFERENCES integrations(id),
    expires_at      TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_telegram_pending_links_code ON telegram_pending_links (code);
