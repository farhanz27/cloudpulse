<template>
  <div class="landing">
    <LandingNav />

    <!-- Hero -->
    <section class="hero">
      <div class="hero-crosshatch" aria-hidden="true"></div>
      <div class="hero-sunburst" aria-hidden="true"></div>

      <div class="hero-content">
        <div class="hero-eyebrow">
          <div class="eyebrow-line" aria-hidden="true"></div>
          <span>CLOUD INFRASTRUCTURE MONITORING</span>
          <div class="eyebrow-line" aria-hidden="true"></div>
        </div>

        <h1 class="hero-title">
          CLOUD<span class="title-gold">PULSE</span>
        </h1>

        <div class="hero-rule" aria-hidden="true">
          <div class="rule-line"></div>
          <div class="rule-diamond"></div>
          <div class="rule-line"></div>
        </div>

        <p class="hero-tagline">MONITOR · PROTECT · RESPOND</p>
        <p class="hero-desc">
          Real-time uptime monitoring, instant alerts, and incident management
          for your cloud services — engineered for precision.
        </p>

        <div class="hero-ctas">
          <router-link to="/sign-up" class="cta-primary">BEGIN MONITORING</router-link>
          <router-link to="/sign-in" class="cta-outline">SIGN IN</router-link>
        </div>

        <p class="hero-note">No credit card required · Free plan available · Setup in minutes</p>
      </div>

    </section>

    <!-- Stats Bar -->
    <section class="stats">
      <div class="stats-inner">
        <div class="stat-item" v-for="stat in stats" :key="stat.value">
          <div class="stat-value">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </div>
      </div>
    </section>

    <!-- Features -->
    <section class="features" id="features">
      <div class="section-header" v-motion :initial="{ opacity: 0, x: -40 }" :visible="{ opacity: 1, x: 0, transition: { duration: 600, ease: 'easeOut' } }">
        <div class="sh-line" aria-hidden="true"></div>
        <div class="sh-text">
          <span class="sh-label">PLATFORM CAPABILITIES</span>
        </div>
        <div class="sh-line" aria-hidden="true"></div>
      </div>
      <h2 class="section-title" v-motion :initial="{ opacity: 0, y: 40, scale: 0.97 }" :visible="{ opacity: 1, y: 0, scale: 1, transition: { duration: 600, ease: 'easeOut' } }">Powerful Features<br><span class="title-accent">Built for Scale</span></h2>

      <div class="features-grid">
        <div class="feat-card" v-for="(feat, i) in features" :key="feat.numeral"
          v-motion
          :initial="{ opacity: 0, y: 50, scale: 0.95 }"
          :visible="{ opacity: 1, y: 0, scale: 1, transition: { duration: 600, ease: 'easeOut', delay: i * 80 } }">
          <div class="feat-corner feat-corner--tl" aria-hidden="true"></div>
          <div class="feat-corner feat-corner--br" aria-hidden="true"></div>
          <div class="feat-numeral">{{ feat.numeral }}</div>
          <div class="feat-icon-wrap" aria-hidden="true">
            <div class="feat-diamond">
              <div class="feat-icon-inner" v-html="feat.icon"></div>
            </div>
          </div>
          <h3 class="feat-title">{{ feat.title }}</h3>
          <p class="feat-desc">{{ feat.desc }}</p>
        </div>
      </div>
    </section>

    <!-- How It Works -->
    <section class="how-works" id="how-it-works">
      <div class="section-header" v-motion :initial="{ opacity: 0, x: -40 }" :visible="{ opacity: 1, x: 0, transition: { duration: 600, ease: 'easeOut' } }">
        <div class="sh-line" aria-hidden="true"></div>
        <div class="sh-text"><span class="sh-label">HOW IT WORKS</span></div>
        <div class="sh-line" aria-hidden="true"></div>
      </div>
      <h2 class="section-title" v-motion :initial="{ opacity: 0, y: 40, scale: 0.97 }" :visible="{ opacity: 1, y: 0, scale: 1, transition: { duration: 600, ease: 'easeOut' } }">Up and Running<br><span class="title-accent">In Minutes</span></h2>

      <div class="steps-grid">
        <div class="step-item" v-for="(step, i) in steps" :key="i"
          v-motion
          :initial="{ opacity: 0, y: 50 }"
          :visible="{ opacity: 1, y: 0, transition: { duration: 600, ease: 'easeOut', delay: i * 110 } }">
          <div class="step-numeral">{{ step.numeral }}</div>
          <h3 class="step-title">{{ step.title }}</h3>
          <p class="step-desc">{{ step.desc }}</p>
        </div>
      </div>
    </section>

    <!-- Why Choose CloudPulse -->
    <section class="why-choose" id="why-choose">
      <div class="section-header" v-motion :initial="{ opacity: 0, x: -40 }" :visible="{ opacity: 1, x: 0, transition: { duration: 600, ease: 'easeOut' } }">
        <div class="sh-line" aria-hidden="true"></div>
        <div class="sh-text"><span class="sh-label">WHY CLOUDPULSE</span></div>
        <div class="sh-line" aria-hidden="true"></div>
      </div>
      <h2 class="section-title" v-motion :initial="{ opacity: 0, y: 40, scale: 0.97 }" :visible="{ opacity: 1, y: 0, scale: 1, transition: { duration: 600, ease: 'easeOut' } }">The Standard for<br><span class="title-accent">Serious Teams</span></h2>

      <div class="why-grid">
        <div class="why-card" v-for="(item, i) in whyItems" :key="item.title"
          v-motion
          :initial="{ opacity: 0, y: 40, scale: 0.95 }"
          :visible="{ opacity: 1, y: 0, scale: 1, transition: { duration: 550, ease: 'easeOut', delay: i * 70 } }">
          <div class="why-icon" v-html="item.icon" aria-hidden="true"></div>
          <h4 class="why-title">{{ item.title }}</h4>
          <p class="why-desc">{{ item.desc }}</p>
        </div>
      </div>
    </section>

    <!-- Testimonials -->
    <section class="testimonials-section">
      <div class="section-header" v-motion :initial="{ opacity: 0, x: -40 }" :visible="{ opacity: 1, x: 0, transition: { duration: 600, ease: 'easeOut' } }">
        <div class="sh-line" aria-hidden="true"></div>
        <div class="sh-text"><span class="sh-label">TRUSTED BY TEAMS</span></div>
        <div class="sh-line" aria-hidden="true"></div>
      </div>
      <h2 class="section-title" v-motion :initial="{ opacity: 0, y: 40, scale: 0.97 }" :visible="{ opacity: 1, y: 0, scale: 1, transition: { duration: 600, ease: 'easeOut' } }">Reliability You Can<br><span class="title-accent">Depend On</span></h2>

      <div class="testimonials-grid">
        <div class="testimonial-card" v-for="(t, i) in testimonials" :key="t.name"
          v-motion
          :initial="{ opacity: 0, y: 50, scale: 0.95 }"
          :visible="{ opacity: 1, y: 0, scale: 1, transition: { duration: 600, ease: 'easeOut', delay: i * 80 } }">
          <div class="testimonial-quote-mark" aria-hidden="true">"</div>
          <p class="testimonial-text">{{ t.quote }}</p>
          <div class="testimonial-author">
            <div class="testimonial-avatar" aria-hidden="true">{{ t.initials }}</div>
            <div>
              <div class="testimonial-name">{{ t.name }}</div>
              <div class="testimonial-role">{{ t.role }}</div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Use Cases -->
    <section class="use-cases-section">
      <div class="section-header" v-motion :initial="{ opacity: 0, x: -40 }" :visible="{ opacity: 1, x: 0, transition: { duration: 600, ease: 'easeOut' } }">
        <div class="sh-line" aria-hidden="true"></div>
        <div class="sh-text"><span class="sh-label">WHO IT'S FOR</span></div>
        <div class="sh-line" aria-hidden="true"></div>
      </div>
      <h2 class="section-title" v-motion :initial="{ opacity: 0, y: 40, scale: 0.97 }" :visible="{ opacity: 1, y: 0, scale: 1, transition: { duration: 600, ease: 'easeOut' } }">Built for Every<br><span class="title-accent">Engineering Team</span></h2>

      <div class="use-cases-grid">
        <div class="use-case-card" v-for="(uc, i) in useCases" :key="uc.title"
          v-motion
          :initial="{ opacity: 0, y: 50, scale: 0.95 }"
          :visible="{ opacity: 1, y: 0, scale: 1, transition: { duration: 600, ease: 'easeOut', delay: i * 90 } }">
          <div class="use-case-icon" v-html="uc.icon" aria-hidden="true"></div>
          <h3 class="use-case-title">{{ uc.title }}</h3>
          <p class="use-case-desc">{{ uc.desc }}</p>
          <ul class="use-case-points">
            <li v-for="pt in uc.points" :key="pt">
              <span class="uc-check" aria-hidden="true">◆</span>{{ pt }}
            </li>
          </ul>
        </div>
      </div>
    </section>

    <!-- Pricing -->
    <section class="pricing-section" id="pricing">
      <div class="section-header" v-motion :initial="{ opacity: 0, x: -40 }" :visible="{ opacity: 1, x: 0, transition: { duration: 600, ease: 'easeOut' } }">
        <div class="sh-line" aria-hidden="true"></div>
        <div class="sh-text"><span class="sh-label">PRICING</span></div>
        <div class="sh-line" aria-hidden="true"></div>
      </div>
      <h2 class="section-title" v-motion :initial="{ opacity: 0, y: 40, scale: 0.97 }" :visible="{ opacity: 1, y: 0, scale: 1, transition: { duration: 600, ease: 'easeOut' } }">Transparent Pricing<br><span class="title-accent">No Surprises</span></h2>

      <div class="billing-toggle" v-motion :initial="{ opacity: 0, y: 50 }" :visible="{ opacity: 1, y: 0, transition: { duration: 650, ease: 'easeOut' } }">
        <button
          class="toggle-btn"
          :class="{ 'toggle-btn--active': billing === 'monthly' }"
          @click="billing = 'monthly'"
        >MONTHLY</button>
        <button
          class="toggle-btn"
          :class="{ 'toggle-btn--active': billing === 'annually' }"
          @click="billing = 'annually'"
        >
          ANNUALLY
          <span class="save-badge">SAVE 20%</span>
        </button>
      </div>

      <div class="pricing-grid">
        <div
          class="plan-card"
          v-for="(plan, i) in displayPlans"
          :key="plan.name"
          :class="{ 'plan-card--featured': plan.featured }"
          v-motion
          :initial="i === 0 ? { opacity: 0, x: -30, y: 20 } : i === 1 ? { opacity: 0, y: 55 } : { opacity: 0, x: 30, y: 20 }"
          :visible="i === 0 ? { opacity: 1, x: 0, y: 0, transition: { duration: 650, ease: 'easeOut' } } : i === 1 ? { opacity: 1, y: 0, transition: { duration: 650, ease: 'easeOut', delay: 80 } } : { opacity: 1, x: 0, y: 0, transition: { duration: 650, ease: 'easeOut', delay: 160 } }"
        >
          <div class="plan-badge" v-if="plan.badge">{{ plan.badge }}</div>
          <div class="plan-corner plan-corner--tl" aria-hidden="true"></div>
          <div class="plan-corner plan-corner--br" aria-hidden="true"></div>
          <div class="plan-name">{{ plan.name }}</div>
          <div class="plan-price">
            <span class="plan-amount">{{ plan.price }}</span>
            <span class="plan-period" v-if="plan.period">{{ plan.period }}</span>
          </div>
          <div class="plan-annual-note" v-if="plan.annualNote">{{ plan.annualNote }}</div>
          <div class="plan-divider" aria-hidden="true"></div>
          <ul class="plan-feature-list">
            <li v-for="f in plan.features" :key="f">
              <span class="plan-check" aria-hidden="true">◆</span>{{ f }}
            </li>
          </ul>
          <router-link :to="plan.ctaLink" class="plan-cta" :class="{ 'plan-cta--featured': plan.featured }">
            {{ plan.ctaLabel }}
          </router-link>
        </div>
      </div>
    </section>

    <!-- FAQ -->
    <section class="faq-section" id="faq">
      <div class="section-header" v-motion :initial="{ opacity: 0, x: -40 }" :visible="{ opacity: 1, x: 0, transition: { duration: 600, ease: 'easeOut' } }">
        <div class="sh-line" aria-hidden="true"></div>
        <div class="sh-text"><span class="sh-label">FREQUENTLY ASKED</span></div>
        <div class="sh-line" aria-hidden="true"></div>
      </div>
      <h2 class="section-title" v-motion :initial="{ opacity: 0, y: 40, scale: 0.97 }" :visible="{ opacity: 1, y: 0, scale: 1, transition: { duration: 600, ease: 'easeOut' } }">Got Questions?<br><span class="title-accent">We Have Answers</span></h2>

      <div class="faq-list">
        <div
          class="faq-item"
          v-for="(item, i) in faqs"
          :key="i"
          v-motion
          :initial="{ opacity: 0, x: -30 }"
          :visible="{ opacity: 1, x: 0, transition: { duration: 550, ease: 'easeOut', delay: i * 60 } }"
          :class="{ 'faq-item--open': openFaq === i }"
          @click="openFaq = openFaq === i ? null : i"
        >
          <div class="faq-question">
            <span>{{ item.q }}</span>
            <span class="faq-toggle" aria-hidden="true">+</span>
          </div>
          <div class="faq-answer">{{ item.a }}</div>
        </div>
      </div>
    </section>

    <!-- Final CTA -->
    <section class="final-cta-section">
      <div class="final-cta-inner" v-motion :initial="{ opacity: 0, y: 40, scale: 0.97 }" :visible="{ opacity: 1, y: 0, scale: 1, transition: { duration: 650, ease: 'easeOut' } }">
        <div class="final-cta-eyebrow">
          <div class="eyebrow-line" aria-hidden="true"></div>
          <span>GET STARTED TODAY</span>
          <div class="eyebrow-line" aria-hidden="true"></div>
        </div>
        <h2 class="final-cta-title">Zero Downtime<br><span class="title-gold">Starts Here</span></h2>
        <p class="final-cta-desc">
          Join thousands of engineers who trust CloudPulse to keep their services online.
          Start monitoring in minutes — no credit card required.
        </p>
        <div class="final-cta-buttons">
          <router-link to="/sign-up" class="cta-primary">BEGIN MONITORING</router-link>
          <router-link to="/sign-in" class="cta-outline">SIGN IN</router-link>
        </div>
        <p class="final-cta-note">Free plan · No credit card · Cancel anytime</p>
      </div>
    </section>

    <LandingFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import LandingNav from '@/components/landing/LandingNav.vue'
