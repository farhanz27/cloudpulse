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
          <span>INTEGRATIONS MARKETPLACE</span>
          <div class="eyebrow-line" aria-hidden="true"></div>
        </div>
        <h1 class="page-title">Connect Your <span class="gold">Entire Stack</span></h1>
        <p class="page-sub">
          CloudPulse integrates with the tools your team already uses — from alert channels to
          incident management platforms. Set up in minutes, not days.
        </p>
        <div class="hero-stats">
          <div class="hero-stat" v-for="s in heroStats" :key="s.label">
            <div class="hero-stat-value">{{ s.value }}</div>
            <div class="hero-stat-label">{{ s.label }}</div>
          </div>
        </div>
      </div>
    </section>

    <!-- Filter Bar -->
    <div class="filter-bar">
      <div class="filter-inner">
        <button
          v-for="cat in categories"
          :key="cat.id"
          class="filter-btn"
          :class="{ 'filter-btn--active': activeCategory === cat.id }"
          @click="activeCategory = cat.id"
        >
          {{ cat.label }}
        </button>
      </div>
    </div>

    <!-- Integrations Grid -->
    <section class="integrations-section">
      <div class="integrations-grid">
        <div
          class="int-card"
          v-for="integration in filteredIntegrations"
          :key="integration.id"
          :class="{ 'int-card--coming-soon': integration.status === 'coming-soon' }"
        >
          <div class="int-card-header">
            <div class="int-icon" :style="{ background: integration.iconBg }" aria-hidden="true" v-html="integration.icon"></div>
            <div class="int-meta">
              <div class="int-name">{{ integration.name }}</div>
              <div class="int-category">{{ integration.category }}</div>
            </div>
            <div
              class="int-status"
              :class="integration.plan === 'pro' ? 'int-status--pro' : `int-status--${integration.status}`"
            >
              {{ integration.plan === 'pro' ? 'PRO' : integration.status === 'available' ? 'FREE' : 'COMING SOON' }}
            </div>
          </div>

          <p class="int-desc">{{ integration.desc }}</p>

          <div class="int-benefits">
            <div class="int-benefit" v-for="b in integration.benefits" :key="b">
              <span class="benefit-dot" aria-hidden="true">◆</span>
              <span>{{ b }}</span>
            </div>
          </div>

          <div class="int-setup" v-if="integration.setup">
            <div class="int-setup-label">SETUP OVERVIEW</div>
            <p class="int-setup-text">{{ integration.setup }}</p>
          </div>

          <div class="int-actions">
            <router-link
              v-if="integration.status === 'available'"
              to="/sign-up"
              class="int-cta int-cta--primary"
            >
              GET STARTED
            </router-link>
            <router-link
              v-if="integration.status === 'available'"
              to="/sign-up"
              class="int-cta int-cta--ghost"
            >
              VIEW DOCS
            </router-link>
            <router-link
              v-if="integration.status === 'coming-soon'"
              to="/contact"
              class="int-cta int-cta--ghost"
            >
              REQUEST EARLY ACCESS
            </router-link>
          </div>
        </div>
      </div>

      <div class="custom-cta-block">
        <div class="custom-cta-inner">
          <div class="custom-cta-icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="28" height="28">
              <path d="M10 13a5 5 0 007.54.54l3-3a5 5 0 00-7.07-7.07l-1.72 1.71"/>
              <path d="M14 11a5 5 0 00-7.54-.54l-3 3a5 5 0 007.07 7.07l1.71-1.71"/>
            </svg>
          </div>
          <div class="custom-cta-text">
            <div class="custom-cta-title">Need a Custom Integration?</div>
            <p class="custom-cta-desc">Enterprise plans include bespoke webhook and API integrations tailored to your workflow. Contact our team to discuss your requirements.</p>
          </div>
          <router-link to="/contact" class="int-cta int-cta--primary">CONTACT US</router-link>
        </div>
      </div>
    </section>

    <LandingFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import LandingNav from '@/components/landing/LandingNav.vue'
