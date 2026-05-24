<template>
  <div class="page">
    <LandingNav />

    <section class="page-hero">
      <div class="hero-bg" aria-hidden="true"></div>
      <div class="hero-sunburst" aria-hidden="true"></div>
      <div class="hero-content">
        <div class="page-eyebrow">
          <div class="eyebrow-line" aria-hidden="true"></div>
          <span>SUPPORT CENTER</span>
          <div class="eyebrow-line" aria-hidden="true"></div>
        </div>
        <h1 class="page-title">We're Here<br><span class="gold">When You Need Us</span></h1>
        <p class="page-sub">
          Choose the fastest path to an answer — self-service documentation, community discussion,
          or direct contact with our support team.
        </p>
      </div>
    </section>

    <!-- Support Channels -->
    <section class="channels-section">
      <div class="section-inner">
        <div class="section-label">SUPPORT OPTIONS</div>
        <h2 class="section-heading">How Can We <span class="gold">Help?</span></h2>
        <div class="channels-grid">
          <div class="channel-card" v-for="ch in channels" :key="ch.title">
            <div class="channel-icon" v-html="ch.icon" aria-hidden="true"></div>
            <h3 class="channel-title">{{ ch.title }}</h3>
            <p class="channel-desc">{{ ch.desc }}</p>
            <div class="channel-meta">{{ ch.meta }}</div>
            <router-link :to="ch.link" class="channel-cta">{{ ch.cta }}</router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- SLA Table -->
    <section class="sla-section">
      <div class="section-inner">
        <div class="section-label">RESPONSE TIMES</div>
        <h2 class="section-heading">Support <span class="gold">SLA by Plan</span></h2>
        <div class="sla-table">
          <div class="sla-row sla-header">
            <div class="sla-cell sla-cell--label">Support Type</div>
            <div class="sla-cell">Starter</div>
            <div class="sla-cell">Professional</div>
            <div class="sla-cell">Enterprise</div>
          </div>
          <div class="sla-row" v-for="row in slaRows" :key="row.type">
            <div class="sla-cell sla-cell--label">{{ row.type }}</div>
            <div class="sla-cell" :class="{ 'sla-na': row.starter === '—' }">{{ row.starter }}</div>
            <div class="sla-cell">{{ row.pro }}</div>
            <div class="sla-cell sla-cell--gold">{{ row.enterprise }}</div>
          </div>
        </div>
      </div>
    </section>

    <!-- FAQ -->
    <section class="faq-section">
      <div class="section-inner faq-inner">
        <div>
          <div class="section-label">COMMON QUESTIONS</div>
          <h2 class="section-heading">Quick <span class="gold">Answers</span></h2>
        </div>
        <div class="faq-list">
          <div
            class="faq-item"
            v-for="(item, i) in faqs"
            :key="i"
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
      </div>
    </section>

    <!-- Contact CTA -->
    <section class="cta-section">
      <div class="cta-inner">
        <h2 class="cta-title">Still Need Help?</h2>
        <p class="cta-desc">Our team is standing by. Send us a message and we'll get back to you promptly.</p>
        <router-link to="/contact" class="btn-primary">CONTACT SUPPORT</router-link>
      </div>
    </section>

    <LandingFooter />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import LandingNav from '@/components/landing/LandingNav.vue'
import LandingFooter from '@/components/landing/LandingFooter.vue'

const openFaq = ref<number | null>(null)

