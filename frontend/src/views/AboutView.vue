<template>
  <div class="page">
    <LandingNav />

    <section class="page-hero">
      <div class="hero-bg" aria-hidden="true"></div>
      <div class="hero-sunburst" aria-hidden="true"></div>
      <div class="hero-content">
        <div class="page-eyebrow">
          <div class="eyebrow-line" aria-hidden="true"></div>
          <span>OUR STORY</span>
          <div class="eyebrow-line" aria-hidden="true"></div>
        </div>
        <h1 class="page-title">Built by Engineers,<br><span class="gold">for Engineers</span></h1>
        <p class="page-sub">
          CloudPulse started as an internal tool after one too many 3 AM pages that could have been caught earlier.
          Today it monitors thousands of services for teams who care about uptime as much as we do.
        </p>
      </div>
    </section>

    <!-- Mission -->
    <section class="mission-section">
      <div class="mission-inner">
        <div class="mission-text">
          <div class="section-label">OUR MISSION</div>
          <h2 class="section-heading">Zero Downtime for<br><span class="gold">Every Team</span></h2>
          <p class="body-text">
            Downtime is expensive. A one-minute outage can cost thousands in lost revenue, erode user trust,
            and burn your on-call team. We believe every engineering team — from solo developers to enterprise
            operations — deserves the same precision monitoring that was once only accessible to companies
            with dedicated reliability engineering teams.
          </p>
          <p class="body-text">
            CloudPulse is built to catch failures before your users do, route the right alert to the right person
            instantly, and give your team the data to prevent recurrence. We obsess over detection speed,
            signal quality, and ease of setup so you can focus on shipping.
          </p>
        </div>
        <div class="mission-stats">
          <div class="mission-stat" v-for="s in missionStats" :key="s.value">
            <div class="mission-stat-value">{{ s.value }}</div>
            <div class="mission-stat-label">{{ s.label }}</div>
          </div>
        </div>
      </div>
    </section>

    <!-- Values -->
    <section class="values-section">
      <div class="values-inner">
        <div class="section-label centered">WHAT WE STAND FOR</div>
        <h2 class="section-heading centered">Our Core <span class="gold">Values</span></h2>
        <div class="values-grid">
          <div class="value-card" v-for="v in values" :key="v.title">
            <div class="value-icon" v-html="v.icon" aria-hidden="true"></div>
            <h3 class="value-title">{{ v.title }}</h3>
            <p class="value-desc">{{ v.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Timeline -->
    <section class="timeline-section">
      <div class="timeline-inner">
        <div class="section-label centered">HOW WE GOT HERE</div>
        <h2 class="section-heading centered">The <span class="gold">Journey</span></h2>
        <div class="timeline">
          <div class="timeline-item" v-for="(item, i) in timeline" :key="i">
            <div class="timeline-year">{{ item.year }}</div>
            <div class="timeline-dot" aria-hidden="true"></div>
            <div class="timeline-body">
              <div class="timeline-title">{{ item.title }}</div>
              <p class="timeline-desc">{{ item.desc }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA -->
    <section class="cta-section">
      <div class="cta-inner">
        <h2 class="cta-title">Ready to Stop <span class="gold">Flying Blind?</span></h2>
        <p class="cta-desc">Join thousands of engineers who trust CloudPulse to keep their services online.</p>
        <div class="cta-buttons">
          <router-link to="/sign-up" class="btn-primary">BEGIN MONITORING</router-link>
          <router-link to="/contact" class="btn-outline">TALK TO US</router-link>
        </div>
      </div>
    </section>

    <LandingFooter />
  </div>
</template>

<script setup lang="ts">
import LandingNav from '@/components/landing/LandingNav.vue'
import LandingFooter from '@/components/landing/LandingFooter.vue'

const missionStats = [
  { value: '10K+', label: 'SERVICES MONITORED' },
  { value: '99.99%', label: 'PLATFORM UPTIME' },
  { value: '15+', label: 'GLOBAL REGIONS' },
  { value: '<1s', label: 'MEAN DETECTION TIME' },
]

const values = [
  {
    title: 'SIGNAL OVER NOISE',
    desc: 'Every alert we send is one your team needs to act on. We obsess over reducing false positives and routing the right information to the right person.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22"><polyline points="22,12 18,12 15,21 9,3 6,12 2,12"/></svg>`,
  },
  {
    title: 'SIMPLICITY FIRST',
    desc: 'Powerful monitoring should not require a week of configuration. We ship features when they are genuinely useful, not to pad a feature comparison table.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22"><circle cx="12" cy="12" r="10"/><polyline points="12,6 12,12 16,14"/></svg>`,
  },
  {
    title: 'TRANSPARENCY',
    desc: 'Our pricing has no hidden fees. Our incident communications are honest. We hold our own platform to the same standards we help you enforce on yours.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>`,
  },
  {
    title: 'RELIABILITY IS NON-NEGOTIABLE',
    desc: 'A monitoring platform that goes down is worse than no monitoring at all. Our architecture is built with redundancy at every layer and we maintain a public status page.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>`,
  },
  {
    title: 'ENGINEER-LED',
    desc: 'Every product decision is made by people who have been on call. We understand what it means to be paged at 2 AM and we build accordingly.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22"><path d="M16 21v-2a4 4 0 00-4-4H6a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M22 21v-2a4 4 0 00-3-3.87M16 3.13a4 4 0 010 7.75"/></svg>`,
  },
  {
    title: 'CUSTOMER OBSESSION',
    desc: 'We read every support ticket. Our roadmap is shaped by real user feedback, not by what looks good in a deck. When you write to us, a person writes back.',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="22" height="22"><path d="M20.84 4.61a5.5 5.5 0 00-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 00-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 000-7.78z"/></svg>`,
  },
]

const timeline = [
  {
    year: '2022',
    title: 'The Incident That Started It All',
    desc: 'A regional CDN outage went undetected for 47 minutes because the existing monitoring tool only checked from a single region. The internal replacement project began the next day.',
  },
  {
    year: '2023',
    title: 'First Public Beta',
    desc: 'CloudPulse launched in private beta with multi-region checks, Slack and email alerts, and a basic status page. Early adopters provided the feedback that shaped everything that followed.',
  },
  {
    year: '2024',
    title: 'Incident Management & Integrations',
    desc: 'Structured incident workflows, PagerDuty integration, and the professional dashboard launched. The platform crossed 1,000 monitored services.',
  },
  {
    year: '2025',
    title: 'Enterprise & Global Expansion',
    desc: 'Custom domains for status pages, SSO, audit logs, and private endpoint monitoring via agent. Check network expanded to 15+ regions. Enterprise plan launched.',
  },
  {
    year: '2026',
    title: 'Today',
    desc: 'Monitoring tens of thousands of services for teams across the globe, from solo developers to Fortune 500 infrastructure teams.',
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
  max-width: 580px;
  margin: 0 auto;
}

/* Shared */
.section-label {
  font-size: 10px;
  font-weight: 600;
  letter-spacing: 0.3em;
  color: #D4AF37;
  margin-bottom: 16px;
}
.section-label.centered { text-align: center; }
.section-heading {
  font-family: 'Marcellus', Georgia, serif;
  font-size: clamp(28px, 4vw, 48px);
  font-weight: 400;
  color: #F2F0E4;
  letter-spacing: 0.05em;
  line-height: 1.2;
  margin-bottom: 32px;
}
.section-heading.centered { text-align: center; max-width: 560px; margin-left: auto; margin-right: auto; }
.body-text {
  font-size: 14px;
  color: #777;
  line-height: 1.85;
  font-weight: 300;
  margin-bottom: 16px;
}

/* Mission */
.mission-section {
  padding: 100px 32px;
  border-bottom: 1px solid rgba(212,175,55,0.1);
  background: #0E0E0E;
}
.mission-inner {
  max-width: 1100px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 340px;
  gap: 80px;
  align-items: center;
}
.mission-stats {
  display: flex;
  flex-direction: column;
  gap: 0;
}
.mission-stat {
  padding: 28px 32px;
  border: 1px solid rgba(212,175,55,0.12);
  border-bottom: none;
  background: #141414;
}
.mission-stat:last-child { border-bottom: 1px solid rgba(212,175,55,0.12); }
.mission-stat-value {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 32px;
  color: #D4AF37;
  letter-spacing: 0.06em;
  margin-bottom: 4px;
}
.mission-stat-label {
  font-size: 10px;
  font-weight: 600;
  letter-spacing: 0.25em;
  color: #555;
}

/* Values */
.values-section {
  padding: 100px 32px;
  border-bottom: 1px solid rgba(212,175,55,0.1);
}
.values-inner { max-width: 1100px; margin: 0 auto; }
.values-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}
.value-card {
  background: #141414;
  border: 1px solid rgba(212,175,55,0.1);
  padding: 32px 28px;
  transition: border-color 0.3s, background 0.3s;
}
.value-card:hover {
  border-color: rgba(212,175,55,0.3);
  background: #18160e;
}
.value-icon {
  color: #D4AF37;
  margin-bottom: 16px;
  display: flex;
}
.value-title {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 13px;
  letter-spacing: 0.15em;
  color: #F2F0E4;
  margin-bottom: 10px;
  text-transform: uppercase;
}
.value-desc {
  font-size: 13px;
  color: #666;
  line-height: 1.8;
  font-weight: 300;
}

/* Timeline */
.timeline-section {
  padding: 100px 32px;
  background: #0E0E0E;
  border-bottom: 1px solid rgba(212,175,55,0.1);
}
.timeline-inner { max-width: 760px; margin: 0 auto; }
.timeline {
  display: flex;
  flex-direction: column;
  gap: 0;
  position: relative;
}
.timeline::before {
  content: '';
  position: absolute;
  left: 88px;
  top: 0;
  bottom: 0;
  width: 1px;
  background: rgba(212,175,55,0.15);
}
.timeline-item {
  display: grid;
  grid-template-columns: 72px 32px 1fr;
  gap: 0 16px;
  align-items: flex-start;
  padding: 28px 0;
  border-bottom: 1px solid rgba(212,175,55,0.06);
  position: relative;
}
.timeline-item:last-child { border-bottom: none; }
.timeline-year {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 13px;
  letter-spacing: 0.08em;
  color: #D4AF37;
  padding-top: 3px;
  text-align: right;
}
.timeline-dot {
  width: 9px;
  height: 9px;
  background: #D4AF37;
  border-radius: 50%;
  margin-top: 6px;
  flex-shrink: 0;
  position: relative;
  z-index: 1;
  justify-self: center;
  box-shadow: 0 0 10px rgba(212,175,55,0.3);
}
.timeline-title {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 15px;
  letter-spacing: 0.06em;
  color: #F2F0E4;
  margin-bottom: 8px;
}
.timeline-desc {
  font-size: 13px;
  color: #666;
  line-height: 1.8;
  font-weight: 300;
}

/* CTA */
.cta-section {
  padding: 100px 32px;
  text-align: center;
  background: #141414;
  border-top: 1px solid rgba(212,175,55,0.15);
  border-bottom: 1px solid rgba(212,175,55,0.15);
}
.cta-inner { max-width: 600px; margin: 0 auto; }
.cta-title {
  font-family: 'Marcellus', Georgia, serif;
  font-size: clamp(28px, 5vw, 48px);
  font-weight: 400;
  color: #F2F0E4;
  letter-spacing: 0.06em;
  line-height: 1.2;
  margin-bottom: 16px;
}
.cta-desc {
  font-size: 14px;
  color: #666;
  font-weight: 300;
  line-height: 1.75;
  margin-bottom: 36px;
}
.cta-buttons {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  flex-wrap: wrap;
}
.btn-primary {
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.2em;
  color: #0A0A0A;
  background: #D4AF37;
  text-decoration: none;
  padding: 14px 32px;
  transition: background 0.3s, box-shadow 0.3s;
  display: inline-block;
}
.btn-primary:hover { background: #F2E8C4; box-shadow: 0 0 20px rgba(212,175,55,0.3); }
.btn-outline {
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.2em;
  color: #D4AF37;
  border: 1px solid rgba(212,175,55,0.4);
  text-decoration: none;
  padding: 14px 32px;
  transition: background 0.3s, border-color 0.3s;
  display: inline-block;
}
.btn-outline:hover { background: rgba(212,175,55,0.07); border-color: #D4AF37; }

@media (max-width: 1024px) {
  .mission-inner { grid-template-columns: 1fr; gap: 48px; }
  .mission-stats { flex-direction: row; flex-wrap: wrap; }
  .mission-stat { flex: 1; min-width: 160px; border-bottom: 1px solid rgba(212,175,55,0.12); }
  .values-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 640px) {
  .page-hero { padding: 120px 20px 72px; }
  .mission-section, .values-section, .timeline-section, .cta-section { padding: 72px 20px; }
  .values-grid { grid-template-columns: 1fr; }
  .timeline::before { left: 68px; }
  .timeline-item { grid-template-columns: 56px 24px 1fr; }
}
</style>