import LandingFooter from '@/components/landing/LandingFooter.vue'

const activeCategory = ref('all')

const heroStats = [
  { value: '6', label: 'AVAILABLE NOW' },
  { value: '5 MIN', label: 'SETUP TIME' },
  { value: 'NO CODE', label: 'REQUIRED' },
]

const categories = [
  { id: 'all', label: 'All' },
  { id: 'alerts', label: 'Alerts & Notifications' },
  { id: 'incidents', label: 'Incident Management' },
  { id: 'observability', label: 'Observability' },
  { id: 'productivity', label: 'Productivity' },
]

const integrations = [
  {
    id: 'slack',
    name: 'Slack',
    category: 'Alerts & Notifications',
    categoryId: 'alerts',
    status: 'available',
    plan: 'pro',
    iconBg: 'rgba(74,21,75,0.15)',
    icon: `<svg viewBox="0 0 24 24" fill="currentColor" width="28" height="28" style="color:#E01E5A">
      <path d="M5.042 15.165a2.528 2.528 0 01-2.52 2.523A2.528 2.528 0 010 15.165a2.527 2.527 0 012.522-2.52h2.52v2.52zM6.313 15.165a2.527 2.527 0 012.521-2.52 2.527 2.527 0 012.521 2.52v6.313A2.528 2.528 0 018.834 24a2.528 2.528 0 01-2.521-2.522v-6.313zM8.834 5.042a2.528 2.528 0 01-2.521-2.52A2.528 2.528 0 018.834 0a2.528 2.528 0 012.521 2.522v2.52H8.834zM8.834 6.313a2.528 2.528 0 012.521 2.521 2.528 2.528 0 01-2.521 2.521H2.522A2.528 2.528 0 010 8.834a2.528 2.528 0 012.522-2.521h6.312zM18.956 8.834a2.528 2.528 0 012.522-2.521A2.528 2.528 0 0124 8.834a2.528 2.528 0 01-2.522 2.521h-2.522V8.834zM17.688 8.834a2.528 2.528 0 01-2.523 2.521 2.527 2.527 0 01-2.52-2.521V2.522A2.527 2.527 0 0115.165 0a2.528 2.528 0 012.523 2.522v6.312zM15.165 18.956a2.528 2.528 0 012.523 2.522A2.528 2.528 0 0115.165 24a2.527 2.527 0 01-2.52-2.522v-2.522h2.52zM15.165 17.688a2.527 2.527 0 01-2.52-2.523 2.526 2.526 0 012.52-2.52h6.313A2.527 2.527 0 0124 15.165a2.528 2.528 0 01-2.522 2.523h-6.313z"/>
    </svg>`,
    desc: 'Send real-time downtime alerts, recovery notifications, and incident updates directly to any Slack channel or user.',
    benefits: [
      'Instant downtime and recovery alerts to any channel',
      'Rich message formatting with incident details',
      'Configurable alert severity filtering',
      'Multiple channels for different services',
    ],
    setup: 'Create an Incoming Webhook in your Slack workspace settings, paste the URL into CloudPulse. Ready in under 2 minutes.',
  },
  {
    id: 'pagerduty',
    name: 'PagerDuty',
    category: 'Incident Management',
    categoryId: 'incidents',
    status: 'coming-soon',
    iconBg: 'rgba(6,154,68,0.1)',
    icon: `<svg viewBox="0 0 24 24" fill="currentColor" width="28" height="28" style="color:#069A44">
      <path d="M16.003 0H7.997C3.579 0 0 3.579 0 7.997v8.006C0 20.421 3.579 24 7.997 24h8.006C20.421 24 24 20.421 24 16.003V7.997C24 3.579 20.421 0 16.003 0zm-3.572 18.165H9.738v-4.93h2.693v4.93zm0-6.714H9.738V5.835h2.693v5.616z"/>
    </svg>`,
    desc: 'Automatically create, acknowledge, and resolve PagerDuty incidents when CloudPulse detects outages and recoveries.',
    benefits: [
      'Auto-create incidents on downtime detection',
      'Auto-resolve when service recovers',
      'Map services to PagerDuty services and escalation policies',
      'Bidirectional sync — acknowledge in PagerDuty to update CloudPulse',
    ],
    setup: 'Enter your PagerDuty API key and map CloudPulse services to PagerDuty services. Works with all PagerDuty plans.',
  },
  {
    id: 'email',
    name: 'Email',
    category: 'Alerts & Notifications',
    categoryId: 'alerts',
    status: 'available',
    iconBg: 'rgba(212,175,55,0.1)',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="28" height="28" style="color:#D4AF37">
      <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
      <polyline points="22,6 12,13 2,6"/>
    </svg>`,
    desc: 'Receive detailed downtime and recovery emails to any address.',
    benefits: [
      'Instant HTML-formatted alert emails',
      'Downtime and recovery notifications',
      'Alert digest summaries (hourly / daily)',
      'Multiple recipient addresses per service',
    ],
    setup: 'Email alerts are enabled by default. Add recipients in your monitor settings.',
  },
  {
    id: 'telegram',
    name: 'Telegram',
    category: 'Alerts & Notifications',
    categoryId: 'alerts',
    status: 'available',
    iconBg: 'rgba(0,136,204,0.1)',
    icon: `<svg viewBox="0 0 24 24" fill="currentColor" width="28" height="28" style="color:#0088CC">
      <path d="M11.944 0A12 12 0 000 12a12 12 0 0012 12 12 12 0 0012-12A12 12 0 0012 0a12 12 0 00-.056 0zm4.962 7.224c.1-.002.321.023.465.14a.506.506 0 01.171.325c.016.093.036.306.02.472-.18 1.898-.962 6.502-1.36 8.627-.168.9-.499 1.201-.82 1.23-.696.065-1.225-.46-1.9-.902-1.056-.693-1.653-1.124-2.678-1.8-1.185-.78-.417-1.21.258-1.91.177-.184 3.247-2.977 3.307-3.23.007-.032.014-.15-.056-.212s-.174-.041-.249-.024c-.106.024-1.793 1.14-5.061 3.345-.48.33-.913.49-1.302.48-.428-.008-1.252-.241-1.865-.44-.752-.245-1.349-.374-1.297-.789.027-.216.325-.437.893-.663 3.498-1.524 5.83-2.529 6.998-3.014 3.332-1.386 4.025-1.627 4.476-1.635z"/>
    </svg>`,
    desc: 'Get instant downtime alerts delivered to a Telegram chat or group. Ideal for teams that coordinate over Telegram.',
    benefits: [
      'Instant alerts to personal chat or group',
      'Markdown-formatted messages with service details',
      'Recovery and maintenance window notifications',
      'Works with Telegram bots — no phone number exposed',
    ],
    setup: 'Create a Telegram bot via @BotFather, enter the bot token and chat ID in CloudPulse settings. Takes under 3 minutes.',
  },
  {
    id: 'discord',
    name: 'Discord',
    category: 'Alerts & Notifications',
    categoryId: 'alerts',
    status: 'available',
    iconBg: 'rgba(88,101,242,0.1)',
    icon: `<svg viewBox="0 0 24 24" fill="currentColor" width="28" height="28" style="color:#5865F2">
      <path d="M20.317 4.37a19.791 19.791 0 00-4.885-1.515.074.074 0 00-.079.037c-.21.375-.444.864-.608 1.25a18.27 18.27 0 00-5.487 0 12.64 12.64 0 00-.617-1.25.077.077 0 00-.079-.037A19.736 19.736 0 003.677 4.37a.07.07 0 00-.032.027C.533 9.046-.32 13.58.099 18.057a.082.082 0 00.031.057 19.9 19.9 0 005.993 3.03.078.078 0 00.084-.028c.462-.63.874-1.295 1.226-1.994a.076.076 0 00-.041-.106 13.107 13.107 0 01-1.872-.892.077.077 0 01-.008-.128 10.2 10.2 0 00.372-.292.074.074 0 01.077-.01c3.928 1.793 8.18 1.793 12.062 0a.074.074 0 01.078.01c.12.098.246.198.373.292a.077.077 0 01-.006.127 12.299 12.299 0 01-1.873.892.077.077 0 00-.041.107c.36.698.772 1.362 1.225 1.993a.076.076 0 00.084.028 19.839 19.839 0 006.002-3.03.077.077 0 00.032-.054c.5-5.177-.838-9.674-3.549-13.66a.061.061 0 00-.031-.03zM8.02 15.33c-1.183 0-2.157-1.085-2.157-2.419 0-1.333.956-2.419 2.157-2.419 1.21 0 2.176 1.096 2.157 2.42 0 1.333-.956 2.418-2.157 2.418zm7.975 0c-1.183 0-2.157-1.085-2.157-2.419 0-1.333.955-2.419 2.157-2.419 1.21 0 2.176 1.096 2.157 2.42 0 1.333-.946 2.418-2.157 2.418z"/>
    </svg>`,
    desc: 'Send downtime alerts and recovery notifications directly to any Discord channel. Perfect for teams already coordinating on Discord.',
    benefits: [
      'Instant alerts to any Discord channel via webhook',
      'Rich embed formatting with service status and details',
      'Recovery and maintenance window notifications',
      'No bot installation required — uses Discord webhooks',
    ],
    setup: 'Create a webhook in your Discord channel settings, paste the URL into CloudPulse. Ready in under 2 minutes.',
  },
  {
    id: 'webhooks',
    name: 'Webhooks',
    category: 'Alerts & Notifications',
    categoryId: 'alerts',
    status: 'available',
    plan: 'pro',
    iconBg: 'rgba(212,175,55,0.08)',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="28" height="28" style="color:#D4AF37">
      <path d="M10 13a5 5 0 007.54.54l3-3a5 5 0 00-7.07-7.07l-1.72 1.71"/>
      <path d="M14 11a5 5 0 00-7.54-.54l-3 3a5 5 0 007.07 7.07l1.71-1.71"/>
    </svg>`,
    desc: 'Post structured JSON payloads to any URL when events occur — integrate CloudPulse with any internal tool or custom workflow.',
    benefits: [
      'Configurable JSON payload per event type',
      'HMAC-SHA256 signature for secure verification',
      'Retry logic with exponential back-off',
      'Delivery logs with response status and latency',
    ],
    setup: 'Enter your endpoint URL and choose which events trigger the webhook. Inspect delivery history in your dashboard.',
  },
  {
    id: 'msteams',
    name: 'Microsoft Teams',
    category: 'Alerts & Notifications',
    categoryId: 'alerts',
    status: 'available',
    plan: 'pro',
    iconBg: 'rgba(100,67,200,0.1)',
    icon: `<svg viewBox="0 0 24 24" fill="currentColor" width="28" height="28" style="color:#6264A7">
      <path d="M20.625 7.5h-5.25A.375.375 0 0115 7.125V3.375a.375.375 0 01.375-.375h.75a3.375 3.375 0 013.375 3.375v.75a.375.375 0 01-.375.375zM14.25 8.25H9a.75.75 0 00-.75.75v7.5c0 .414.336.75.75.75h5.25A.75.75 0 0015 16.5V9a.75.75 0 00-.75-.75zM7.5 9.75A.75.75 0 006.75 9H2.625A.375.375 0 002.25 9.375V16.5a.75.75 0 00.75.75H6.75a.75.75 0 00.75-.75V9.75zM9 19.5a2.25 2.25 0 100-4.5 2.25 2.25 0 000 4.5zM17.25 5.25a2.25 2.25 0 100-4.5 2.25 2.25 0 000 4.5z"/>
    </svg>`,
    desc: 'Deliver downtime and incident alerts to Microsoft Teams channels via incoming webhooks with rich adaptive card formatting.',
    benefits: [
      'Adaptive card formatting with service status',
      'Direct channel routing by service or severity',
      'Incident acknowledge and update notifications',
      'Works without installing a Teams app',
    ],
    setup: 'In Teams, go to a channel → Manage channel → Workflows → Post to channel when a webhook request is received. Paste the URL into CloudPulse.',
  },
  {
    id: 'opsgenie',
    name: 'OpsGenie',
    category: 'Incident Management',
    categoryId: 'incidents',
    status: 'coming-soon',
    iconBg: 'rgba(0,118,214,0.1)',
    icon: `<svg viewBox="0 0 24 24" fill="currentColor" width="28" height="28" style="color:#0076D6">
      <path d="M12 0C5.373 0 0 5.373 0 12s5.373 12 12 12 12-5.373 12-12S18.627 0 12 0zm0 4.8a7.2 7.2 0 110 14.4A7.2 7.2 0 0112 4.8zm0 2.4a4.8 4.8 0 100 9.6 4.8 4.8 0 000-9.6z"/>
    </svg>`,
    desc: 'Automatically open and close OpsGenie alerts from CloudPulse downtime events, with full escalation policy support.',
    benefits: [
      'Auto-create OpsGenie alerts on downtime',
      'Auto-close alerts on service recovery',
      'Supports OpsGenie teams and escalation policies',
      'Priority mapping based on service criticality',
    ],
    setup: 'Paste your OpsGenie API integration key into CloudPulse and map services to OpsGenie alert routing rules.',
  },
  {
    id: 'datadog',
    name: 'Datadog',
    category: 'Observability',
    categoryId: 'observability',
    status: 'coming-soon',
    iconBg: 'rgba(99,48,143,0.1)',
    icon: `<svg viewBox="0 0 24 24" fill="currentColor" width="28" height="28" style="color:#632F8F">
      <path d="M14.45 0L0 2.9v18.2L14.45 24l9.55-4.1V4.1L14.45 0zm6.8 18.7l-6.8 2.9-11.7-2-1.2-13.4 12.9-2.2 6.8 2.9V18.7z"/>
    </svg>`,
    desc: 'Push CloudPulse uptime events as Datadog events and service checks, unifying availability data with your metrics pipeline.',
    benefits: [
      'Uptime events appear in Datadog event stream',
      'Service check metrics for dashboards and monitors',
      'Tag-based filtering for multi-service environments',
      'Correlate availability with performance metrics',
    ],
    setup: 'Enter your Datadog API key and site region. CloudPulse will begin streaming events within minutes.',
  },
  {
    id: 'grafana',
    name: 'Grafana',
    category: 'Observability',
    categoryId: 'observability',
    status: 'coming-soon',
    iconBg: 'rgba(240,128,0,0.1)',
    icon: `<svg viewBox="0 0 24 24" fill="currentColor" width="28" height="28" style="color:#F08000">
      <path d="M12 0C5.373 0 0 5.373 0 12s5.373 12 12 12 12-5.373 12-12S18.627 0 12 0zm0 2a10 10 0 110 20A10 10 0 0112 2zm0 3a7 7 0 100 14A7 7 0 0012 5zm0 2a5 5 0 110 10A5 5 0 0112 7z"/>
    </svg>`,
    desc: 'Visualize CloudPulse uptime metrics and incident data natively in Grafana dashboards via our built-in data source plugin.',
    benefits: [
      'Native Grafana data source plugin',
      'Uptime percentage and response time panels',
      'Incident annotations on any time-series graph',
      'Combine with Prometheus, Loki, and Tempo data',
    ],
    setup: 'Install the CloudPulse Grafana plugin from the Grafana catalog and configure with your API key.',
  },
  {
    id: 'jira',
    name: 'Jira',
    category: 'Productivity',
    categoryId: 'productivity',
    status: 'coming-soon',
    iconBg: 'rgba(0,82,204,0.1)',
    icon: `<svg viewBox="0 0 24 24" fill="currentColor" width="28" height="28" style="color:#0052CC">
      <path d="M11.571 11.429L6.286 6.143 11.571.857 16.857 6.143l-5.286 5.286zM0 11.571l5.286-5.285 5.286 5.285-5.286 5.286L0 11.571zm12 0l5.286-5.285L22.571 11.571 17.286 16.857 12 11.571zm-5.714 5.715L11.571 12l5.286 5.286-5.286 5.285-5.285-5.285z"/>
    </svg>`,
    desc: 'Automatically create Jira issues when incidents are opened and transition them when services recover. Keep engineering and ops in sync.',
    benefits: [
      'Auto-create issues on incident open',
      'Transition issues on recovery',
      'Map to any Jira project and issue type',
      'Include uptime data and timeline in issue body',
    ],
    setup: 'Connect via Atlassian OAuth, select your Jira project and configure the field mapping. Works with Jira Cloud and Server.',
  },
  {
    id: 'aws-cloudwatch',
    name: 'AWS CloudWatch',
    category: 'Observability',
    categoryId: 'observability',
    status: 'coming-soon',
    iconBg: 'rgba(255,153,0,0.1)',
    icon: `<svg viewBox="0 0 24 24" fill="currentColor" width="28" height="28" style="color:#FF9900">
      <path d="M12 0L0 6v12l12 6 12-6V6L12 0zm-1.5 17.1l-4.8-2.4V9.3l4.8 2.4v5.4zm.75-6.6L6.45 8.1 12 5.4l5.55 2.7-6.3 2.4zm6.45 4.2l-4.8 2.4V11.7l4.8-2.4v5.4z"/>
    </svg>`,
    desc: 'Publish CloudPulse uptime metrics directly to AWS CloudWatch for unified monitoring within your AWS environment.',
    benefits: [
      'Native CloudWatch custom metrics',
      'Trigger CloudWatch alarms from uptime events',
      'Integrate with AWS SNS notification pipelines',
      'Unified AWS monitoring and alerting',
    ],
    setup: 'Provide IAM credentials with CloudWatch PutMetricData permissions. No additional setup required.',
  },
  {
    id: 'google-cloud',
    name: 'Google Cloud Monitoring',
    category: 'Observability',
    categoryId: 'observability',
    status: 'coming-soon',
    iconBg: 'rgba(66,133,244,0.1)',
    icon: `<svg viewBox="0 0 24 24" fill="currentColor" width="28" height="28" style="color:#4285F4">
      <path d="M12 0C5.373 0 0 5.373 0 12s5.373 12 12 12 12-5.373 12-12S18.627 0 12 0zm5.25 15.75H6.75a.75.75 0 010-1.5h10.5a.75.75 0 010 1.5zm0-4.5H6.75a.75.75 0 010-1.5h10.5a.75.75 0 010 1.5zm0-4.5H6.75a.75.75 0 010-1.5h10.5a.75.75 0 010 1.5z"/>
    </svg>`,
    desc: 'Stream CloudPulse availability data to Google Cloud Monitoring for seamless integration with your GCP observability stack.',
    benefits: [
      'Custom GCP metrics from CloudPulse events',
      'Uptime check correlation with GCP native checks',
      'Cloud Monitoring alerting policy triggers',
      'Integrated with Google Cloud dashboards',
    ],
    setup: 'Use a GCP service account with Monitoring write permissions. Configuration takes under 5 minutes.',
  },
]

