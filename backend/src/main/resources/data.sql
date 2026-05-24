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
-- 3 real, stable public endpoints — notify_email left NULL so no
-- emails fire unless ALERT_EMAIL_TO is explicitly set in .env
-- =========================================================
INSERT INTO services (id, name, url, check_interval_seconds, timeout_seconds, latency_threshold_ms, consecutive_failures, ssl_valid_until, domain_valid_until, created_at, updated_at) VALUES

('b1000000-0000-0000-0000-000000000001',
 'GitHub', 'https://github.com',
 60, 10, 2000, 0,
 now() + interval '80 days', now() + interval '200 days',
 now() - interval '30 days', now() - interval '1 hour'),

('b1000000-0000-0000-0000-000000000002',
 'Supabase', 'https://supabase.com',
 60, 10, 2000, 0,
 now() + interval '60 days', now() + interval '180 days',
 now() - interval '25 days', now() - interval '1 hour'),

('b1000000-0000-0000-0000-000000000003',
 'Vercel', 'https://vercel.com',
 60, 10, 2000, 0,
 now() + interval '45 days', now() + interval '150 days',
 now() - interval '20 days', now() - interval '1 hour');

-- =========================================================
-- HEALTH LOGS
-- =========================================================

-- GitHub — steady, 12h at 60s
INSERT INTO health_logs (service_id, status, response_time_ms, status_code, checked_at)
SELECT 'b1000000-0000-0000-0000-000000000001', 'UP',
       (120 + (random() * 80))::int, 200, t
FROM generate_series(now() - interval '12 hours', now(), interval '60 seconds') t;

-- Supabase — UP with occasional latency spikes, 12h at 60s
INSERT INTO health_logs (service_id, status, response_time_ms, status_code, checked_at)
SELECT 'b1000000-0000-0000-0000-000000000002',
       CASE WHEN random() < 0.05 THEN 'DEGRADED' ELSE 'UP' END,
       CASE WHEN random() < 0.05 THEN (2200 + (random() * 600))::int ELSE (150 + (random() * 100))::int END,
       200, t
FROM generate_series(now() - interval '12 hours', now(), interval '60 seconds') t;

-- Vercel — fast, 12h at 60s
INSERT INTO health_logs (service_id, status, response_time_ms, status_code, checked_at)
SELECT 'b1000000-0000-0000-0000-000000000003', 'UP',
       (60 + (random() * 60))::int, 200, t
FROM generate_series(now() - interval '12 hours', now(), interval '60 seconds') t;

-- =========================================================
-- ALERTS (historical, already notified and acknowledged)
-- =========================================================
INSERT INTO alerts (service_id, alert_type, message, notified, acknowledged, created_at) VALUES
('b1000000-0000-0000-0000-000000000002',
 'HIGH_LATENCY',
 'Supabase latency exceeded threshold (2634ms > 2000ms)',
 TRUE, TRUE,
 now() - interval '6 hours');

-- =========================================================
-- INCIDENTS (DOWNTIME / RECOVERY pairs — derived by IncidentService)
-- =========================================================

-- GitHub — resolved ~4 days ago, recovered in 8 min
INSERT INTO alerts (service_id, alert_type, message, notified, acknowledged, created_at) VALUES
('b1000000-0000-0000-0000-000000000001', 'DOWNTIME',
 E'GitHub is unreachable\nURL: https://github.com\nResponse time: 0ms\nError: Connection timed out after 10000ms',
 TRUE, TRUE, now() - interval '4 days');

INSERT INTO alerts (service_id, alert_type, message, notified, acknowledged, created_at) VALUES
('b1000000-0000-0000-0000-000000000001', 'RECOVERY',
 'GitHub has recovered. Response time: 143ms',
 TRUE, TRUE, now() - interval '4 days' + interval '8 minutes');

-- Vercel — resolved ~10 days ago, recovered in 23 min
INSERT INTO alerts (service_id, alert_type, message, notified, acknowledged, created_at) VALUES
('b1000000-0000-0000-0000-000000000003', 'DOWNTIME',
 E'Vercel is unreachable\nURL: https://vercel.com\nResponse time: 0ms\nError: HTTP 503 Service Unavailable',
 TRUE, TRUE, now() - interval '10 days');

INSERT INTO alerts (service_id, alert_type, message, notified, acknowledged, created_at) VALUES
('b1000000-0000-0000-0000-000000000003', 'RECOVERY',
 'Vercel has recovered. Response time: 71ms',
 TRUE, TRUE, now() - interval '10 days' + interval '23 minutes');

-- Supabase — open, started ~3 hours ago (no RECOVERY = IncidentService marks it "open")
INSERT INTO alerts (service_id, alert_type, message, notified, acknowledged, created_at) VALUES
('b1000000-0000-0000-0000-000000000002', 'DOWNTIME',
 E'Supabase is unreachable\nURL: https://supabase.com\nResponse time: 0ms\nError: HTTP 500 Internal Server Error',
 TRUE, FALSE, now() - interval '3 hours');

-- =========================================================
-- STATUS PAGE
-- =========================================================
INSERT INTO status_pages (id, name, slug, description) VALUES
('c1000000-0000-0000-0000-000000000001',
 'Demo Status', 'demo-status',
 'Live status of monitored services');

INSERT INTO status_page_services (status_page_id, service_id, display_order) VALUES
('c1000000-0000-0000-0000-000000000001', 'b1000000-0000-0000-0000-000000000001', 1),
('c1000000-0000-0000-0000-000000000001', 'b1000000-0000-0000-0000-000000000002', 2),
('c1000000-0000-0000-0000-000000000001', 'b1000000-0000-0000-0000-000000000003', 3);
