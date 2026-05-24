<template>
  <div class="page">
    <LandingNav />

    <section class="page-hero">
      <div class="hero-bg" aria-hidden="true"></div>
      <div class="hero-sunburst" aria-hidden="true"></div>
      <div class="hero-content">
        <div class="page-eyebrow">
          <div class="eyebrow-line" aria-hidden="true"></div>
          <span>DOCUMENTATION</span>
          <div class="eyebrow-line" aria-hidden="true"></div>
        </div>
        <h1 class="page-title">Everything You Need<br><span class="gold">to Get Started</span></h1>
        <p class="page-sub">
          Step-by-step guides, API references, and integration documentation. Find what you need or
          <router-link to="/contact" class="inline-link">ask our support team</router-link>.
        </p>
      </div>
    </section>

    <!-- Quick Start -->
    <section class="quick-start">
      <div class="section-inner">
        <div class="section-label">QUICK START</div>
        <h2 class="section-heading">Up in <span class="gold">5 Minutes</span></h2>
        <div class="steps-grid">
          <div class="step-card" v-for="(step, i) in quickSteps" :key="i">
            <div class="step-num">{{ String(i + 1).padStart(2, '0') }}</div>
            <h3 class="step-title">{{ step.title }}</h3>
            <p class="step-desc">{{ step.desc }}</p>
            <div class="step-code" v-if="step.code">
              <code>{{ step.code }}</code>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Doc Categories -->
    <section class="docs-section">
      <div class="section-inner">
        <div class="section-label">DOCUMENTATION</div>
        <h2 class="section-heading">Browse by <span class="gold">Topic</span></h2>
        <div class="docs-grid">
          <div class="doc-category" v-for="cat in docCategories" :key="cat.title">
            <div class="doc-cat-header">
              <div class="doc-cat-icon" v-html="cat.icon" aria-hidden="true"></div>
              <h3 class="doc-cat-title">{{ cat.title }}</h3>
            </div>
            <ul class="doc-cat-links">
              <li v-for="link in cat.links" :key="link">
                <router-link to="/contact" class="doc-link">
                  <span class="doc-link-arrow" aria-hidden="true">→</span>
                  {{ link }}
                </router-link>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </section>

    <!-- API Reference -->
    <section class="api-section">
      <div class="section-inner">
        <div class="api-inner">
          <div class="api-text">
            <div class="section-label">REST API</div>
            <h2 class="section-heading">Full API <span class="gold">Reference</span></h2>
            <p class="body-text">
              CloudPulse exposes a complete REST API for managing services, retrieving check results,
              triggering incidents, and configuring integrations programmatically. Available on
              Professional and Enterprise plans.
            </p>
            <div class="api-features">
              <div class="api-feature" v-for="f in apiFeatures" :key="f">
                <span class="api-dot" aria-hidden="true">◆</span>{{ f }}
              </div>
            </div>
            <div class="api-buttons">
              <router-link to="/contact" class="btn-primary">VIEW API REFERENCE</router-link>
            </div>
          </div>
          <div class="api-code-block">
            <div class="code-header">
              <span class="code-dot" aria-hidden="true"></span>
              <span class="code-dot" aria-hidden="true"></span>
              <span class="code-dot" aria-hidden="true"></span>
              <span class="code-filename">example.sh</span>
            </div>
            <pre class="code-body">{{ apiExample }}</pre>
          </div>
        </div>
      </div>
    </section>

    <!-- Support CTA -->
    <section class="support-cta">
      <div class="support-cta-inner">
        <h2 class="cta-title">Can't find what you need?</h2>
        <p class="cta-desc">Our support team responds to all enquiries within one business day.</p>
        <div class="cta-buttons">
          <router-link to="/support" class="btn-primary">GET SUPPORT</router-link>
          <router-link to="/contact" class="btn-outline">CONTACT US</router-link>
        </div>
      </div>
    </section>

    <LandingFooter />
  </div>
</template>

<script setup lang="ts">
import LandingNav from '@/components/landing/LandingNav.vue'
import LandingFooter from '@/components/landing/LandingFooter.vue'

const quickSteps = [
  {
    title: 'Create Your Account',
    desc: 'Sign up for a free Starter account — no credit card required. Your first 5 services are monitored immediately.',
    code: null,
  },
  {
    title: 'Add a Service',
    desc: 'Enter any HTTP, TCP, or DNS endpoint. Set your check interval, timeout threshold, and which regions to check from.',
    code: 'https://api.yourapp.com/health',
  },
  {
    title: 'Configure Alerts',
    desc: 'Connect your notification channels — Email and Discord are available now, with more integrations on the way.',
    code: null,
  },
  {
    title: 'Go Live',
    desc: 'CloudPulse begins checking immediately. You will receive your first alert within seconds of any downtime.',
    code: null,
  },
]