const tierOrder = (i: typeof integrations[number]) => {
  if (i.status === 'coming-soon') return 2
  if (i.plan === 'pro') return 1
  return 0
}

const filteredIntegrations = computed(() => {
  const list = activeCategory.value === 'all'
    ? integrations
    : integrations.filter(i => i.categoryId === activeCategory.value)
  return [...list].sort((a, b) => tierOrder(a) - tierOrder(b))
})
</script>

<style scoped>
.page {
  background: #0A0A0A;
  color: #F2F0E4;
  min-height: 100vh;
  font-family: 'Josefin Sans', 'Inter', sans-serif;
}

/* ── Hero ── */
.page-hero {
  position: relative;
  padding: 140px 32px 100px;
  text-align: center;
  border-bottom: 1px solid rgba(212,175,55,0.15);
  overflow: hidden;
}
.hero-bg {
  position: absolute;
  inset: 0;
  background-image:
    repeating-linear-gradient(45deg, rgba(212,175,55,0.03) 0px, rgba(212,175,55,0.03) 1px, transparent 1px, transparent 14px),
    repeating-linear-gradient(-45deg, rgba(212,175,55,0.03) 0px, rgba(212,175,55,0.03) 1px, transparent 1px, transparent 14px);
  pointer-events: none;
}
.hero-sunburst {
  position: absolute;
  inset: 0;
  background: radial-gradient(ellipse at 50% 60%, rgba(212,175,55,0.08) 0%, transparent 65%);
  pointer-events: none;
}
.hero-content {
  position: relative;
  z-index: 1;
  max-width: 760px;
  margin: 0 auto;
}