import LandingFooter from '@/components/landing/LandingFooter.vue'

const openFaq = ref<number | null>(null)
const billing = ref<'monthly' | 'annually'>('monthly')

const stats = [
  { value: '99.99%', label: 'UPTIME SLA' },
  { value: '15+', label: 'GLOBAL REGIONS' },
  { value: '24/7', label: 'SUPPORT ACCESS' },
  { value: '<1s', label: 'DETECTION TIME' },
]

const features = [
  {
    numeral: 'I',
    title: 'UPTIME MONITORING',
    desc: 'Continuous health checks across HTTP, TCP, and DNS endpoints. Track response times, detect outages in seconds, and maintain a full historical log.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22">
      <path d="M2 12h2M20 12h2M12 2v2M12 20v2"/><circle cx="12" cy="12" r="4"/>
      <path d="M4.93 4.93l1.41 1.41M17.66 17.66l1.41 1.41M4.93 19.07l1.41-1.41M17.66 6.34l1.41-1.41"/>
    </svg>`,
  },
  {
    numeral: 'II',
    title: 'INSTANT ALERTS',
    desc: 'Multi-channel notifications via email, Slack, Telegram, and webhooks. Define alert thresholds and escalation rules — know before your users do.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22">
      <path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9M13.73 21a2 2 0 01-3.46 0"/>
    </svg>`,
  },
  {
    numeral: 'III',
    title: 'INCIDENT MANAGEMENT',
    desc: 'Track, escalate, and resolve incidents with a structured workflow. Maintain public-facing status pages to keep stakeholders informed in real time.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22">
      <path d="M12 9v4M12 17h.01"/>
      <path d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"/>
    </svg>`,
  },
]

const steps = [
  {
    numeral: 'I',
    title: 'CONNECT YOUR SERVICES',
    desc: 'Add any HTTP, TCP, or DNS endpoint in under a minute. No agent required for external monitoring.',
  },
  {
    numeral: 'II',
    title: '24/7 AUTOMATED CHECKS',
    desc: 'Our distributed network runs checks every 30 seconds from multiple global regions, eliminating false positives.',
  },
  {
    numeral: 'III',
    title: 'INSTANT NOTIFICATIONS',
    desc: 'The moment a service fails, your team is alerted via Slack, email, Telegram, or webhooks — automatically.',
  },
  {
    numeral: 'IV',
    title: 'ANALYZE & RESOLVE',
    desc: 'Review full incident timelines, response latency trends, and SLA compliance reports to prevent recurrence.',
  },
]

const whyItems = [
  {
    title: 'SUB-SECOND DETECTION',
    desc: 'Catch outages before your users do. Our checks detect failures and trigger alerts in under a second.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><circle cx="12" cy="12" r="10"/><polyline points="12,6 12,12 16,14"/></svg>`,
  },
  {
    title: 'MULTI-CHANNEL ALERTS',
    desc: 'Reach your team wherever they are — email, Slack, Telegram, PagerDuty, and custom webhooks.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></svg>`,
  },
  {
    title: 'GLOBAL CHECK NETWORK',
    desc: 'Checks run simultaneously from 15+ regions worldwide, catching regional outages and eliminating false positives.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><circle cx="12" cy="12" r="10"/><line x1="2" y1="12" x2="22" y2="12"/><path d="M12 2a15.3 15.3 0 014 10 15.3 15.3 0 01-4 10 15.3 15.3 0 01-4-10 15.3 15.3 0 014-10z"/></svg>`,
  },
  {
    title: 'ENTERPRISE SECURITY',
    desc: 'TLS 1.3 in transit, AES-256 at rest. SOC 2 Type II certified infrastructure across all plans.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>`,
  },
  {
    title: 'DEEP ANALYTICS',
    desc: 'Historical uptime data, P95 latency trends, SLA compliance reports, and custom time-range analysis.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><line x1="18" y1="20" x2="18" y2="10"/><line x1="12" y1="20" x2="12" y2="4"/><line x1="6" y1="20" x2="6" y2="14"/></svg>`,
  },
  {
    title: 'PUBLIC STATUS PAGES',
    desc: 'Keep customers informed with a branded, real-time status page. Reduce support load during incidents.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20"><path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/><polyline points="9,22 9,12 15,12 15,22"/></svg>`,
  },
]

