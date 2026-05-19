-- CloudPulse seed data
-- Login: demo@cloudpulse.dev / demo123

-- =========================================================
-- USER
-- =========================================================
INSERT INTO users (id, name, email, hashed_password, is_active) VALUES
('a1000000-0000-0000-0000-000000000001', 'Demo User', 'demo@cloudpulse.dev',
 '$2b$10$hjBl5KyIJ5HhhKCeD2ABjuMG4nWgOgIKSZwNS6nLWkhQwacKi9pDK', TRUE);

-- =========================================================
-- MONITORS
-- =========================================================
INSERT INTO services (id, name, url, check_interval_seconds, timeout_seconds, latency_threshold_ms, consecutive_failures, ssl_valid_until, domain_valid_until, created_at, updated_at) VALUES

('b1000000-0000-0000-0000-000000000001',
 'Acme Website', 'https://acme-corp.example.com',
 60, 10, 3000, 0,
 now() + interval '80 days', now() + interval '200 days',
 now() - interval '30 days', now() - interval '1 hour'),

('b1000000-0000-0000-0000-000000000002',
 'API Gateway', 'https://api.acme-corp.example.com',
 30, 10, 2000, 0,
 now() + interval '60 days', now() + interval '180 days',
 now() - interval '25 days', now() - interval '1 hour'),

('b1000000-0000-0000-0000-000000000003',
 'Payments Service', 'https://payments.acme-corp.example.com',
 60, 10, 3000, 5,
 now() + interval '45 days', now() + interval '150 days',
 now() - interval '20 days', now() - interval '2 hours'),

('b1000000-0000-0000-0000-000000000004',
 'CDN Edge', 'https://cdn.acme-corp.example.com',
 300, 10, 1000, 0,
 now() + interval '90 days', now() + interval '210 days',
 now() - interval '15 days', now() - interval '1 hour'),

('b1000000-0000-0000-0000-000000000005',
 'Admin Portal', 'https://admin.acme-corp.example.com',
 120, 10, 3000, 0,
 now() + interval '18 days', now() + interval '160 days',
 now() - interval '10 days', now() - interval '1 hour');

-- =========================================================
-- HEALTH LOGS
-- =========================================================

-- Acme Website — UP, steady latency, 12h at 60s
INSERT INTO health_logs (service_id, status, response_time_ms, status_code, checked_at)
SELECT 'b1000000-0000-0000-0000-000000000001', 'UP',
       (120 + (random() * 80))::int, 200, t
FROM generate_series(now() - interval '12 hours', now(), interval '60 seconds') t;

-- API Gateway — UP but occasional high-latency spikes, 12h at 30s
INSERT INTO health_logs (service_id, status, response_time_ms, status_code, checked_at)
SELECT 'b1000000-0000-0000-0000-000000000002',
       CASE WHEN random() < 0.05 THEN 'DEGRADED' ELSE 'UP' END,
       CASE WHEN random() < 0.05 THEN (2500 + (random() * 1500))::int ELSE (180 + (random() * 120))::int END,
       200, t
FROM generate_series(now() - interval '12 hours', now(), interval '30 seconds') t;

-- Payments Service — UP for first 10h, DOWN for last 2h
INSERT INTO health_logs (service_id, status, response_time_ms, status_code, checked_at)
SELECT 'b1000000-0000-0000-0000-000000000003', 'UP',
       (200 + (random() * 100))::int, 200, t
FROM generate_series(now() - interval '12 hours', now() - interval '2 hours', interval '60 seconds') t;

INSERT INTO health_logs (service_id, status, response_time_ms, status_code, error_message, checked_at)
SELECT 'b1000000-0000-0000-0000-000000000003', 'DOWN',
       NULL, NULL, 'Connection refused', t
FROM generate_series(now() - interval '2 hours', now(), interval '60 seconds') t;

-- CDN Edge — UP, very fast, 12h at 300s
INSERT INTO health_logs (service_id, status, response_time_ms, status_code, checked_at)
SELECT 'b1000000-0000-0000-0000-000000000004', 'UP',
       (20 + (random() * 40))::int, 200, t
FROM generate_series(now() - interval '12 hours', now(), interval '300 seconds') t;

-- Admin Portal — UP, 12h at 120s
INSERT INTO health_logs (service_id, status, response_time_ms, status_code, checked_at)
SELECT 'b1000000-0000-0000-0000-000000000005', 'UP',
       (250 + (random() * 150))::int, 200, t
FROM generate_series(now() - interval '12 hours', now(), interval '120 seconds') t;

-- =========================================================
-- ALERTS
-- =========================================================
INSERT INTO alerts (service_id, alert_type, message, notified, acknowledged, created_at) VALUES

('b1000000-0000-0000-0000-000000000003',
 'DOWNTIME',
 'Payments Service is DOWN — connection refused (5 consecutive failures)',
 TRUE, FALSE,
 now() - interval '2 hours'),

('b1000000-0000-0000-0000-000000000002',
 'HIGH_LATENCY',
 'API Gateway latency exceeded threshold (2847ms > 2000ms)',
 TRUE, TRUE,
 now() - interval '8 hours');

-- =========================================================
-- STATUS PAGE
-- =========================================================
INSERT INTO status_pages (id, name, slug, description) VALUES
('c1000000-0000-0000-0000-000000000001',
 'Acme Platform Status', 'acme-status',
 'Live status of Acme Corp services');

INSERT INTO status_page_services (status_page_id, service_id, display_order) VALUES
('c1000000-0000-0000-0000-000000000001', 'b1000000-0000-0000-0000-000000000001', 1),
('c1000000-0000-0000-0000-000000000001', 'b1000000-0000-0000-0000-000000000002', 2),
('c1000000-0000-0000-0000-000000000001', 'b1000000-0000-0000-0000-000000000003', 3),
('c1000000-0000-0000-0000-000000000001', 'b1000000-0000-0000-0000-000000000004', 4);
