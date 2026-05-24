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