const testimonials = [
  {
    quote: 'We went from a 45-minute mean time to detection down to under 90 seconds. CloudPulse\'s multi-region checks immediately caught a regional CDN failure our old tool missed entirely.',
    name: 'Alex Kim',
    role: 'Lead DevOps Engineer, Meridian FinTech',
    initials: 'AK',
  },
  {
    quote: 'The public status pages alone saved us thousands in support tickets during our last incident. Customers were informed before they even noticed the issue.',
    name: 'Sarah Chen',
    role: 'CTO, Prismatic Labs',
    initials: 'SC',
  },
  {
    quote: 'Setup took 8 minutes. Seriously. We had 20 services monitored the same afternoon. Every other tool we tried needed a full day of configuration.',
    name: 'Marcus Webb',
    role: 'Platform Engineer, Stackflow',
    initials: 'MW',
  },
  {
    quote: 'The on-call scheduling and escalation rules mean our team sleeps through non-critical alerts. We only get paged for things that actually matter.',
    name: 'Priya Nair',
    role: 'VP Engineering, Orbit Systems',
    initials: 'PN',
  },
  {
    quote: 'As a solo developer managing 12 client sites, CloudPulse\'s free plan is everything I need. When I needed more, upgrading to Pro was an obvious decision.',
    name: 'Tom Okonkwo',
    role: 'Freelance Infrastructure Engineer',
    initials: 'TO',
  },
]