const docCategories = [
  {
    title: 'Getting Started',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"/></svg>`,
    links: [
      'Creating your first service',
      'Understanding check intervals',
      'Setting up alert channels',
      'Reading your dashboard',
      'Inviting team members',
    ],
  },
  {
    title: 'Monitoring',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><polyline points="22,12 18,12 15,21 9,3 6,12 2,12"/></svg>`,
    links: [
      'HTTP endpoint monitoring',
      'TCP port checks',
      'DNS resolution checks',
      'SSL certificate monitoring',
      'Custom request headers',
    ],
  },
  {
    title: 'Alerts & Notifications',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9M13.73 21a2 2 0 01-3.46 0"/></svg>`,
    links: [
      'Email notifications',
      'Discord webhooks',
      'Telegram bot setup',
      'Webhook configuration',
      'Alert escalation rules',
    ],
  },
  {
    title: 'Incident Management',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><path d="M12 9v4M12 17h.01"/><path d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"/></svg>`,
    links: [
      'Opening and tracking incidents',
      'Incident timeline view',
      'On-call scheduling',
      'Escalation policies',
      'Post-incident reports',
    ],
  },
  {
    title: 'Status Pages',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/><polyline points="9,22 9,12 15,12 15,22"/></svg>`,
    links: [
      'Creating a status page',
      'Adding services to a page',
      'Customising your page',
      'Custom domain setup',
      'Embedding status widgets',
    ],
  },
  {
    title: 'REST API',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><path d="M10 13a5 5 0 007.54.54l3-3a5 5 0 00-7.07-7.07l-1.72 1.71"/><path d="M14 11a5 5 0 00-7.54-.54l-3 3a5 5 0 007.07 7.07l1.71-1.71"/></svg>`,
    links: [
      'Authentication & API keys',
      'Services API',
      'Check results API',
      'Incidents API',
      'Webhooks & events',
    ],
  },
]

const apiFeatures = [
  'Bearer token authentication',
  'JSON request and response bodies',
  'Paginated list endpoints',
  'Webhook event subscriptions',
  'Rate limiting: 1,000 req/min (Professional)',
]

