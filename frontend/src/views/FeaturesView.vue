<template>
  <div class="page">
    <LandingNav />

    <!-- Hero -->
    <section class="page-hero">
      <div class="hero-bg" aria-hidden="true"></div>
      <div class="hero-sunburst" aria-hidden="true"></div>
      <div class="hero-content">
        <div class="page-eyebrow">
          <div class="eyebrow-line" aria-hidden="true"></div>
          <span>PLATFORM CAPABILITIES</span>
          <div class="eyebrow-line" aria-hidden="true"></div>
        </div>
        <h1 class="page-title">Everything You Need to<br><span class="gold">Stay Online</span></h1>
        <p class="page-sub">
          From sub-second outage detection to structured incident workflows and public status pages —
          CloudPulse gives your team the full picture, all in one place.
        </p>
        <div class="hero-ctas">
          <router-link to="/sign-up" class="btn-primary">START FREE</router-link>
          <router-link to="/pricing" class="btn-outline">SEE PRICING</router-link>
        </div>
      </div>
    </section>

    <!-- Feature Overview Tiles -->
    <section class="overview-section">
      <div class="overview-inner">
        <div class="overview-tile" v-for="tile in overviewTiles" :key="tile.title">
          <div class="tile-icon" v-html="tile.icon" aria-hidden="true"></div>
          <div class="tile-title">{{ tile.title }}</div>
          <div class="tile-desc">{{ tile.desc }}</div>
        </div>
      </div>
    </section>

    <!-- Feature: Uptime Monitoring -->
    <section class="feature-section feature-section--dark">
      <div class="feature-inner">
        <div class="feature-text">
          <div class="feature-eyebrow">01 — UPTIME MONITORING</div>
          <h2 class="feature-heading">Know Before<br><span class="gold">Your Users Do</span></h2>
          <p class="feature-desc">
            CloudPulse runs continuous checks from 15+ global regions simultaneously. A failure is only
            confirmed when multiple regions agree — eliminating the false positives that destroy on-call
            trust over time.
          </p>
          <ul class="feature-list">
            <li v-for="item in monitoringFeatures" :key="item">
              <span class="list-dot" aria-hidden="true">◆</span>{{ item }}
            </li>
          </ul>
        </div>
        <div class="feature-visual">
          <div class="visual-card">
            <div class="visual-header">
              <span class="visual-label">LIVE SERVICES</span>
              <span class="visual-status-dot visual-status-dot--green" aria-hidden="true"></span>
            </div>
            <div class="visual-service-list">
              <div class="visual-service" v-for="svc in demoServices" :key="svc.name">
                <div class="vs-left">
                  <div class="vs-indicator" :class="`vs-indicator--${svc.status}`" aria-hidden="true"></div>
                  <span class="vs-name">{{ svc.name }}</span>
                </div>
                <div class="vs-right">
                  <span class="vs-uptime">{{ svc.uptime }}</span>
                  <span class="vs-latency">{{ svc.latency }}</span>
                </div>
              </div>
            </div>
            <div class="visual-bars">
              <div class="vbar" v-for="(h, i) in uptimeBars" :key="i" :class="h ? 'vbar--up' : 'vbar--down'" :title="h ? 'UP' : 'DOWN'"></div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Feature: Instant Alerts -->
    <section class="feature-section">
      <div class="feature-inner feature-inner--reverse">
        <div class="feature-text">
          <div class="feature-eyebrow">02 — INSTANT ALERTS</div>
          <h2 class="feature-heading">Right Person,<br><span class="gold">Right Channel</span></h2>
          <p class="feature-desc">
            The moment a failure is confirmed, CloudPulse fires alerts across every channel your team uses.
            No polling, no delay — just immediate, actionable notifications with full incident context.
          </p>
          <ul class="feature-list">
            <li v-for="item in alertFeatures" :key="item">
              <span class="list-dot" aria-hidden="true">◆</span>{{ item }}
            </li>
          </ul>
        </div>
        <div class="feature-visual">
          <div class="visual-card">
            <div class="visual-header">
              <span class="visual-label">ALERT CHANNELS</span>
            </div>
            <div class="alert-channels">
              <div class="alert-channel" v-for="ch in alertChannels" :key="ch.name">
                <div class="ac-icon" v-html="ch.icon" aria-hidden="true"></div>
                <div class="ac-info">
                  <div class="ac-name">{{ ch.name }}</div>
                  <div class="ac-status" :class="`ac-status--${ch.status}`">{{ ch.statusLabel }}</div>
                </div>
                <div class="ac-badge" :class="`ac-badge--${ch.badge}`">{{ ch.badge }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Feature: Incident Management -->
    <section class="feature-section feature-section--dark">
      <div class="feature-inner">
        <div class="feature-text">
          <div class="feature-eyebrow">03 — INCIDENT MANAGEMENT</div>
          <h2 class="feature-heading">From Alert to<br><span class="gold">Resolution</span></h2>
          <p class="feature-desc">
            Every outage becomes a structured incident with a full timeline, ownership assignment, and
            status workflow. Your team stays coordinated and your stakeholders stay informed — without
            a single manual status update.
          </p>
          <ul class="feature-list">
            <li v-for="item in incidentFeatures" :key="item">
              <span class="list-dot" aria-hidden="true">◆</span>{{ item }}
            </li>
          </ul>
        </div>
        <div class="feature-visual">
          <div class="visual-card">
            <div class="visual-header">
              <span class="visual-label">INCIDENT TIMELINE</span>
              <span class="visual-badge visual-badge--red">OPEN</span>
            </div>
            <div class="incident-timeline">
              <div class="it-event" v-for="ev in incidentTimeline" :key="ev.time">
                <div class="it-dot" :class="`it-dot--${ev.type}`" aria-hidden="true"></div>
                <div class="it-body">
                  <div class="it-time">{{ ev.time }}</div>
                  <div class="it-text">{{ ev.text }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Feature: Status Pages -->
    <section class="feature-section">
      <div class="feature-inner feature-inner--reverse">
        <div class="feature-text">
          <div class="feature-eyebrow">04 — PUBLIC STATUS PAGES</div>
          <h2 class="feature-heading">Keep Customers<br><span class="gold">In the Loop</span></h2>
          <p class="feature-desc">
            A branded, real-time status page gives your customers and stakeholders a single source of truth
            during incidents. Reduce support tickets by communicating proactively — not reactively.
          </p>
          <ul class="feature-list">
            <li v-for="item in statusPageFeatures" :key="item">
              <span class="list-dot" aria-hidden="true">◆</span>{{ item }}
            </li>
          </ul>
        </div>
        <div class="feature-visual">
          <div class="visual-card visual-card--status">
            <div class="sp-header">
              <div class="sp-logo-row">
                <div class="sp-logo-placeholder" aria-hidden="true"></div>
                <span class="sp-brand">YourApp Status</span>
              </div>
              <div class="sp-overall">
                <span class="sp-dot sp-dot--green" aria-hidden="true"></span>
                <span class="sp-overall-text">All Systems Operational</span>
              </div>
            </div>
            <div class="sp-services">
              <div class="sp-svc" v-for="s in statusPageDemo" :key="s.name">
                <span class="sp-svc-name">{{ s.name }}</span>
                <span class="sp-svc-status" :class="`sp-svc-status--${s.status}`">{{ s.label }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Feature: Analytics -->
    <section class="feature-section feature-section--dark">
      <div class="feature-inner">
        <div class="feature-text">
          <div class="feature-eyebrow">05 — ANALYTICS & REPORTING</div>
          <h2 class="feature-heading">Data to Drive<br><span class="gold">Reliability Decisions</span></h2>
          <p class="feature-desc">
            Historical uptime charts, P95 latency trends, SLA compliance tracking, and incident frequency
            analysis — everything you need to understand your service health over time and make the case
            for reliability investments.
          </p>
          <ul class="feature-list">
            <li v-for="item in analyticsFeatures" :key="item">
              <span class="list-dot" aria-hidden="true">◆</span>{{ item }}
            </li>
          </ul>
        </div>
        <div class="feature-visual">
          <div class="visual-card">
            <div class="visual-header">
              <span class="visual-label">30-DAY UPTIME REPORT</span>
            </div>
            <div class="analytics-stats">
              <div class="an-stat" v-for="s in analyticsStats" :key="s.label">
                <div class="an-value">{{ s.value }}</div>
                <div class="an-label">{{ s.label }}</div>
              </div>
            </div>
            <div class="chart-placeholder">
              <div class="chart-bar" v-for="(h, i) in chartBars" :key="i" :style="{ height: h + '%' }"></div>
            </div>
            <div class="chart-x-labels">
              <span>30d</span><span>21d</span><span>14d</span><span>7d</span><span>Now</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Check Types -->
    <section class="check-types-section">
      <div class="check-types-inner">
        <div class="section-label centered">MONITORING PROTOCOLS</div>
        <h2 class="section-heading centered">Monitor Any <span class="gold">Endpoint</span></h2>
        <div class="check-types-grid">
          <div class="check-type-card" v-for="ct in checkTypes" :key="ct.title">
            <div class="ct-icon" v-html="ct.icon" aria-hidden="true"></div>
            <h3 class="ct-title">{{ ct.title }}</h3>
            <p class="ct-desc">{{ ct.desc }}</p>
            <div class="ct-tags">
              <span class="ct-tag" v-for="tag in ct.tags" :key="tag">{{ tag }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Final CTA -->
    <section class="cta-section">
      <div class="cta-inner">
        <h2 class="cta-title">Ready to Stop <span class="gold">Flying Blind?</span></h2>
        <p class="cta-desc">Start monitoring your first service in under 5 minutes — free, no card required.</p>
        <div class="cta-buttons">
          <router-link to="/sign-up" class="btn-primary">BEGIN MONITORING FREE</router-link>
          <router-link to="/pricing" class="btn-outline">VIEW PRICING</router-link>
        </div>
      </div>
    </section>

    <LandingFooter />
  </div>
</template>

<script setup lang="ts">
import LandingNav from '@/components/landing/LandingNav.vue'
import LandingFooter from '@/components/landing/LandingFooter.vue'

const overviewTiles = [
  {
    title: 'UPTIME MONITORING',
    desc: 'HTTP, TCP, DNS checks every 30 seconds from 15+ global regions',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><polyline points="22,12 18,12 15,21 9,3 6,12 2,12"/></svg>`,
  },
  {
    title: 'INSTANT ALERTS',
    desc: 'Email, Discord, Telegram, and webhooks fire the moment a failure is confirmed',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9M13.73 21a2 2 0 01-3.46 0"/></svg>`,
  },
  {
    title: 'INCIDENT MANAGEMENT',
    desc: 'Structured workflows, timelines, escalation rules, and on-call scheduling',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><path d="M12 9v4M12 17h.01"/><path d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"/></svg>`,
  },
  {
    title: 'STATUS PAGES',
    desc: 'Branded public status pages with real-time updates and custom domain support',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/><polyline points="9,22 9,12 15,12 15,22"/></svg>`,
  },
  {
    title: 'ANALYTICS',
    desc: 'Historical uptime charts, P95 latency trends, and SLA compliance reports',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><line x1="18" y1="20" x2="18" y2="10"/><line x1="12" y1="20" x2="12" y2="4"/><line x1="6" y1="20" x2="6" y2="14"/></svg>`,
  },
  {
    title: 'SSL MONITORING',
    desc: 'Automatic certificate expiry tracking with advance warning alerts',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>`,
  },
]

const monitoringFeatures = [
  'HTTP, TCP, and DNS endpoint checks',
  'Configurable check intervals from 30 seconds to 5 minutes',
  'Multi-region confirmation eliminates false positives',
  'Custom request headers and expected response validation',
  'Response time tracking with P50, P95, and P99 percentiles',
  'SSL certificate expiry monitoring with advance alerts',
]

const demoServices = [
  { name: 'Production API', status: 'up', uptime: '99.98%', latency: '142ms' },
  { name: 'Auth Service', status: 'up', uptime: '100%', latency: '89ms' },
  { name: 'CDN / Assets', status: 'degraded', uptime: '99.1%', latency: '340ms' },
  { name: 'Database Cluster', status: 'up', uptime: '99.99%', latency: '12ms' },
]

const uptimeBars = Array.from({ length: 48 }, (_, i) =>
  i !== 11 && i !== 12 && i !== 31 ? true : false
)

const alertFeatures = [
  'Email alerts with HTML-formatted incident details',
  'Discord webhook integration for team channels',
  'Telegram bot alerts to personal chats or groups',
  'Configurable webhooks with HMAC-SHA256 signing',
  'Alert escalation — re-notify or escalate if not acknowledged',
  'Per-service routing — different channels for different services',
]

const alertChannels = [
  {
    name: 'Email',
    statusLabel: 'Connected',
    status: 'active',
    badge: 'available',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="18" height="18" style="color:#D4AF37"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></svg>`,
  },
  {
    name: 'Discord',
    statusLabel: 'Connected',
    status: 'active',
    badge: 'available',
    icon: `<svg viewBox="0 0 24 24" fill="currentColor" width="18" height="18" style="color:#5865F2"><path d="M20.317 4.37a19.791 19.791 0 00-4.885-1.515.074.074 0 00-.079.037c-.21.375-.444.864-.608 1.25a18.27 18.27 0 00-5.487 0 12.64 12.64 0 00-.617-1.25.077.077 0 00-.079-.037A19.736 19.736 0 003.677 4.37a.07.07 0 00-.032.027C.533 9.046-.32 13.58.099 18.057a.082.082 0 00.031.057 19.9 19.9 0 005.993 3.03.078.078 0 00.084-.028c.462-.63.874-1.295 1.226-1.994a.076.076 0 00-.041-.106 13.107 13.107 0 01-1.872-.892.077.077 0 01-.008-.128 10.2 10.2 0 00.372-.292.074.074 0 01.077-.01c3.928 1.793 8.18 1.793 12.062 0a.074.074 0 01.078.01c.12.098.246.198.373.292a.077.077 0 01-.006.127 12.299 12.299 0 01-1.873.892.077.077 0 00-.041.107c.36.698.772 1.362 1.225 1.993a.076.076 0 00.084.028 19.839 19.839 0 006.002-3.03.077.077 0 00.032-.054c.5-5.177-.838-9.674-3.549-13.66a.061.061 0 00-.031-.03zM8.02 15.33c-1.183 0-2.157-1.085-2.157-2.419 0-1.333.956-2.419 2.157-2.419 1.21 0 2.176 1.096 2.157 2.42 0 1.333-.956 2.418-2.157 2.418zm7.975 0c-1.183 0-2.157-1.085-2.157-2.419 0-1.333.955-2.419 2.157-2.419 1.21 0 2.176 1.096 2.157 2.42 0 1.333-.946 2.418-2.157 2.418z"/></svg>`,
  },
  {
    name: 'Telegram',
    statusLabel: 'Connected',
    status: 'active',
    badge: 'available',
    icon: `<svg viewBox="0 0 24 24" fill="currentColor" width="18" height="18" style="color:#0088CC"><path d="M11.944 0A12 12 0 000 12a12 12 0 0012 12 12 12 0 0012-12A12 12 0 0012 0a12 12 0 00-.056 0zm4.962 7.224c.1-.002.321.023.465.14a.506.506 0 01.171.325c.016.093.036.306.02.472-.18 1.898-.962 6.502-1.36 8.627-.168.9-.499 1.201-.82 1.23-.696.065-1.225-.46-1.9-.902-1.056-.693-1.653-1.124-2.678-1.8-1.185-.78-.417-1.21.258-1.91.177-.184 3.247-2.977 3.307-3.23.007-.032.014-.15-.056-.212s-.174-.041-.249-.024c-.106.024-1.793 1.14-5.061 3.345-.48.33-.913.49-1.302.48-.428-.008-1.252-.241-1.865-.44-.752-.245-1.349-.374-1.297-.789.027-.216.325-.437.893-.663 3.498-1.524 5.83-2.529 6.998-3.014 3.332-1.386 4.025-1.627 4.476-1.635z"/></svg>`,
  },
  {
    name: 'Webhook',
    statusLabel: 'Connected',
    status: 'active',
    badge: 'available',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="18" height="18" style="color:#D4AF37"><path d="M10 13a5 5 0 007.54.54l3-3a5 5 0 00-7.07-7.07l-1.72 1.71"/><path d="M14 11a5 5 0 00-7.54-.54l-3 3a5 5 0 007.07 7.07l1.71-1.71"/></svg>`,
  },
]

const incidentFeatures = [
  'Auto-open incidents on downtime, auto-resolve on recovery',
  'Full event timeline: checks, alerts, acknowledgements, updates',
  'Incident status workflow: Open → Investigating → Resolved',
  'On-call scheduling with weekly and custom rotations',
  'Escalation rules — re-alert after unacknowledged timeout',
  'Post-incident reports with full timeline export',
]

const incidentTimeline = [
  { time: '14:03:12', text: 'Production API DOWN — confirmed by 4 regions', type: 'error' },
  { time: '14:03:13', text: 'Alert sent via Email, Discord, Telegram', type: 'alert' },
  { time: '14:05:44', text: 'Acknowledged by @alex.kim', type: 'ack' },
  { time: '14:06:11', text: 'Status updated to Investigating', type: 'update' },
  { time: '14:19:03', text: 'Production API UP — recovery confirmed', type: 'resolved' },
]

const statusPageFeatures = [
  'Branded public status page with your logo and domain',
  'Real-time service status — updates automatically as checks run',
  'Incident announcements with live timeline visible to visitors',
  'Maintenance window scheduling with advance notice',
  'Embeddable status widget for your own website or dashboard',
  'Custom domain support on Enterprise plan',
]

const statusPageDemo = [
  { name: 'API', status: 'up', label: 'Operational' },
  { name: 'Dashboard', status: 'up', label: 'Operational' },
  { name: 'Authentication', status: 'up', label: 'Operational' },
  { name: 'Webhooks', status: 'degraded', label: 'Degraded Performance' },
]

const analyticsFeatures = [
  '30, 60, and 90-day uptime history per service',
  'Response latency percentiles — P50, P95, P99',
  'SLA compliance tracking against configurable targets',
  'Incident frequency and MTTR (mean time to recovery) trends',
  'Exportable reports in CSV and PDF format',
  'Uptime comparison across time periods',
]

const analyticsStats = [
  { value: '99.97%', label: 'UPTIME (30D)' },
  { value: '142ms', label: 'AVG LATENCY' },
  { value: '2', label: 'INCIDENTS' },
  { value: '8m 14s', label: 'AVG MTTR' },
]

const chartBars = [62, 71, 68, 75, 80, 78, 85, 82, 88, 91, 72, 70, 85, 90, 88, 92, 95, 93, 88, 91]

const checkTypes = [
  {
    title: 'HTTP / HTTPS',
    desc: 'Monitor any web endpoint with configurable expected status codes, response body matching, and custom request headers. Supports GET, POST, HEAD, and OPTIONS.',
    tags: ['Status code', 'Body match', 'Custom headers', 'Redirect follow'],
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22"><circle cx="12" cy="12" r="10"/><line x1="2" y1="12" x2="22" y2="12"/><path d="M12 2a15.3 15.3 0 014 10 15.3 15.3 0 01-4 10 15.3 15.3 0 01-4-10 15.3 15.3 0 014-10z"/></svg>`,
  },
  {
    title: 'TCP',
    desc: 'Verify that a TCP port is open and accepting connections. Ideal for monitoring databases, message queues, and internal services that do not expose HTTP.',
    tags: ['Port open', 'Connection time', 'Any protocol'],
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22"><rect x="2" y="2" width="20" height="8" rx="2" ry="2"/><rect x="2" y="14" width="20" height="8" rx="2" ry="2"/><line x1="6" y1="6" x2="6.01" y2="6"/><line x1="6" y1="18" x2="6.01" y2="18"/></svg>`,
  },
  {
    title: 'DNS',
    desc: 'Check that a domain resolves to expected IP addresses and that your DNS records are correct. Detect hijacking, misconfiguration, or propagation failures instantly.',
    tags: ['A / AAAA', 'CNAME', 'MX', 'Expected IP'],
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z"/><circle cx="12" cy="10" r="3"/></svg>`,
  },
  {
    title: 'SSL / TLS',
    desc: 'Track certificate validity and receive early warning alerts before your certificate expires. Supports custom alert thresholds — 60, 30, 14, and 7 days before expiry.',
    tags: ['Expiry warning', 'Chain validation', 'Hostname match'],
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>`,
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

/* Hero */
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
.hero-content { position: relative; z-index: 1; max-width: 760px; margin: 0 auto; }
.page-eyebrow {
  display: flex; align-items: center; gap: 16px; justify-content: center;
  margin-bottom: 24px; font-size: 11px; font-weight: 600; letter-spacing: 0.25em; color: #D4AF37;
}
.eyebrow-line { width: 40px; height: 1px; background: rgba(212,175,55,0.4); }
.page-title {
  font-family: 'Marcellus', Georgia, serif;
  font-size: clamp(36px, 6vw, 68px);
  font-weight: 400; letter-spacing: 0.06em; color: #F2F0E4; line-height: 1.1; margin-bottom: 20px;
}
.gold { color: #D4AF37; }
.page-sub { font-size: 16px; color: #666; font-weight: 300; line-height: 1.8; max-width: 580px; margin: 0 auto 36px; }
.hero-ctas { display: flex; align-items: center; justify-content: center; gap: 16px; flex-wrap: wrap; }

/* Overview tiles */
.overview-section {
  background: #141414;
  border-bottom: 1px solid rgba(212,175,55,0.1);
}
.overview-inner {
  max-width: 1200px; margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(6, 1fr);
}
.overview-tile {
  padding: 36px 24px;
  border-right: 1px solid rgba(212,175,55,0.1);
  display: flex; flex-direction: column; gap: 10px;
  transition: background 0.3s;
}
.overview-tile:last-child { border-right: none; }
.overview-tile:hover { background: rgba(212,175,55,0.03); }
.tile-icon { color: #D4AF37; display: flex; }
.tile-title { font-size: 11px; font-weight: 600; letter-spacing: 0.15em; color: #F2F0E4; }
.tile-desc { font-size: 16px; color: #555; font-weight: 300; line-height: 1.6; }

/* Feature sections */
.feature-section { padding: 100px 32px; border-bottom: 1px solid rgba(212,175,55,0.1); }
.feature-section--dark { background: #0E0E0E; }

.feature-inner {
  max-width: 1100px; margin: 0 auto;
  display: grid; grid-template-columns: 1fr 1fr; gap: 80px; align-items: center;
}
.feature-inner--reverse .feature-text { order: 2; }
.feature-inner--reverse .feature-visual { order: 1; }

.feature-eyebrow {
  font-size: 10px; font-weight: 600; letter-spacing: 0.3em; color: #D4AF37; margin-bottom: 16px;
}
.feature-heading {
  font-family: 'Marcellus', Georgia, serif;
  font-size: clamp(28px, 4vw, 48px);
  font-weight: 400; color: #F2F0E4; letter-spacing: 0.05em; line-height: 1.2; margin-bottom: 20px;
}
.feature-desc { font-size: 16px; color: #777; line-height: 1.85; font-weight: 300; margin-bottom: 24px; }
.feature-list { list-style: none; padding: 0; margin: 0; display: flex; flex-direction: column; gap: 10px; }
.feature-list li {
  display: flex; align-items: flex-start; gap: 10px;
  font-size: 16px; color: #888; font-weight: 300; line-height: 1.5;
}
.list-dot { color: #D4AF37; font-size: 6px; flex-shrink: 0; margin-top: 5px; }

/* Visual cards */
.feature-visual { display: flex; align-items: center; justify-content: center; }
.visual-card {
  background: #141414; border: 1px solid rgba(212,175,55,0.2);
  width: 100%; max-width: 420px; padding: 0; overflow: hidden;
}
.visual-header {
  display: flex; align-items: center; justify-content: space-between;
  padding: 14px 20px; border-bottom: 1px solid rgba(212,175,55,0.1);
  background: rgba(212,175,55,0.03);
}
.visual-label { font-size: 9px; font-weight: 700; letter-spacing: 0.25em; color: #D4AF37; }
.visual-status-dot--green {
  width: 8px; height: 8px; border-radius: 50%; background: #22c55e;
  box-shadow: 0 0 6px rgba(34,197,94,0.5);
}
.visual-badge { font-size: 9px; font-weight: 700; letter-spacing: 0.15em; padding: 3px 8px; }
.visual-badge--red { background: rgba(239,68,68,0.1); color: #ef4444; border: 1px solid rgba(239,68,68,0.2); }

/* Services list in visual */
.visual-service-list { padding: 12px 20px; display: flex; flex-direction: column; gap: 10px; }
.visual-service {
  display: flex; align-items: center; justify-content: space-between;
  font-size: 12px;
}
.vs-left { display: flex; align-items: center; gap: 10px; }
.vs-indicator {
  width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0;
}
.vs-indicator--up { background: #22c55e; }
.vs-indicator--degraded { background: #f59e0b; }
.vs-indicator--down { background: #ef4444; }
.vs-name { color: #ccc; font-weight: 300; }
.vs-right { display: flex; gap: 16px; }
.vs-uptime { color: #666; font-size: 11px; }
.vs-latency { color: #555; font-size: 11px; font-family: 'Courier New', monospace; }

.visual-bars {
  display: flex; gap: 2px; padding: 12px 20px 16px;
  border-top: 1px solid rgba(212,175,55,0.08);
}
.vbar { height: 16px; flex: 1; border-radius: 1px; }
.vbar--up { background: rgba(34,197,94,0.5); }
.vbar--down { background: rgba(239,68,68,0.6); }

/* Alert channels */
.alert-channels { padding: 12px 20px; display: flex; flex-direction: column; gap: 0; }
.alert-channel {
  display: flex; align-items: center; gap: 14px;
  padding: 12px 0; border-bottom: 1px solid rgba(212,175,55,0.07);
}
.alert-channel:last-child { border-bottom: none; }
.ac-icon { display: flex; flex-shrink: 0; width: 32px; height: 32px; align-items: center; justify-content: center;
  background: rgba(212,175,55,0.05); border: 1px solid rgba(212,175,55,0.1);
}
.ac-info { flex: 1; }
.ac-name { font-size: 13px; color: #F2F0E4; font-weight: 300; margin-bottom: 2px; }
.ac-status { font-size: 10px; letter-spacing: 0.1em; }
.ac-status--active { color: #22c55e; }
.ac-badge {
  font-size: 9px; font-weight: 700; letter-spacing: 0.12em; padding: 3px 8px;
  background: rgba(34,197,94,0.08); color: #22c55e; border: 1px solid rgba(34,197,94,0.2);
}

/* Incident timeline */
.incident-timeline { padding: 16px 20px; display: flex; flex-direction: column; gap: 0; position: relative; }
.it-event:not(:last-child)::after {
  content: ''; position: absolute; left: 4px; width: 1px;
  top: 18px; bottom: -18px;
  background: rgba(212,175,55,0.1);
}
.it-event {
  display: flex; align-items: flex-start; gap: 16px;
  padding: 10px 0; position: relative;
}
.it-dot {
  width: 10px; height: 10px; border-radius: 50%; flex-shrink: 0;
  margin-top: 3px; position: relative; z-index: 1;
}
.it-dot--error { background: #ef4444; box-shadow: 0 0 6px rgba(239,68,68,0.5); }
.it-dot--alert { background: #f59e0b; }
.it-dot--ack { background: #D4AF37; }
.it-dot--update { background: #818cf8; }
.it-dot--resolved { background: #22c55e; box-shadow: 0 0 6px rgba(34,197,94,0.4); }
.it-time { font-size: 10px; color: #555; font-family: 'Courier New', monospace; margin-bottom: 2px; }
.it-text { font-size: 12px; color: #888; font-weight: 300; line-height: 1.4; }

/* Status page visual */
.visual-card--status { max-width: 380px; }
.sp-header { padding: 16px 20px; border-bottom: 1px solid rgba(212,175,55,0.1); }
.sp-logo-row { display: flex; align-items: center; gap: 10px; margin-bottom: 12px; }
.sp-logo-placeholder { width: 20px; height: 20px; background: #D4AF37; border-radius: 3px; }
.sp-brand { font-size: 13px; color: #F2F0E4; font-weight: 400; }
.sp-overall { display: flex; align-items: center; gap: 8px; }
.sp-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }
.sp-dot--green { background: #22c55e; box-shadow: 0 0 6px rgba(34,197,94,0.5); }
.sp-overall-text { font-size: 12px; color: #22c55e; font-weight: 300; }
.sp-services { padding: 12px 20px; display: flex; flex-direction: column; gap: 0; }
.sp-svc {
  display: flex; align-items: center; justify-content: space-between;
  padding: 10px 0; border-bottom: 1px solid rgba(212,175,55,0.06); font-size: 12px;
}
.sp-svc:last-child { border-bottom: none; }
.sp-svc-name { color: #999; font-weight: 300; }
.sp-svc-status { font-size: 11px; font-weight: 400; }
.sp-svc-status--up { color: #22c55e; }
.sp-svc-status--degraded { color: #f59e0b; }

/* Analytics visual */
.analytics-stats {
  display: grid; grid-template-columns: repeat(4, 1fr);
  border-bottom: 1px solid rgba(212,175,55,0.08);
}
.an-stat { padding: 16px 14px; border-right: 1px solid rgba(212,175,55,0.08); text-align: center; }
.an-stat:last-child { border-right: none; }
.an-value { font-family: 'Marcellus', Georgia, serif; font-size: 16px; color: #D4AF37; margin-bottom: 4px; }
.an-label { font-size: 8px; font-weight: 600; letter-spacing: 0.2em; color: #555; }
.chart-placeholder {
  display: flex; align-items: flex-end; gap: 3px;
  padding: 16px 20px 0; height: 80px;
}
.chart-bar { flex: 1; background: rgba(212,175,55,0.25); border-radius: 1px 1px 0 0; min-height: 4px; }
.chart-x-labels {
  display: flex; justify-content: space-between;
  padding: 6px 20px 14px;
  font-size: 9px; color: #444; letter-spacing: 0.05em;
}

/* Check types */
.check-types-section { padding: 100px 32px; border-bottom: 1px solid rgba(212,175,55,0.1); }
.check-types-inner { max-width: 1100px; margin: 0 auto; }
.section-label { font-size: 10px; font-weight: 600; letter-spacing: 0.3em; color: #D4AF37; margin-bottom: 16px; }
.section-label.centered { text-align: center; }
.section-heading {
  font-family: 'Marcellus', Georgia, serif;
  font-size: clamp(28px, 4vw, 44px); font-weight: 400; color: #F2F0E4;
  letter-spacing: 0.05em; line-height: 1.2; margin-bottom: 56px;
}
.section-heading.centered { text-align: center; }
.check-types-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 1px; background: rgba(212,175,55,0.1); border: 1px solid rgba(212,175,55,0.1); }
.check-type-card { background: #141414; padding: 36px 28px; display: flex; flex-direction: column; gap: 12px; }
.ct-icon { color: #D4AF37; display: flex; }
.ct-title { font-family: 'Marcellus', Georgia, serif; font-size: 15px; letter-spacing: 0.1em; color: #F2F0E4; }
.ct-desc { font-size: 16px; color: #666; line-height: 1.75; font-weight: 300; flex: 1; }
.ct-tags { display: flex; flex-wrap: wrap; gap: 6px; margin-top: 4px; }
.ct-tag { font-size: 9px; font-weight: 600; letter-spacing: 0.12em; color: #D4AF37; background: rgba(212,175,55,0.08); border: 1px solid rgba(212,175,55,0.15); padding: 3px 8px; }

/* CTA */
.cta-section { padding: 100px 32px; text-align: center; background: #141414; border-top: 1px solid rgba(212,175,55,0.15); }
.cta-inner { max-width: 600px; margin: 0 auto; }
.cta-title { font-family: 'Marcellus', Georgia, serif; font-size: clamp(28px, 5vw, 48px); font-weight: 400; color: #F2F0E4; letter-spacing: 0.06em; line-height: 1.2; margin-bottom: 16px; }
.cta-desc { font-size: 16px; color: #666; font-weight: 300; line-height: 1.75; margin-bottom: 36px; }
.cta-buttons { display: flex; align-items: center; justify-content: center; gap: 16px; flex-wrap: wrap; }

.btn-primary { font-size: 11px; font-weight: 700; letter-spacing: 0.2em; color: #0A0A0A; background: #D4AF37; text-decoration: none; padding: 14px 32px; transition: background 0.3s, box-shadow 0.3s; display: inline-block; }
.btn-primary:hover { background: #F2E8C4; box-shadow: 0 0 20px rgba(212,175,55,0.3); }
.btn-outline { font-size: 11px; font-weight: 700; letter-spacing: 0.2em; color: #D4AF37; border: 1px solid rgba(212,175,55,0.4); text-decoration: none; padding: 14px 32px; transition: background 0.3s, border-color 0.3s; display: inline-block; }
.btn-outline:hover { background: rgba(212,175,55,0.07); border-color: #D4AF37; }

@media (max-width: 1024px) {
  .overview-inner { grid-template-columns: repeat(3, 1fr); }
  .overview-tile:nth-child(3) { border-right: none; }
  .feature-inner { grid-template-columns: 1fr; gap: 48px; }
  .feature-inner--reverse .feature-text { order: 0; }
  .feature-inner--reverse .feature-visual { order: 0; }
  .check-types-grid { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 640px) {
  .page-hero { padding: 120px 20px 72px; }
  .overview-inner { grid-template-columns: repeat(2, 1fr); }
  .overview-tile { border-right: none; border-bottom: 1px solid rgba(212,175,55,0.1); }
  .feature-section { padding: 72px 20px; }
  .check-types-section, .cta-section { padding: 72px 20px; }
  .check-types-grid { grid-template-columns: 1fr; }
  .analytics-stats { grid-template-columns: repeat(2, 1fr); }
}
</style>