const useCases = [
  {
    title: 'STARTUPS',
    desc: 'Build with confidence. Ship without fear of silent failures reaching your users first.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22"><path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"/></svg>`,
    points: ['Free plan to start immediately', 'No agent — monitor externally in seconds', 'Email + Slack alerts out of the box'],
  },
  {
    title: 'DEVOPS TEAMS',
    desc: 'Give your on-call rotation the precision tooling it deserves — not another alert storm.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22"><circle cx="12" cy="12" r="3"/><path d="M19.07 4.93a10 10 0 0 1 0 14.14M4.93 4.93a10 10 0 0 0 0 14.14"/><path d="M15.54 8.46a5 5 0 0 1 0 7.07M8.46 8.46a5 5 0 0 0 0 7.07"/></svg>`,
    points: ['On-call scheduling & escalation rules', 'PagerDuty and OpsGenie integrations', 'P95 latency trends and SLA tracking'],
  },
  {
    title: 'AGENCIES',
    desc: 'Monitor all client infrastructure from a single dashboard with full transparency.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22"><rect x="2" y="3" width="20" height="14" rx="2"/><path d="M8 21h8M12 17v4"/></svg>`,
    points: ['Branded public status pages per client', 'Bulk service management', 'Custom domain status pages (Enterprise)'],
  },
  {
    title: 'ENTERPRISES',
    desc: 'Production-grade observability with the security, compliance, and SLAs your organization requires.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22"><path d="M3 21h18M9 8h1M9 12h1M9 16h1M14 8h1M14 12h1M14 16h1M5 21V5a2 2 0 012-2h10a2 2 0 012 2v16"/></svg>`,
    points: ['SSO / SAML & audit logs', 'Dedicated SLA with uptime guarantee', 'Private endpoint agent for internal services'],
  },
]

const plans = [
  {
    name: 'STARTER',
    monthlyPrice: 'Free',
    monthlyPeriod: null,
    badge: null,
    featured: false,
    features: [
      '5 monitored services',
      '5-minute check intervals',
      'Email alerts only',
      '30-day uptime history',
      '1 public status page',
      'Basic incident tracking',
      'Community support',
    ],
    ctaLabel: 'START FREE',
    ctaLink: '/sign-up',
  },
  {
    name: 'PROFESSIONAL',
    monthlyPrice: 'RM20',
    monthlyPeriod: '/month',
    badge: 'MOST POPULAR',
    featured: true,
    features: [
      '50 monitored services',
      '1-minute check intervals',
      'All alert channels (Slack, email, Telegram, webhooks)',
      '12-month uptime history',
      'Unlimited status pages',
      'Full incident management',
      'On-call scheduling',
      'SSL certificate monitoring',
      'Response time SLA tracking',
      'API access',
      'Priority support',
    ],
    ctaLabel: 'GET STARTED',
    ctaLink: '/sign-up',
  },
  {
    name: 'ENTERPRISE',
    monthlyPrice: 'Custom',
    monthlyPeriod: null,
    badge: null,
    featured: false,
    features: [
      'Unlimited monitored services',
      '30-second check intervals',
      'All channels + PagerDuty, OpsGenie, custom integrations',
      'Unlimited uptime history',
      'Unlimited status pages with custom domain',
      'Advanced incident workflows & escalations',
      'Private endpoint monitoring (agent)',
      'Multi-region redundant checks',
      'Dedicated SLA with uptime guarantee',
      'SSO / SAML integration',
      'Audit logs & compliance exports',
      'Custom data retention policies',
      'Dedicated account manager',
      '24/7 premium support with SLA',
    ],
    ctaLabel: 'CONTACT US',
    ctaLink: '/contact',
  },
]

const displayPlans = computed(() =>
  plans.map(plan => {
    if (billing.value === 'annually' && plan.monthlyPrice === 'RM20') {
      return {
        ...plan,
        price: 'RM16',
        period: '/month',
        annualNote: null,
      }
    }
    return {
      ...plan,
      price: plan.monthlyPrice,
      period: plan.monthlyPeriod,
      annualNote: null,
    }
  })
)

const faqs = [
  {
    q: 'How frequently does CloudPulse check my services?',
    a: 'Depending on your plan, checks run every 30 seconds to 5 minutes from multiple global regions simultaneously. Our distributed check network eliminates false positives caused by regional network issues.',
  },
  {
    q: 'What happens when an outage is detected?',
    a: 'CloudPulse instantly notifies you via all configured alert channels, opens a structured incident, and tracks the full timeline. A recovery alert is sent automatically when your service comes back online.',
  },
  {
    q: 'Which notification channels are supported?',
    a: 'Email, Slack, Telegram, PagerDuty, and custom webhooks. Enterprise plans include bespoke integration options to fit your existing incident management workflow.',
  },
  {
    q: 'Can I monitor internal or private services?',
    a: 'Yes. CloudPulse supports private endpoint monitoring via our lightweight agent, which requires no inbound firewall rule changes. It establishes an outbound-only connection to our network.',
  },
  {
    q: 'Is my monitoring data secure?',
    a: 'All data is encrypted in transit using TLS 1.3 and at rest using AES-256. Our infrastructure runs on SOC 2 Type II certified cloud providers with full audit logging.',
  },
  {
    q: 'Do you offer a free plan?',
    a: 'Yes. The Starter plan is free forever with no credit card required. It supports up to 5 services with 5-minute check intervals and email alerts — perfect for personal projects and small teams.',
  },
  {
    q: 'Can I switch between monthly and annual billing?',
    a: 'Yes. You can switch billing cycles at any time from your account settings. When upgrading to annual billing, you\'ll be credited for any unused monthly time.',
  },
]
</script>