.page-eyebrow {
  display: flex;
  align-items: center;
  gap: 16px;
  justify-content: center;
  margin-bottom: 24px;
  font-size: 11px;
  font-weight: 600;
  letter-spacing: 0.25em;
  color: #D4AF37;
}
.eyebrow-line {
  width: 40px;
  height: 1px;
  background: rgba(212,175,55,0.4);
}

.page-title {
  font-family: 'Marcellus', Georgia, serif;
  font-size: clamp(36px, 6vw, 68px);
  font-weight: 400;
  letter-spacing: 0.06em;
  color: #F2F0E4;
  line-height: 1.1;
  margin-bottom: 20px;
}
.gold { color: #D4AF37; }

.page-sub {
  font-size: 16px;
  color: #666;
  font-weight: 300;
  line-height: 1.75;
  max-width: 560px;
  margin: 0 auto 48px;
}

.hero-stats {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 48px;
}
.hero-stat { text-align: center; }
.hero-stat-value {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 28px;
  color: #D4AF37;
  letter-spacing: 0.06em;
  margin-bottom: 4px;
}
.hero-stat-label {
  font-size: 10px;
  font-weight: 600;
  letter-spacing: 0.25em;
  color: #555;
}

/* ── Filter Bar ── */
.filter-bar {
  position: sticky;
  top: 64px;
  z-index: 50;
  background: rgba(10,10,10,0.95);
  backdrop-filter: blur(8px);
  border-bottom: 1px solid rgba(212,175,55,0.1);
}
.filter-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 32px;
  display: flex;
  align-items: center;
  gap: 4px;
  height: 56px;
  overflow-x: auto;
}
.filter-btn {
  font-size: 11px;
  font-weight: 600;
  letter-spacing: 0.15em;
  color: #555;
  background: none;
  border: none;
  padding: 8px 16px;
  cursor: pointer;
  transition: color 0.3s, background 0.3s;
  white-space: nowrap;
  font-family: inherit;
  border-bottom: 2px solid transparent;
}
.filter-btn:hover { color: #F2F0E4; }
.filter-btn--active {
  color: #D4AF37;
  border-bottom-color: #D4AF37;
}

/* ── Grid ── */
.integrations-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 64px 32px 80px;
}