const channels = [
  {
    title: 'Documentation',
    desc: 'Step-by-step guides, API references, and integration setup instructions — the fastest way to find an answer.',
    meta: 'Available 24/7 · Self-service',
    cta: 'BROWSE DOCS',
    link: '/docs',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="24" height="24"><path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z"/><polyline points="14,2 14,8 20,8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/><polyline points="10,9 9,9 8,9"/></svg>`,
  },
  {
    title: 'Email Support',
    desc: 'Send a detailed message and our engineering support team will investigate and respond with a solution.',
    meta: 'Response within 1 business day · All plans',
    cta: 'SEND A MESSAGE',
    link: '/contact',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="24" height="24"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></svg>`,
  },
  {
    title: 'Priority Support',
    desc: 'Professional and Enterprise customers receive faster response times and direct access to senior engineers for critical issues.',
    meta: 'Same-day response · Pro & Enterprise',
    cta: 'UPGRADE PLAN',
    link: '/sign-up',
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="24" height="24"><polygon points="12,2 15.09,8.26 22,9.27 17,14.14 18.18,21.02 12,17.77 5.82,21.02 7,14.14 2,9.27 8.91,8.26 12,2"/></svg>`,
  },
]

const slaRows = [
  { type: 'Documentation & FAQs', starter: '24/7', pro: '24/7', enterprise: '24/7' },
  { type: 'Email support', starter: '3 business days', pro: '1 business day', enterprise: '4 hours' },
  { type: 'Critical incident support', starter: '—', pro: 'Same day', enterprise: '1 hour' },
  { type: 'Dedicated account manager', starter: '—', pro: '—', enterprise: 'Included' },
  { type: 'Phone / video call support', starter: '—', pro: '—', enterprise: 'On request' },
]

const faqs = [
  {
    q: 'How do I report a bug or unexpected behaviour?',
    a: 'Use the Contact page to send us a detailed report. Include the service ID, the time the issue occurred, and any relevant screenshots or error messages. We triage bug reports within one business day.',
  },
  {
    q: 'My alerts stopped working — what should I check first?',
    a: 'First verify your integration is still connected under Account → Integrations. Check that the destination (email address, Discord webhook URL) is still valid. If the issue persists, contact support with your account email and integration type.',
  },
  {
    q: 'How do I request a feature?',
    a: 'Send your feature request via the Contact page or email product@cloudpulse.io. We review all requests and add high-demand features to our roadmap. You can track what\'s coming on the Updates page.',
  },
  {
    q: 'Can I get a refund?',
    a: 'Yes. If you cancel within 14 days of your first payment and are not satisfied, we will issue a full refund — no questions asked. After 14 days, you can cancel at any time and your plan will run until the end of the current billing period.',
  },
  {
    q: 'I forgot my password — how do I reset it?',
    a: 'Click "Forgot password?" on the Sign In page. Enter your email address and we will send you a secure reset link within a few minutes. If you do not receive the email, check your spam folder or contact support.',
  },
  {
    q: 'How do I cancel my subscription?',
    a: 'Go to Account → Billing and click "Cancel Plan". Your access will continue until the end of your current billing period. Your monitoring data is retained for 30 days after cancellation so you can export it if needed.',
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
.hero-content { position: relative; z-index: 1; max-width: 680px; margin: 0 auto; }
.page-eyebrow {
  display: flex; align-items: center; gap: 16px; justify-content: center;
  margin-bottom: 24px; font-size: 11px; font-weight: 600; letter-spacing: 0.25em; color: #D4AF37;
}
.eyebrow-line { width: 40px; height: 1px; background: rgba(212,175,55,0.4); }
.page-title {
  font-family: 'Marcellus', Georgia, serif;
  font-size: clamp(36px, 6vw, 64px);
  font-weight: 400; letter-spacing: 0.06em; color: #F2F0E4; line-height: 1.15; margin-bottom: 24px;
}
.gold { color: #D4AF37; }
.page-sub { font-size: 16px; color: #666; font-weight: 300; line-height: 1.8; max-width: 560px; margin: 0 auto; }

.section-inner { max-width: 1100px; margin: 0 auto; }
.section-label { font-size: 10px; font-weight: 600; letter-spacing: 0.3em; color: #D4AF37; margin-bottom: 16px; }
.section-heading {
  font-family: 'Marcellus', Georgia, serif;
  font-size: clamp(26px, 4vw, 42px); font-weight: 400; color: #F2F0E4;
  letter-spacing: 0.05em; line-height: 1.2; margin-bottom: 48px;
}

/* Channels */
.channels-section { padding: 100px 32px; background: #0E0E0E; border-bottom: 1px solid rgba(212,175,55,0.1); }
.channels-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; }
.channel-card {
  background: #141414; border: 1px solid rgba(212,175,55,0.12); padding: 36px 28px;
  display: flex; flex-direction: column; gap: 12px;
  transition: border-color 0.3s, background 0.3s;
}
.channel-card:hover { border-color: rgba(212,175,55,0.35); background: #18160e; }
.channel-icon { color: #D4AF37; display: flex; margin-bottom: 4px; }
.channel-title { font-family: 'Marcellus', Georgia, serif; font-size: 16px; letter-spacing: 0.08em; color: #F2F0E4; }
.channel-desc { font-size: 16px; color: #777; line-height: 1.75; font-weight: 300; flex: 1; }
.channel-meta { font-size: 11px; color: #555; letter-spacing: 0.05em; font-weight: 300; }
.channel-cta {
  display: inline-block; margin-top: 8px;
  font-size: 10px; font-weight: 700; letter-spacing: 0.18em;
  color: #D4AF37; border: 1px solid rgba(212,175,55,0.3); text-decoration: none;
  padding: 10px 18px; align-self: flex-start;
  transition: background 0.3s, border-color 0.3s;
}
.channel-cta:hover { background: rgba(212,175,55,0.08); border-color: #D4AF37; }

/* SLA */
.sla-section { padding: 100px 32px; border-bottom: 1px solid rgba(212,175,55,0.1); }
.sla-table { border: 1px solid rgba(212,175,55,0.15); }
.sla-row {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  border-bottom: 1px solid rgba(212,175,55,0.08);
}
.sla-row:last-child { border-bottom: none; }
.sla-header { background: rgba(212,175,55,0.04); }
.sla-cell {
  padding: 16px 20px;
  font-size: 16px; color: #777; font-weight: 300;
  border-right: 1px solid rgba(212,175,55,0.08);
  display: flex; align-items: center;
}
.sla-cell:last-child { border-right: none; }
.sla-cell--label { color: #F2F0E4; font-weight: 400; }
.sla-header .sla-cell { font-size: 10px; font-weight: 600; letter-spacing: 0.2em; color: #D4AF37; }
.sla-na { color: #333; }
.sla-cell--gold { color: #D4AF37; }

/* FAQ */
.faq-section { padding: 100px 32px; background: #0E0E0E; border-bottom: 1px solid rgba(212,175,55,0.1); }
.faq-inner { display: grid; grid-template-columns: 280px 1fr; gap: 80px; align-items: start; }
.faq-list { display: flex; flex-direction: column; }
.faq-item {
  border: 1px solid rgba(212,175,55,0.12); border-bottom: none; cursor: pointer;
  transition: background 0.3s; user-select: none;
}
.faq-item:last-child { border-bottom: 1px solid rgba(212,175,55,0.12); }
.faq-item:hover { background: rgba(212,175,55,0.02); }
.faq-item--open { background: rgba(212,175,55,0.04); }
.faq-question {
  display: flex; align-items: center; justify-content: space-between; gap: 16px;
  padding: 18px 20px; font-size: 16px; font-weight: 500; letter-spacing: 0.03em; color: #F2F0E4;
}
.faq-toggle {
  font-size: 18px; color: #D4AF37; flex-shrink: 0;
  width: 22px; height: 22px; text-align: center; display: flex; align-items: center; justify-content: center;
  border: 1px solid rgba(212,175,55,0.3);
  transition: transform 0.35s cubic-bezier(0.4, 0, 0.2, 1);
}
.faq-item--open .faq-toggle { transform: rotate(45deg); }
.faq-answer {
  max-height: 0; overflow: hidden; padding: 0 20px;
  font-size: 16px; color: #777; line-height: 1.8; font-weight: 300;
  border-top: 1px solid transparent;
  transition: max-height 0.4s cubic-bezier(0.4, 0, 0.2, 1), padding 0.4s cubic-bezier(0.4, 0, 0.2, 1), border-color 0.3s;
}
.faq-item--open .faq-answer { max-height: 300px; padding: 14px 20px 20px; border-top-color: rgba(212,175,55,0.1); }

/* CTA */
.cta-section { padding: 80px 32px; text-align: center; background: #141414; border-top: 1px solid rgba(212,175,55,0.15); }
.cta-inner { max-width: 480px; margin: 0 auto; }
.cta-title { font-family: 'Marcellus', Georgia, serif; font-size: clamp(22px, 3vw, 34px); color: #F2F0E4; letter-spacing: 0.05em; margin-bottom: 12px; }
.cta-desc { font-size: 16px; color: #666; font-weight: 300; margin-bottom: 32px; }
.btn-primary {
  font-size: 11px; font-weight: 700; letter-spacing: 0.2em;
  color: #0A0A0A; background: #D4AF37; text-decoration: none;
  padding: 14px 32px; transition: background 0.3s; display: inline-block;
}
.btn-primary:hover { background: #F2E8C4; }

@media (max-width: 1024px) {
  .channels-grid { grid-template-columns: 1fr; max-width: 560px; }
  .faq-inner { grid-template-columns: 1fr; gap: 40px; }
  .sla-row { grid-template-columns: 1.5fr 1fr 1fr 1fr; }
}
@media (max-width: 640px) {
  .page-hero { padding: 120px 20px 72px; }
  .channels-section, .sla-section, .faq-section, .cta-section { padding: 72px 20px; }
  .sla-row { grid-template-columns: 1fr 1fr; }
  .sla-header .sla-cell:nth-child(1) { display: none; }
  .sla-cell--label { grid-column: 1 / -1; border-right: none; border-bottom: 1px solid rgba(212,175,55,0.08); }
}
</style>