<style scoped>
/* ── Tokens ── */
:root {
  --gold: #D4AF37;
  --gold-dim: rgba(212,175,55,0.15);
  --bg: #0A0A0A;
  --card: #141414;
  --cream: #F2F0E4;
  --muted: #888;
  --font-display: 'Marcellus', Georgia, serif;
  --font-body: 'Josefin Sans', 'Inter', sans-serif;
}

.landing {
  background: #0A0A0A;
  color: #F2F0E4;
  min-height: 100vh;
  font-family: var(--font-body);
}

/* ── Hero ── */
.hero {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  padding: 120px 32px 80px;
}

.hero-crosshatch {
  position: absolute;
  inset: 0;
  background-image:
    repeating-linear-gradient(45deg, rgba(212,175,55,0.04) 0px, rgba(212,175,55,0.04) 1px, transparent 1px, transparent 12px),
    repeating-linear-gradient(-45deg, rgba(212,175,55,0.04) 0px, rgba(212,175,55,0.04) 1px, transparent 1px, transparent 12px);
  pointer-events: none;
}

.hero-sunburst {
  position: absolute;
  inset: 0;
  background: radial-gradient(ellipse at 50% 45%, rgba(212,175,55,0.1) 0%, rgba(212,175,55,0.02) 40%, transparent 70%);
  pointer-events: none;
}

.hero-content {
  position: relative;
  z-index: 1;
  text-align: center;
  max-width: 780px;
}

.hero-eyebrow {
  display: flex;
  align-items: center;
  gap: 16px;
  justify-content: center;
  margin-bottom: 28px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.25em;
  color: #D4AF37;
}
.eyebrow-line {
  width: 48px;
  height: 1px;
  background: rgba(212,175,55,0.5);
}

