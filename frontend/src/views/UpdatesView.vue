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
    version: 'v1.1.0',
    date: 'May 2026',
    type: 'feature',
    title: 'Response Time Tracking & Alert Thresholds',
    summary: 'Service detail pages now show response time history alongside uptime. You can set custom alert thresholds per monitor so you\'re only paged when latency actually matters.',
    changes: [
      { type: 'new', text: 'Response time chart on the service detail page — P50 and P95 latency over the last 24 hours' },
      { type: 'new', text: 'Custom alert threshold per monitor — trigger alerts only when response time exceeds your defined limit' },
      { type: 'improvement', text: '30-day uptime percentage now shown on each service card in the dashboard' },
      { type: 'fix', text: 'Monitor status badge now updates immediately after a manual re-check without requiring a page reload' },
    ],
  },
  {
    version: 'v1.0.0',
    date: 'April 2026',
    type: 'major',
    title: 'CloudPulse Launch',
    summary: 'The initial release of CloudPulse — HTTP and HTTPS uptime monitoring with email alerts, a live dashboard, incident tracking, and public status pages.',
    changes: [
      { type: 'new', text: 'HTTP and HTTPS monitor creation with configurable check intervals (1, 3, 5, or 10 minutes)' },
      { type: 'new', text: 'Email alert notifications on downtime and recovery' },
      { type: 'new', text: 'Dashboard with per-service health status, uptime bars, and last-checked timestamps' },
      { type: 'new', text: 'Incident log — automatic incident creation on failure with open/resolved state tracking' },
      { type: 'new', text: 'Public status pages — shareable uptime page per workspace' },
      { type: 'new', text: 'Webhook integration — send alert payloads to any HTTP endpoint' },
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
  font-size: 16px;
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
  font-size: 16px;
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