.integrations-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin-bottom: 64px;
}

/* ── Integration Card ── */
.int-card {
  background: #141414;
  border: 1px solid rgba(212,175,55,0.15);
  padding: 28px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  transition: border-color 0.3s, box-shadow 0.3s;
}
.int-card:hover {
  border-color: rgba(212,175,55,0.4);
  box-shadow: 0 4px 24px rgba(212,175,55,0.06);
}
.int-card--coming-soon {
  opacity: 0.7;
}
.int-card--coming-soon:hover {
  border-color: rgba(212,175,55,0.2);
  box-shadow: none;
}

.int-card-header {
  display: flex;
  align-items: flex-start;
  gap: 14px;
}

.int-icon {
  width: 52px;
  height: 52px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  border: 1px solid rgba(212,175,55,0.1);
}

.int-meta {
  flex: 1;
  min-width: 0;
}
.int-name {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 16px;
  letter-spacing: 0.05em;
  color: #F2F0E4;
  margin-bottom: 3px;
}
.int-category {
  font-size: 11px;
  color: #555;
  font-weight: 300;
  letter-spacing: 0.05em;
}

.int-status {
  font-size: 9px;
  font-weight: 700;
  letter-spacing: 0.2em;
  padding: 4px 10px;
  flex-shrink: 0;
  align-self: flex-start;
}
.int-status--available {
  color: #22c55e;
  background: rgba(34,197,94,0.1);
  border: 1px solid rgba(34,197,94,0.2);
}
.int-status--coming-soon {
  color: #888;
  background: rgba(136,136,136,0.08);
  border: 1px solid rgba(136,136,136,0.15);
}
.int-status--pro {
  color: #D4AF37;
  background: rgba(212,175,55,0.1);
  border: 1px solid rgba(212,175,55,0.35);
}

