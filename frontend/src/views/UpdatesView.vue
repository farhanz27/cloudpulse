<template>
  <div class="page">
    <LandingNav />

    <section class="page-hero">
      <div class="hero-bg" aria-hidden="true"></div>
      <div class="hero-sunburst" aria-hidden="true"></div>
      <div class="hero-content">
        <div class="page-eyebrow">
          <div class="eyebrow-line" aria-hidden="true"></div>
          <span>CHANGELOG</span>
          <div class="eyebrow-line" aria-hidden="true"></div>
        </div>
        <h1 class="page-title">What's <span class="gold">New</span></h1>
        <p class="page-sub">
          Every update, improvement, and fix — logged in full detail.
          Follow along to see what the CloudPulse team ships.
        </p>
      </div>
    </section>

    <!-- Changelog -->
    <section class="changelog-section">
      <div class="changelog-inner">
        <div class="changelog-list">
          <div class="changelog-item" v-for="release in releases" :key="release.version">
            <div class="release-sidebar">
              <div class="release-version">{{ release.version }}</div>
              <div class="release-date">{{ release.date }}</div>
              <div class="release-type" :class="`release-type--${release.type}`">
                {{ release.type.toUpperCase() }}
              </div>
            </div>
            <div class="release-body">
              <h2 class="release-title">{{ release.title }}</h2>
              <p class="release-summary">{{ release.summary }}</p>
              <div class="release-changes">
                <div
                  class="change-item"
                  v-for="change in release.changes"
                  :key="change.text"
                >
                  <span class="change-tag" :class="`change-tag--${change.type}`">
                    {{ change.type }}
                  </span>
                  <span class="change-text">{{ change.text }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <LandingFooter />
  </div>
</template>

<script setup lang="ts">
import LandingNav from '@/components/landing/LandingNav.vue'
import LandingFooter from '@/components/landing/LandingFooter.vue'

const releases = [
  {
    version: 'v2.4.0',
    date: 'May 2026',
    type: 'feature',
    title: 'Discord Integration & Integrations Marketplace',
    summary: 'Discord webhook alerts are now live, and the public integrations page has been redesigned as a full marketplace view with per-integration setup guides.',
    changes: [
      { type: 'new', text: 'Discord webhook integration — send downtime and recovery alerts to any Discord channel' },
      { type: 'new', text: 'Public integrations marketplace with setup overviews and availability status' },
      { type: 'new', text: 'Coming soon labels for Slack, PagerDuty, Microsoft Teams, OpsGenie, Datadog, Grafana, and Jira' },
      { type: 'improvement', text: 'Pricing cards now maintain equal height and display hover effects' },
      { type: 'fix', text: 'Icon scale animation in the "Who it\'s for" section no longer shifts icons to the left on hover' },
    ],
  },
  {
    version: 'v2.3.0',
    date: 'April 2026',
    type: 'feature',
    title: 'Telegram Alerts & Webhook Delivery Logs',
    summary: 'Telegram bot integration is now available for all plans. Webhook delivery history is now visible in the integrations dashboard with per-event response codes and latency.',
    changes: [
      { type: 'new', text: 'Telegram bot integration — send alerts to personal chats or groups via @BotFather token' },
      { type: 'new', text: 'Webhook delivery logs — inspect response status codes, payloads, and latency per delivery' },
      { type: 'new', text: 'Webhook retry configuration — set retry count and back-off strategy per endpoint' },
      { type: 'improvement', text: 'Alert notification history now shows delivery status per channel' },
      { type: 'fix', text: 'Fixed an edge case where recovery alerts were not sent when a service recovered within a single check cycle' },
    ],
  },
  {
    version: 'v2.2.0',
    date: 'March 2026',
    type: 'feature',
    title: 'Public Status Pages — Custom Domain Support',
    summary: 'Enterprise customers can now map a custom domain to their status pages. All plans receive a refreshed status page design with real-time incident updates.',
    changes: [
      { type: 'new', text: 'Custom domain support for status pages (Enterprise plan)' },
      { type: 'new', text: 'Real-time incident timeline visible to status page visitors' },
      { type: 'new', text: 'Maintenance window scheduling — pre-announce planned downtime on your status page' },
      { type: 'improvement', text: 'Status page load time reduced by 60% via edge caching' },
      { type: 'improvement', text: 'Mobile layout for status pages fully redesigned' },
    ],
  },
  {
    version: 'v2.1.2',
    date: 'February 2026',
    type: 'fix',
    title: 'Alert Deduplication & Dashboard Performance',
    summary: 'This release addresses duplicate alert notifications and improves dashboard rendering speed for accounts with more than 50 monitored services.',
    changes: [
      { type: 'fix', text: 'Duplicate alert notifications no longer sent when multiple check regions detect the same failure simultaneously' },
      { type: 'fix', text: 'Dashboard uptime bars now render correctly for services with no check history' },
      { type: 'fix', text: 'Service detail latency chart no longer shows incorrect P95 values after timezone change' },
      { type: 'improvement', text: 'Dashboard initial load time reduced by 40% for large service lists' },
    ],
  },
  {
    version: 'v2.1.0',
    date: 'January 2026',
    type: 'feature',
    title: 'On-Call Scheduling & Escalation Rules',
    summary: 'Professional and Enterprise plans now include on-call rotation management. Define schedules, set escalation rules, and ensure the right person is always paged.',
    changes: [
      { type: 'new', text: 'On-call schedule management with weekly and custom rotation support' },
      { type: 'new', text: 'Escalation rules — automatically re-alert or escalate if an incident is not acknowledged within a threshold' },
      { type: 'new', text: 'Per-service alert routing — route alerts to different channels or on-call groups by service' },
      { type: 'new', text: 'Acknowledgement from notification — acknowledge incidents directly from Telegram messages' },
      { type: 'improvement', text: 'Alert settings UI redesigned with per-channel configuration panels' },
    ],
  },
  {
    version: 'v2.0.0',
    date: 'December 2025',
    type: 'major',
    title: 'Dashboard Redesign & Incident Management v2',
    summary: 'A full redesign of the monitoring dashboard and a rebuilt incident management workflow, bringing structured incident timelines, richer filtering, and a new black/gold visual identity throughout the app.',
    changes: [
      { type: 'new', text: 'Fully redesigned dashboard with service health cards, latency charts, and uptime history bars' },
      { type: 'new', text: 'Incident timeline view — see every check result, alert, acknowledgement, and resolution in order' },
      { type: 'new', text: 'Incident status workflow: Open → Investigating → Identified → Monitoring → Resolved' },
      { type: 'new', text: 'Service detail page with 30/60/90 day uptime trend and response latency percentiles' },
      { type: 'improvement', text: 'All app-side modals migrated to PrimeVue Dialog for consistent behaviour' },
      { type: 'improvement', text: 'Black/gold design system applied consistently across all authenticated views' },
    ],
  },
]
</script>

<style scoped>
.page {
  background: #0A0A0A;
  color: #F2F0E4;
  min-height: 100vh;
  font-family: 'Josefin Sans', 'Inter', sans-serif;
}

.page-hero {
  position: relative;
  padding: 140px 32px 100px;
  text-align: center;
  border-bottom: 1px solid rgba(212,175,55,0.15);
  overflow: hidden;
}
.hero-bg {
  position: absolute; inset: 0;
  background-image:
    repeating-linear-gradient(45deg, rgba(212,175,55,0.03) 0px, rgba(212,175,55,0.03) 1px, transparent 1px, transparent 14px),
    repeating-linear-gradient(-45deg, rgba(212,175,55,0.03) 0px, rgba(212,175,55,0.03) 1px, transparent 1px, transparent 14px);
  pointer-events: none;
}
.hero-sunburst {
  position: absolute; inset: 0;
  background: radial-gradient(ellipse at 50% 60%, rgba(212,175,55,0.08) 0%, transparent 65%);
  pointer-events: none;
}
.hero-content { position: relative; z-index: 1; max-width: 640px; margin: 0 auto; }
.page-eyebrow {
  display: flex; align-items: center; gap: 16px; justify-content: center;
  margin-bottom: 24px; font-size: 11px; font-weight: 600; letter-spacing: 0.25em; color: #D4AF37;
}
.eyebrow-line { width: 40px; height: 1px; background: rgba(212,175,55,0.4); }
.page-title {
  font-family: 'Marcellus', Georgia, serif;
  font-size: clamp(40px, 7vw, 72px);
  font-weight: 400; letter-spacing: 0.08em; color: #F2F0E4; line-height: 1.1; margin-bottom: 24px;
}
.gold { color: #D4AF37; }
.page-sub { font-size: 16px; color: #666; font-weight: 300; line-height: 1.8; max-width: 480px; margin: 0 auto; }

/* Changelog */
.changelog-section { padding: 80px 32px 100px; }
.changelog-inner { max-width: 1000px; margin: 0 auto; }
.changelog-list { display: flex; flex-direction: column; gap: 0; }

.changelog-item {
  display: grid;
  grid-template-columns: 180px 1fr;
  gap: 48px;
  padding: 56px 0;
  border-bottom: 1px solid rgba(212,175,55,0.1);
  align-items: flex-start;
}
.changelog-item:first-child { padding-top: 0; }
.changelog-item:last-child { border-bottom: none; }

.release-sidebar {
  display: flex;
  flex-direction: column;
  gap: 8px;
  position: sticky;
  top: 88px;
}
.release-version {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 20px;
  letter-spacing: 0.1em;
  color: #F2F0E4;
}
.release-date {
  font-size: 12px;
  color: #555;
  font-weight: 300;
  letter-spacing: 0.08em;
}
.release-type {
  display: inline-block;
  font-size: 9px;
  font-weight: 700;
  letter-spacing: 0.2em;
  padding: 4px 10px;
  margin-top: 4px;
}
.release-type--feature { background: rgba(212,175,55,0.1); color: #D4AF37; border: 1px solid rgba(212,175,55,0.2); }
.release-type--fix { background: rgba(34,197,94,0.08); color: #22c55e; border: 1px solid rgba(34,197,94,0.2); }
.release-type--major { background: rgba(212,175,55,0.15); color: #D4AF37; border: 1px solid rgba(212,175,55,0.4); }
.release-type--improvement { background: rgba(99,102,241,0.1); color: #818cf8; border: 1px solid rgba(99,102,241,0.2); }

.release-body { display: flex; flex-direction: column; gap: 16px; }
.release-title {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 22px;
  letter-spacing: 0.05em;
  color: #F2F0E4;
  line-height: 1.3;
}
.release-summary {
  font-size: 14px;
  color: #777;
  line-height: 1.8;
  font-weight: 300;
}

.release-changes {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding-top: 8px;
  border-top: 1px solid rgba(212,175,55,0.1);
  margin-top: 4px;
}
.change-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  font-size: 13px;
  color: #888;
  font-weight: 300;
  line-height: 1.6;
}
.change-tag {
  font-size: 9px;
  font-weight: 700;
  letter-spacing: 0.15em;
  padding: 3px 8px;
  flex-shrink: 0;
  margin-top: 1px;
}
.change-tag--new { background: rgba(212,175,55,0.1); color: #D4AF37; }
.change-tag--fix { background: rgba(34,197,94,0.08); color: #22c55e; }
.change-tag--improvement { background: rgba(99,102,241,0.1); color: #818cf8; }

@media (max-width: 768px) {
  .changelog-item { grid-template-columns: 1fr; gap: 20px; }
  .release-sidebar { position: static; flex-direction: row; align-items: center; gap: 12px; flex-wrap: wrap; }
  .release-type { margin-top: 0; }
  .changelog-section { padding: 56px 20px 80px; }
  .page-hero { padding: 120px 20px 72px; }
}
</style>