const apiExample = `# Retrieve all monitored services
curl -X GET https://api.cloudpulse.io/v1/services \\
  -H "Authorization: Bearer YOUR_API_KEY" \\
  -H "Content-Type: application/json"

# Response
{
  "data": [
    {
      "id": "svc_abc123",
      "name": "Production API",
      "url": "https://api.example.com/health",
      "status": "up",
      "uptime_30d": 99.98
    }
  ]
}`
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
.hero-content { position: relative; z-index: 1; max-width: 760px; margin: 0 auto; }
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
.eyebrow-line { width: 40px; height: 1px; background: rgba(212,175,55,0.4); }
.page-title {
  font-family: 'Marcellus', Georgia, serif;
  font-size: clamp(36px, 6vw, 64px);
  font-weight: 400;
  letter-spacing: 0.06em;
  color: #F2F0E4;
  line-height: 1.15;
  margin-bottom: 24px;
}
.gold { color: #D4AF37; }
.page-sub {
  font-size: 16px;
  color: #666;
  font-weight: 300;
  line-height: 1.8;
  max-width: 560px;
  margin: 0 auto;
}
.inline-link { color: #D4AF37; text-decoration: none; border-bottom: 1px solid rgba(212,175,55,0.4); }
.inline-link:hover { border-bottom-color: #D4AF37; }

/* Shared */
.section-inner { max-width: 1100px; margin: 0 auto; }
.section-label {
  font-size: 10px;
  font-weight: 600;
  letter-spacing: 0.3em;
  color: #D4AF37;
  margin-bottom: 16px;
}
.section-heading {
  font-family: 'Marcellus', Georgia, serif;
  font-size: clamp(28px, 4vw, 44px);
  font-weight: 400;
  color: #F2F0E4;
  letter-spacing: 0.05em;
  line-height: 1.2;
  margin-bottom: 48px;
}
.body-text {
  font-size: 16px;
  color: #777;
  line-height: 1.85;
  font-weight: 300;
  margin-bottom: 24px;
}

/* Quick Start */
.quick-start {
  padding: 100px 32px;
  background: #0E0E0E;
  border-bottom: 1px solid rgba(212,175,55,0.1);
}
.steps-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1px;
  background: rgba(212,175,55,0.1);
  border: 1px solid rgba(212,175,55,0.1);
}
.step-card {
  background: #141414;
  padding: 32px 28px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.step-num {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 32px;
  color: rgba(212,175,55,0.2);
  letter-spacing: 0.05em;
  line-height: 1;
}
.step-title {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 14px;
  letter-spacing: 0.1em;
  color: #F2F0E4;
}
.step-desc {
  font-size: 16px;
  color: #666;
  line-height: 1.75;
  font-weight: 300;
  flex: 1;
}
.step-code {
  background: rgba(212,175,55,0.05);
  border: 1px solid rgba(212,175,55,0.12);
  padding: 8px 12px;
  margin-top: 4px;
}
.step-code code {
  font-family: 'Courier New', monospace;
  font-size: 11px;
  color: #D4AF37;
  letter-spacing: 0.02em;
}

/* Docs Grid */
.docs-section {
  padding: 100px 32px;
  border-bottom: 1px solid rgba(212,175,55,0.1);
}
.docs-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}
.doc-category {
  background: #141414;
  border: 1px solid rgba(212,175,55,0.1);
  padding: 28px;
  transition: border-color 0.3s;
}
.doc-category:hover { border-color: rgba(212,175,55,0.3); }
.doc-cat-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(212,175,55,0.1);
}
.doc-cat-icon { color: #D4AF37; display: flex; flex-shrink: 0; }
.doc-cat-title {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 14px;
  letter-spacing: 0.1em;
  color: #F2F0E4;
}
.doc-cat-links {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.doc-link {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  color: #666;
  text-decoration: none;
  font-weight: 300;
  transition: color 0.3s;
  padding: 4px 0;
}
.doc-link:hover { color: #D4AF37; }
.doc-link-arrow {
  font-size: 12px;
  color: rgba(212,175,55,0.4);
  transition: color 0.3s, transform 0.3s;
  flex-shrink: 0;
}
.doc-link:hover .doc-link-arrow { color: #D4AF37; transform: translateX(3px); }

/* API */
.api-section {
  padding: 100px 32px;
  background: #0E0E0E;
  border-bottom: 1px solid rgba(212,175,55,0.1);
}
.api-inner {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 64px;
  align-items: center;
}
.api-features {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 32px;
}
.api-feature {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 16px;
  color: #777;
  font-weight: 300;
}
.api-dot { color: #D4AF37; font-size: 6px; flex-shrink: 0; }
.api-code-block {
  background: #0D0D0D;
  border: 1px solid rgba(212,175,55,0.15);
}
.code-header {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 12px 16px;
  border-bottom: 1px solid rgba(212,175,55,0.1);
}
.code-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: rgba(212,175,55,0.2);
}
.code-filename {
  font-size: 11px;
  color: #555;
  font-family: 'Courier New', monospace;
  letter-spacing: 0.05em;
  margin-left: 8px;
}
.code-body {
  padding: 24px 20px;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  color: #888;
  line-height: 1.7;
  white-space: pre;
  overflow-x: auto;
  margin: 0;
}

/* Support CTA */
.support-cta {
  padding: 80px 32px;
  text-align: center;
  background: #141414;
  border-top: 1px solid rgba(212,175,55,0.15);
}
.support-cta-inner { max-width: 560px; margin: 0 auto; }
.cta-title {
  font-family: 'Marcellus', Georgia, serif;
  font-size: clamp(22px, 3vw, 32px);
  color: #F2F0E4;
  letter-spacing: 0.05em;
  margin-bottom: 12px;
}
.cta-desc {
  font-size: 16px;
  color: #666;
  font-weight: 300;
  margin-bottom: 32px;
}
.cta-buttons { display: flex; align-items: center; justify-content: center; gap: 16px; flex-wrap: wrap; }
.btn-primary {
  font-size: 11px; font-weight: 700; letter-spacing: 0.2em;
  color: #0A0A0A; background: #D4AF37; text-decoration: none;
  padding: 13px 28px; transition: background 0.3s; display: inline-block;
}
.btn-primary:hover { background: #F2E8C4; }
.btn-outline {
  font-size: 11px; font-weight: 700; letter-spacing: 0.2em;
  color: #D4AF37; border: 1px solid rgba(212,175,55,0.4); text-decoration: none;
  padding: 13px 28px; transition: background 0.3s, border-color 0.3s; display: inline-block;
}
.btn-outline:hover { background: rgba(212,175,55,0.07); border-color: #D4AF37; }

.api-buttons { display: flex; gap: 12px; flex-wrap: wrap; }

@media (max-width: 1024px) {
  .steps-grid { grid-template-columns: repeat(2, 1fr); }
  .docs-grid { grid-template-columns: repeat(2, 1fr); }
  .api-inner { grid-template-columns: 1fr; }
}
@media (max-width: 640px) {
  .page-hero { padding: 120px 20px 72px; }
  .quick-start, .docs-section, .api-section, .support-cta { padding: 72px 20px; }
  .steps-grid { grid-template-columns: 1fr; }
  .docs-grid { grid-template-columns: 1fr; }
}
</style>