.int-desc {
  font-size: 16px;
  color: #777;
  line-height: 1.75;
  font-weight: 300;
}

.int-benefits {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.int-benefit {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  font-size: 16px;
  color: #888;
  font-weight: 300;
  line-height: 1.5;
}
.benefit-dot {
  color: #D4AF37;
  font-size: 6px;
  margin-top: 4px;
  flex-shrink: 0;
}

.int-setup {
  background: rgba(212,175,55,0.04);
  border: 1px solid rgba(212,175,55,0.1);
  padding: 14px 16px;
}
.int-setup-label {
  font-size: 9px;
  font-weight: 700;
  letter-spacing: 0.25em;
  color: #D4AF37;
  margin-bottom: 6px;
}
.int-setup-text {
  font-size: 16px;
  color: #666;
  font-weight: 300;
  line-height: 1.6;
}

.int-actions {
  display: flex;
  gap: 10px;
  margin-top: auto;
}

.int-cta {
  font-size: 10px;
  font-weight: 700;
  letter-spacing: 0.18em;
  text-decoration: none;
  padding: 10px 18px;
  transition: background 0.3s, color 0.3s, box-shadow 0.3s;
  display: inline-block;
}
.int-cta--primary {
  background: #D4AF37;
  color: #0A0A0A;
}
.int-cta--primary:hover {
  background: #F2E8C4;
  box-shadow: 0 0 16px rgba(212,175,55,0.3);
}
.int-cta--ghost {
  border: 1px solid rgba(212,175,55,0.3);
  color: #D4AF37;
}
.int-cta--ghost:hover {
  background: rgba(212,175,55,0.08);
  border-color: #D4AF37;
}

/* ── Custom CTA Block ── */
.custom-cta-block {
  border: 1px solid rgba(212,175,55,0.2);
  background: #141414;
  padding: 40px 48px;
}
.custom-cta-inner {
  display: flex;
  align-items: center;
  gap: 32px;
}
.custom-cta-icon {
  color: #D4AF37;
  flex-shrink: 0;
  width: 56px;
  height: 56px;
  border: 1px solid rgba(212,175,55,0.3);
  display: flex;
  align-items: center;
  justify-content: center;
}
.custom-cta-text { flex: 1; }
.custom-cta-title {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 20px;
  color: #F2F0E4;
  letter-spacing: 0.05em;
  margin-bottom: 8px;
}
.custom-cta-desc {
  font-size: 16px;
  color: #666;
  font-weight: 300;
  line-height: 1.7;
}

/* ── Responsive ── */
@media (max-width: 1024px) {
  .integrations-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 768px) {
  .integrations-grid { grid-template-columns: 1fr; }
  .page-hero { padding: 120px 20px 72px; }
  .integrations-section { padding: 48px 20px 64px; }
  .hero-stats { gap: 28px; }
  .custom-cta-inner { flex-direction: column; align-items: flex-start; gap: 20px; }
  .custom-cta-block { padding: 28px 24px; }
  .filter-inner { padding: 0 20px; }
}

@media (max-width: 480px) {
  .hero-stats { flex-direction: column; gap: 20px; }
}
</style>