.hero-title {
  font-family: var(--font-display);
  font-size: clamp(56px, 10vw, 100px);
  font-weight: 400;
  letter-spacing: 0.12em;
  color: #F2F0E4;
  text-transform: uppercase;
  line-height: 1;
  margin-bottom: 28px;
}
.title-gold { color: #D4AF37; }

.hero-rule {
  display: flex;
  align-items: center;
  gap: 12px;
  justify-content: center;
  margin-bottom: 20px;
}
.rule-line {
  width: 80px;
  height: 1px;
  background: rgba(212,175,55,0.4);
}
.rule-diamond {
  width: 8px;
  height: 8px;
  border: 1px solid #D4AF37;
  transform: rotate(45deg);
}

.hero-tagline {
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 0.3em;
  color: #D4AF37;
  margin-bottom: 20px;
  text-transform: uppercase;
}

.hero-desc {
  font-size: 18px;
  color: #888;
  line-height: 1.8;
  max-width: 520px;
  margin: 0 auto 40px;
  font-weight: 300;
}

.hero-ctas {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  flex-wrap: wrap;
  margin-bottom: 20px;
}

.hero-note {
  font-size: 14px;
  color: #444;
  letter-spacing: 0.1em;
  font-weight: 400;
}

.cta-primary {
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 0.2em;
  color: #0A0A0A;
  background: #D4AF37;
  text-decoration: none;
  padding: 14px 36px;
  transition: background 0.3s, box-shadow 0.3s;
  display: inline-block;
}
.cta-primary:hover {
  background: #F2E8C4;
  box-shadow: 0 0 24px rgba(212,175,55,0.4);
}

.cta-outline {
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 0.2em;
  color: #D4AF37;
  border: 1px solid #D4AF37;
  text-decoration: none;
  padding: 14px 36px;
  transition: background 0.3s, color 0.3s;
  display: inline-block;
}
.cta-outline:hover {
  background: rgba(212,175,55,0.08);
}


/* ── Stats Bar ── */
.stats {
  background: #141414;
  border-top: 1px solid rgba(212,175,55,0.2);
  border-bottom: 1px solid rgba(212,175,55,0.2);
  padding: 48px 32px;
}

.stats-inner {
  max-width: 900px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
}

.stat-item {
  text-align: center;
  padding: 0 24px;
  border-right: 1px solid rgba(212,175,55,0.15);
}
.stat-item:last-child { border-right: none; }

.stat-value {
  font-family: var(--font-display);
  font-size: 26px;
  color: #D4AF37;
  letter-spacing: 0.06em;
  margin-bottom: 6px;
}

.stat-label {
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.28em;
  color: #555;
}

/* ── Shared section header ── */
.section-header {
  display: flex;
  align-items: center;
  gap: 24px;
  margin-bottom: 20px;
}
.sh-line {
  flex: 1;
  height: 1px;
  background: rgba(212,175,55,0.2);
}
.sh-text { flex-shrink: 0; }
.sh-label {
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.3em;
  color: #D4AF37;
}

.section-title {
  font-family: var(--font-display);
  font-size: clamp(36px, 5vw, 60px);
  font-weight: 400;
  color: #F2F0E4;
  letter-spacing: 0.04em;
  line-height: 1.15;
  text-align: center;
  margin: 0 auto 64px;
  max-width: 700px;
}
.title-accent { color: #D4AF37; }

/* ── Features ── */
.features {
  background: #0E0E0E;
  padding: 100px 32px;
  border-top: 1px solid rgba(212,175,55,0.15);
}

.features-grid {
  max-width: 1100px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.feat-card {
  position: relative;
  background: #141414;
  border: 1px solid rgba(212,175,55,0.15);
  padding: 40px 32px;
  transition: border-color 0.4s, transform 0.45s cubic-bezier(0.25, 0.46, 0.45, 0.94), box-shadow 0.45s;
  overflow: hidden;
}
.feat-card::before {
  content: '';
  position: absolute;
  top: 0; left: 0; right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, #D4AF37, transparent);
  transform: scaleX(0);
  transition: transform 0.5s ease;
}
.feat-card:hover {
  border-color: rgba(212,175,55,0.45);
  transform: translateY(-8px);
  box-shadow: 0 20px 52px rgba(0,0,0,0.5), 0 0 0 1px rgba(212,175,55,0.08);
}
.feat-card:hover::before { transform: scaleX(1); }

.feat-corner {
  position: absolute;
  width: 14px;
  height: 14px;
  pointer-events: none;
  transition: opacity 0.4s, width 0.4s, height 0.4s;
  opacity: 0.4;
}
.feat-card:hover .feat-corner { opacity: 1; width: 20px; height: 20px; }
.feat-corner--tl { top: 6px; left: 6px; border-top: 2px solid #D4AF37; border-left: 2px solid #D4AF37; }
.feat-corner--br { bottom: 6px; right: 6px; border-bottom: 2px solid #D4AF37; border-right: 2px solid #D4AF37; }

.feat-numeral {
  font-family: var(--font-display);
  font-size: 13px;
  color: rgba(212,175,55,0.35);
  letter-spacing: 0.2em;
  margin-bottom: 24px;
  transition: color 0.4s;
}
.feat-card:hover .feat-numeral { color: rgba(212,175,55,0.9); }

.feat-icon-wrap { margin-bottom: 24px; }

.feat-diamond {
  width: 48px;
  height: 48px;
  border: 1px solid rgba(212,175,55,0.3);
  transform: rotate(45deg);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: border-color 0.4s, box-shadow 0.4s, background 0.4s;
}
.feat-card:hover .feat-diamond {
  border-color: rgba(212,175,55,0.8);
  box-shadow: 0 0 20px rgba(212,175,55,0.2);
  background: rgba(212,175,55,0.06);
}

.feat-icon-inner {
  transform: rotate(-45deg);
  color: #D4AF37;
  display: flex;
  align-items: center;
  justify-content: center;
}

.feat-title {
  font-family: var(--font-display);
  font-size: 16px;
  letter-spacing: 0.12em;
  color: #F2F0E4;
  margin-bottom: 14px;
  text-transform: uppercase;
  transition: color 0.4s;
}
.feat-card:hover .feat-title { color: #D4AF37; }

.feat-desc {
  font-size: 17px;
  color: #888;
  line-height: 1.75;
  font-weight: 300;
}

/* ── How It Works ── */
.how-works {
  background: #0A0A0A;
  padding: 100px 32px;
  border-top: 1px solid rgba(212,175,55,0.15);
}

.steps-grid {
  max-width: 1100px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
}

.step-item {
  padding: 0 32px;
  border-right: 1px solid rgba(212,175,55,0.1);
}
.step-item:last-child { border-right: none; }
.step-item:first-child { padding-left: 0; }

.step-numeral {
  font-family: var(--font-display);
  font-size: 52px;
  color: rgba(212,175,55,0.12);
  letter-spacing: 0.05em;
  line-height: 1;
  margin-bottom: 20px;
}

.step-title {
  font-family: var(--font-display);
  font-size: 16px;
  letter-spacing: 0.15em;
  color: #F2F0E4;
  margin-bottom: 12px;
  text-transform: uppercase;
}

.step-desc {
  font-size: 17px;
  color: #666;
  line-height: 1.8;
  font-weight: 300;
}

/* ── Why Choose ── */
.why-choose {
  background: #0E0E0E;
  padding: 100px 32px;
  border-top: 1px solid rgba(212,175,55,0.15);
}

.why-grid {
  max-width: 1100px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.why-card {
  background: #141414;
  border: 1px solid rgba(212,175,55,0.1);
  padding: 36px 32px;
  position: relative;
  overflow: hidden;
  transition: background 0.35s, transform 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94), border-color 0.35s;
}
.why-card::after {
  content: '';
  position: absolute;
  left: 0; top: 0; bottom: 0;
  width: 2px;
  background: linear-gradient(to bottom, transparent, #D4AF37, transparent);
  transform: scaleY(0);
  transform-origin: center;
  transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}
.why-card:hover {
  background: #18160e;
  border-color: rgba(212,175,55,0.28);
  transform: translateY(-5px);
}
.why-card:hover::after { transform: scaleY(1); }

.why-icon {
  color: #D4AF37;
  margin-bottom: 16px;
  display: flex;
  transition: filter 0.35s;
}
.why-card:hover .why-icon { filter: drop-shadow(0 0 6px rgba(212,175,55,0.5)); }

.why-title {
  font-family: var(--font-display);
  font-size: 15px;
  letter-spacing: 0.18em;
  color: #F2F0E4;
  margin-bottom: 10px;
  text-transform: uppercase;
  transition: color 0.35s;
}
.why-card:hover .why-title { color: #D4AF37; }

.why-desc {
  font-size: 17px;
  color: #666;
  line-height: 1.8;
  font-weight: 300;
}

/* ── Testimonials ── */
.testimonials-section {
  background: #0A0A0A;
  padding: 100px 32px;
  border-top: 1px solid rgba(212,175,55,0.15);
}

.testimonials-grid {
  max-width: 1100px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.testimonial-card {
  background: #141414;
  border: 1px solid rgba(212,175,55,0.12);
  padding: 36px 32px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  position: relative;
  overflow: hidden;
  transition: border-color 0.4s, transform 0.45s cubic-bezier(0.25, 0.46, 0.45, 0.94), box-shadow 0.45s;
}
.testimonial-card::before {
  content: '';
  position: absolute;
  top: 0; left: 0; right: 0;
  height: 2px;
  background: linear-gradient(90deg, #D4AF37, rgba(212,175,55,0.3), transparent);
  transform: translateX(-101%);
  transition: transform 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}
.testimonial-card:hover {
  border-color: rgba(212,175,55,0.4);
  transform: translateY(-7px);
  box-shadow: 0 24px 52px rgba(0,0,0,0.45), 0 0 0 1px rgba(212,175,55,0.06);
}
.testimonial-card:hover::before { transform: translateX(0); }

.testimonial-quote-mark {
  font-family: var(--font-display);
  font-size: 48px;
  color: rgba(212,175,55,0.2);
  line-height: 1;
  margin-bottom: -10px;
  transition: color 0.4s;
}
.testimonial-card:hover .testimonial-quote-mark { color: rgba(212,175,55,0.5); }

.testimonial-text {
  font-size: 16px;
  color: #999;
  line-height: 1.85;
  font-weight: 300;
  flex: 1;
  transition: color 0.4s;
}
.testimonial-card:hover .testimonial-text { color: #bbb; }

.testimonial-author {
  display: flex;
  align-items: center;
  gap: 14px;
  padding-top: 20px;
  border-top: 1px solid rgba(212,175,55,0.1);
}

.testimonial-avatar {
  width: 38px;
  height: 38px;
  background: rgba(212,175,55,0.08);
  border: 1px solid rgba(212,175,55,0.25);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.05em;
  color: #D4AF37;
  flex-shrink: 0;
  transition: background 0.4s, border-color 0.4s;
}
.testimonial-card:hover .testimonial-avatar {
  background: rgba(212,175,55,0.15);
  border-color: #D4AF37;
}

.testimonial-name {
  font-size: 15px;
  font-weight: 600;
  color: #F2F0E4;
  letter-spacing: 0.05em;
  margin-bottom: 2px;
}

.testimonial-role {
  font-size: 14px;
  color: #555;
  font-weight: 300;
}

/* ── Use Cases ── */
.use-cases-section {
  background: #0E0E0E;
  padding: 100px 32px;
  border-top: 1px solid rgba(212,175,55,0.15);
}

.use-cases-grid {
  max-width: 1100px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.use-case-card {
  background: #141414;
  border: 1px solid rgba(212,175,55,0.1);
  padding: 36px 28px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  position: relative;
  overflow: hidden;
  transition: background 0.35s, transform 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94), border-color 0.35s, box-shadow 0.4s;
}
.use-case-card::after {
  content: '';
  position: absolute;
  bottom: 0; left: 0; right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, #D4AF37, transparent);
  transform: scaleX(0);
  transition: transform 0.45s ease;
}
.use-case-card:hover {
  background: #18160e;
  border-color: rgba(212,175,55,0.28);
  transform: translateY(-6px);
  box-shadow: 0 18px 44px rgba(0,0,0,0.4);
}
.use-case-card:hover::after { transform: scaleX(1); }

.use-case-icon {
  color: #D4AF37;
  margin-bottom: 4px;
  display: flex;
  transition: filter 0.35s;
}
.use-case-card:hover .use-case-icon {
  filter: drop-shadow(0 0 8px rgba(212,175,55,0.5));
}

.use-case-title {
  font-family: var(--font-display);
  font-size: 15px;
  letter-spacing: 0.15em;
  color: #F2F0E4;
  text-transform: uppercase;
  transition: color 0.35s;
}
.use-case-card:hover .use-case-title { color: #D4AF37; }

.use-case-desc {
  font-size: 16px;
  color: #666;
  line-height: 1.75;
  font-weight: 300;
}

.use-case-points {
  list-style: none;
  padding: 0;
  margin: 8px 0 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.use-case-points li {
  font-size: 14px;
  color: #555;
  font-weight: 300;
  display: flex;
  align-items: flex-start;
  gap: 8px;
  line-height: 1.5;
  transition: color 0.35s;
}
.use-case-card:hover .use-case-points li { color: #666; }

.uc-check {
  color: #D4AF37;
  font-size: 10px;
  flex-shrink: 0;
  margin-top: 4px;
}

/* ── Pricing ── */
.pricing-section {
  background: #0A0A0A;
  padding: 100px 32px;
  border-top: 1px solid rgba(212,175,55,0.15);
}

.billing-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  margin-bottom: 56px;
}

.toggle-btn {
  font-family: var(--font-body);
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 0.18em;
  color: #555;
  background: transparent;
  border: 1px solid rgba(212,175,55,0.15);
  padding: 10px 24px;
  cursor: pointer;
  transition: color 0.3s, border-color 0.3s, background 0.3s;
  display: flex;
  align-items: center;
  gap: 10px;
}
.toggle-btn:hover { color: #F2F0E4; border-color: rgba(212,175,55,0.4); }
.toggle-btn--active {
  color: #0A0A0A;
  background: #D4AF37;
  border-color: #D4AF37;
}

.save-badge {
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.1em;
  color: #0A0A0A;
  background: rgba(10,10,10,0.2);
  padding: 2px 7px;
}
.toggle-btn:not(.toggle-btn--active) .save-badge {
  color: #D4AF37;
  background: rgba(212,175,55,0.12);
}

.pricing-grid {
  max-width: 1100px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 32px;
  align-items: stretch;
}

.plan-card {
  position: relative;
  background: #141414;
  border: 1px solid rgba(212,175,55,0.15);
  padding: 52px 32px 36px;
  display: flex;
  flex-direction: column;
  transition: border-color 0.35s, box-shadow 0.35s, transform 0.35s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}
.plan-card:hover {
  border-color: rgba(212,175,55,0.45);
  box-shadow: 0 16px 48px rgba(0,0,0,0.4), 0 0 0 1px rgba(212,175,55,0.08);
  transform: translateY(-6px);
}

.plan-card--featured {
  border-color: rgba(212,175,55,0.5);
  background: #16140e;
}
.plan-card--featured:hover {
  border-color: #D4AF37;
  box-shadow: 0 20px 56px rgba(212,175,55,0.15), 0 0 0 1px rgba(212,175,55,0.2);
}

.plan-corner {
  position: absolute;
  width: 14px;
  height: 14px;
  pointer-events: none;
}
.plan-corner--tl { top: 6px; left: 6px; border-top: 2px solid rgba(212,175,55,0.25); border-left: 2px solid rgba(212,175,55,0.25); }
.plan-corner--br { bottom: 6px; right: 6px; border-bottom: 2px solid rgba(212,175,55,0.25); border-right: 2px solid rgba(212,175,55,0.25); }
.plan-card--featured .plan-corner--tl,
.plan-card--featured .plan-corner--br { border-color: #D4AF37; }

.plan-badge {
  position: absolute;
  top: -13px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.25em;
  color: #0A0A0A;
  background: #D4AF37;
  padding: 5px 14px;
  white-space: nowrap;
}

.plan-name {
  font-family: var(--font-display);
  font-size: 18px;
  letter-spacing: 0.15em;
  color: #F2F0E4;
  margin-bottom: 16px;
}

.plan-price {
  display: flex;
  align-items: baseline;
  gap: 4px;
  margin-bottom: 6px;
}

.plan-amount {
  font-family: var(--font-display);
  font-size: 36px;
  color: #D4AF37;
  line-height: 1;
}

.plan-period {
  font-size: 15px;
  color: #555;
  font-weight: 300;
}

.plan-annual-note {
  font-size: 14px;
  color: #555;
  font-weight: 300;
  margin-bottom: 18px;
  letter-spacing: 0.05em;
}

.plan-divider {
  height: 1px;
  background: rgba(212,175,55,0.15);
  margin-bottom: 24px;
  margin-top: 18px;
}

.plan-feature-list {
  list-style: none;
  padding: 0;
  margin: 0 0 32px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  flex: 1;
}

.plan-feature-list li {
  font-size: 15px;
  color: #888;
  font-weight: 300;
  display: flex;
  align-items: flex-start;
  gap: 10px;
  line-height: 1.5;
}

.plan-check {
  color: #D4AF37;
  font-size: 10px;
  flex-shrink: 0;
  margin-top: 4px;
}

.plan-cta {
  display: block;
  text-align: center;
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 0.2em;
  color: #D4AF37;
  border: 1px solid rgba(212,175,55,0.4);
  padding: 13px;
  text-decoration: none;
  transition: background 0.3s, color 0.3s, border-color 0.3s;
  margin-top: auto;
}
.plan-cta:hover {
  background: rgba(212,175,55,0.08);
  border-color: #D4AF37;
}

.plan-cta--featured {
  background: #D4AF37;
  color: #0A0A0A;
  border-color: #D4AF37;
}
.plan-cta--featured:hover {
  background: #F2E8C4;
  color: #0A0A0A;
}

/* ── FAQ ── */
.faq-section {
  background: #0E0E0E;
  padding: 100px 32px;
  border-top: 1px solid rgba(212,175,55,0.15);
}

.faq-list {
  max-width: 800px;
  margin: 0 auto;
}

.faq-item {
  border: 1px solid rgba(212,175,55,0.12);
  border-bottom: none;
  cursor: pointer;
  transition: background 0.3s, border-color 0.3s;
  user-select: none;
}
.faq-item:last-child { border-bottom: 1px solid rgba(212,175,55,0.12); }
.faq-item:hover { background: rgba(212,175,55,0.02); border-color: rgba(212,175,55,0.22); }
.faq-item--open { background: rgba(212,175,55,0.04); border-color: rgba(212,175,55,0.22); }

.faq-question {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  padding: 22px 24px;
  font-size: 16px;
  font-weight: 500;
  letter-spacing: 0.04em;
  color: #F2F0E4;
}

.faq-toggle {
  font-size: 20px;
  color: #D4AF37;
  line-height: 1;
  flex-shrink: 0;
  width: 24px;
  height: 24px;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid rgba(212,175,55,0.3);
  transition: background 0.3s, border-color 0.3s, transform 0.35s cubic-bezier(0.4, 0, 0.2, 1);
}
.faq-item--open .faq-toggle {
  background: rgba(212,175,55,0.1);
  border-color: rgba(212,175,55,0.6);
  transform: rotate(45deg);
}

.faq-answer {
  max-height: 0;
  overflow: hidden;
  padding: 0 24px;
  font-size: 17px;
  color: #888;
  line-height: 1.8;
  font-weight: 300;
  border-top: 1px solid transparent;
  transition:
    max-height 0.45s cubic-bezier(0.4, 0, 0.2, 1),
    padding 0.45s cubic-bezier(0.4, 0, 0.2, 1),
    border-color 0.3s;
}
.faq-item--open .faq-answer {
  max-height: 400px;
  padding: 16px 24px 22px;
  border-top-color: rgba(212,175,55,0.1);
}

/* ── Final CTA ── */
.final-cta-section {
  background: #141414;
  border-top: 1px solid rgba(212,175,55,0.2);
  border-bottom: 1px solid rgba(212,175,55,0.2);
  padding: 120px 32px;
  position: relative;
  overflow: hidden;
}

.final-cta-section::before {
  content: '';
  position: absolute;
  inset: 0;
  background: radial-gradient(ellipse at 50% 50%, rgba(212,175,55,0.07) 0%, transparent 65%);
  pointer-events: none;
}

.final-cta-inner {
  position: relative;
  z-index: 1;
  text-align: center;
  max-width: 680px;
  margin: 0 auto;
}

.final-cta-eyebrow {
  display: flex;
  align-items: center;
  gap: 16px;
  justify-content: center;
  margin-bottom: 28px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.25em;
  color: #D4AF37;
}

.final-cta-title {
  font-family: var(--font-display);
  font-size: clamp(40px, 6vw, 72px);
  font-weight: 400;
  letter-spacing: 0.06em;
  color: #F2F0E4;
  line-height: 1.1;
  margin-bottom: 24px;
}

.final-cta-desc {
  font-size: 17px;
  color: #777;
  line-height: 1.8;
  font-weight: 300;
  max-width: 480px;
  margin: 0 auto 40px;
}

.final-cta-buttons {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  flex-wrap: wrap;
  margin-bottom: 20px;
}

.final-cta-note {
  font-size: 13px;
  color: #3a3a3a;
  letter-spacing: 0.1em;
}

/* ── Responsive ── */
@media (max-width: 1024px) {
  .steps-grid { grid-template-columns: repeat(2, 1fr); gap: 0; }
  .step-item { padding: 24px 28px; border-right: none; border-bottom: 1px solid rgba(212,175,55,0.1); }
  .step-item:last-child { border-bottom: none; }
  .step-item:first-child { padding-left: 28px; }
  .use-cases-grid { grid-template-columns: repeat(2, 1fr); }
  .testimonials-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 900px) {
  .features-grid { grid-template-columns: 1fr; }
  .why-grid { grid-template-columns: repeat(2, 1fr); }
  .pricing-grid { grid-template-columns: 1fr; max-width: 480px; gap: 32px; }
  .stats-inner { grid-template-columns: repeat(2, 1fr); gap: 0; }
  .stat-item:nth-child(2) { border-right: none; }
  .stat-item:nth-child(3),
  .stat-item:nth-child(4) { border-top: 1px solid rgba(212,175,55,0.15); }
  .stat-item:nth-child(4) { border-right: none; }
}

@media (max-width: 640px) {
  .hero { padding: 100px 20px 60px; }
  .steps-grid { grid-template-columns: 1fr; }
  .why-grid { grid-template-columns: 1fr; }
  .use-cases-grid { grid-template-columns: 1fr; }
  .testimonials-grid { grid-template-columns: 1fr; }
  .stats { padding: 40px 20px; }
  .features, .how-works, .why-choose, .pricing-section, .faq-section,
  .testimonials-section, .use-cases-section { padding: 72px 20px; }
  .final-cta-section { padding: 80px 20px; }
  .section-title { font-size: clamp(28px, 8vw, 40px); }
  .billing-toggle { flex-direction: column; }
}
</style>
