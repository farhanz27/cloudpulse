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
          <span>PRICING</span>
          <div class="eyebrow-line" aria-hidden="true"></div>
        </div>
        <h1 class="page-title">Transparent Pricing,<br><span class="gold">No Surprises</span></h1>
        <p class="page-sub">
          Start free. Upgrade when you need more. Cancel anytime.
          Every plan includes our core monitoring, alerts, and status pages.
        </p>
      </div>
    </section>

    <!-- Billing Toggle + Cards -->
    <section class="plans-section">
      <div class="plans-inner">
        <div class="billing-toggle">
          <button class="toggle-btn" :class="{ 'toggle-btn--active': billing === 'monthly' }" @click="billing = 'monthly'">MONTHLY</button>
          <button class="toggle-btn" :class="{ 'toggle-btn--active': billing === 'annually' }" @click="billing = 'annually'">
            ANNUALLY
            <span class="save-badge">SAVE 20%</span>
          </button>
        </div>

        <div class="pricing-grid">
          <div
            class="plan-card"
            v-for="plan in displayPlans"
            :key="plan.name"
            :class="{ 'plan-card--featured': plan.featured }"
          >
            <div class="plan-badge" v-if="plan.badge">{{ plan.badge }}</div>
            <div class="plan-corner plan-corner--tl" aria-hidden="true"></div>
            <div class="plan-corner plan-corner--br" aria-hidden="true"></div>
            <div class="plan-name">{{ plan.name }}</div>
            <div class="plan-desc">{{ plan.tagline }}</div>
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
            <p class="plan-note" v-if="plan.note">{{ plan.note }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Feature Comparison Table -->
    <section class="comparison-section">
      <div class="comparison-inner">
        <div class="section-label centered">FULL COMPARISON</div>
        <h2 class="section-heading centered">Everything <span class="gold">Side by Side</span></h2>

        <div class="comparison-table">
          <!-- Header -->
          <div class="comp-row comp-row--header">
            <div class="comp-cell comp-cell--feature"></div>
            <div class="comp-cell comp-cell--plan">STARTER</div>
            <div class="comp-cell comp-cell--plan comp-cell--featured">PROFESSIONAL</div>
            <div class="comp-cell comp-cell--plan">ENTERPRISE</div>
          </div>

          <!-- Groups -->
          <template v-for="group in comparisonGroups" :key="group.title">
            <div class="comp-group-header">{{ group.title }}</div>
            <div class="comp-row" v-for="row in group.rows" :key="row.feature">
              <div class="comp-cell comp-cell--feature">{{ row.feature }}</div>
              <div class="comp-cell" :class="{ 'comp-cell--featured': i === 1 }" v-for="(val, i) in row.values" :key="i">
                <span v-if="val === true" class="comp-yes" aria-label="Included">◆</span>
                <span v-else-if="val === false" class="comp-no" aria-label="Not included">—</span>
                <span v-else class="comp-val">{{ val }}</span>
              </div>
            </div>
          </template>

        </div>
      </div>
    </section>

    <!-- FAQ -->
    <section class="faq-section">
      <div class="faq-inner">
        <div class="section-label centered">FREQUENTLY ASKED</div>
        <h2 class="section-heading centered">Pricing <span class="gold">Questions</span></h2>
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

    <!-- CTA -->
    <section class="cta-section">
      <div class="cta-inner">
        <h2 class="cta-title">Start for Free,<br><span class="gold">Upgrade Anytime</span></h2>
        <p class="cta-desc">No credit card required. Your first 5 services are monitored free, forever.</p>
        <div class="cta-buttons">
          <router-link to="/sign-up" class="btn-primary">BEGIN MONITORING FREE</router-link>
          <router-link to="/contact" class="btn-outline">TALK TO SALES</router-link>
        </div>
        <p class="cta-note">Free plan · No credit card · Cancel anytime</p>
      </div>
    </section>

    <LandingFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import LandingNav from '@/components/landing/LandingNav.vue'
import LandingFooter from '@/components/landing/LandingFooter.vue'

const billing = ref<'monthly' | 'annually'>('monthly')
const openFaq = ref<number | null>(null)

const plans = [
  {
    name: 'STARTER',
    tagline: 'Perfect for personal projects and small teams.',
    monthlyPrice: 'Free',
    monthlyPeriod: null,
    badge: null,
    featured: false,
    features: [
      '5 monitored services',
      '5-minute check intervals',
      'Email, Telegram & Discord alerts',
      '30-day uptime history',
      '1 public status page',
      'Basic incident tracking',
      'Community support',
    ],
    ctaLabel: 'START FREE',
    ctaLink: '/sign-up',
    note: null,
  },
  {
    name: 'PROFESSIONAL',
    tagline: 'For growing teams that need faster checks and full alert coverage.',
    monthlyPrice: 'RM20',
    monthlyPeriod: '/month',
    badge: 'COMING SOON',
    featured: true,
    features: [
      '50 monitored services',
      '1-minute check intervals',
      'All alert channels (Email, Telegram, Discord, Slack, Teams, Webhooks)',
      '12-month uptime history',
      'Up to 10 status pages',
      'Full incident management',
      'On-call scheduling',
      'SSL certificate monitoring',
      'Response time SLA tracking',
      'API access',
      'Priority support',
    ],
    ctaLabel: 'REQUEST EARLY ACCESS',
    ctaLink: '/contact',
    note: null,
  },
  {
    name: 'ENTERPRISE',
    tagline: 'For organisations that need unlimited scale and dedicated support.',
    monthlyPrice: 'Custom',
    monthlyPeriod: null,
    badge: null,
    featured: false,
    features: [
      'Unlimited monitored services',
      '30-second check intervals',
      'All channels + custom integrations',
      'Unlimited uptime history',
      'Unlimited status pages with custom domain',
      'Advanced incident workflows & escalations',
      'Private endpoint monitoring (agent)',
      'Multi-region redundant checks',
      'Dedicated SLA with uptime guarantee',
      'SSO / SAML & audit logs',
      'Custom data retention policies',
      'Dedicated account manager',
      '24/7 premium support with SLA',
    ],
    ctaLabel: 'CONTACT US',
    ctaLink: '/contact',
    note: null,
  },
]

const displayPlans = computed(() =>
  plans.map(plan => {
    if (billing.value === 'annually' && plan.monthlyPrice === 'RM20') {
      return { ...plan, price: 'RM16', period: '/month', annualNote: null }
    }
    return { ...plan, price: plan.monthlyPrice, period: plan.monthlyPeriod, annualNote: null }
  })
)

const comparisonGroups = [
  {
    title: 'MONITORING',
    rows: [
      { feature: 'Monitored services', values: ['5', '50', 'Unlimited'] },
      { feature: 'Check interval', values: ['5 min', '1 min', '30 sec'] },
      { feature: 'Global check regions', values: ['3', '15+', '15+'] },
      { feature: 'HTTP / HTTPS checks', values: [true, true, true] },
      { feature: 'TCP port checks', values: [true, true, true] },
      { feature: 'DNS checks', values: [false, true, true] },
      { feature: 'SSL certificate monitoring', values: [false, true, true] },
      { feature: 'Private endpoint (agent)', values: [false, false, true] },
      { feature: 'Custom request headers', values: [false, true, true] },
    ],
  },
  {
    title: 'ALERTS & NOTIFICATIONS',
    rows: [
      { feature: 'Email alerts', values: [true, true, true] },
      { feature: 'Telegram alerts', values: [true, true, true] },
      { feature: 'Discord alerts', values: [true, true, true] },
      { feature: 'Slack', values: [false, true, true] },
      { feature: 'Microsoft Teams', values: [false, true, true] },
      { feature: 'Custom webhooks', values: [false, true, true] },
      { feature: 'Alert escalation rules', values: [false, true, true] },
      { feature: 'On-call scheduling', values: [false, true, true] },
      { feature: 'Per-service routing', values: [false, true, true] },
    ],
  },
  {
    title: 'INCIDENT MANAGEMENT',
    rows: [
      { feature: 'Basic incident tracking', values: [true, true, true] },
      { feature: 'Full incident timeline', values: [false, true, true] },
      { feature: 'Incident status workflow', values: [false, true, true] },
      { feature: 'Post-incident reports', values: [false, true, true] },
      { feature: 'Advanced escalation workflows', values: [false, false, true] },
    ],
  },
  {
    title: 'STATUS PAGES',
    rows: [
      { feature: 'Public status pages', values: ['1', '10', 'Unlimited'] },
      { feature: 'Real-time incident updates', values: [true, true, true] },
      { feature: 'Maintenance windows', values: [false, true, true] },
      { feature: 'Custom domain', values: [false, false, true] },
      { feature: 'Status page embed widget', values: [false, true, true] },
    ],
  },
  {
    title: 'ANALYTICS & DATA',
    rows: [
      { feature: 'Uptime history', values: ['30 days', '12 months', 'Unlimited'] },
      { feature: 'Latency percentiles (P95/P99)', values: [false, true, true] },
      { feature: 'SLA compliance tracking', values: [false, true, true] },
      { feature: 'CSV / PDF export', values: [false, true, true] },
      { feature: 'Custom data retention', values: [false, false, true] },
      { feature: 'API access', values: [false, true, true] },
    ],
  },
  {
    title: 'SECURITY & COMPLIANCE',
    rows: [
      { feature: 'TLS 1.3 in transit', values: [true, true, true] },
      { feature: 'AES-256 at rest', values: [true, true, true] },
      { feature: 'SSO / SAML', values: [false, false, true] },
      { feature: 'Audit logs', values: [false, false, true] },
      { feature: 'Dedicated SLA & uptime guarantee', values: [false, false, true] },
    ],
  },
  {
    title: 'SUPPORT',
    rows: [
      { feature: 'Documentation & FAQ', values: [true, true, true] },
      { feature: 'Email support', values: ['3 days', '1 business day', '4 hours'] },
      { feature: 'Priority support', values: [false, true, true] },
      { feature: 'Dedicated account manager', values: [false, false, true] },
    ],
  },
]

const faqs = [
  {
    q: 'Can I start on the free plan and upgrade later?',
    a: 'Yes. The Starter plan is free forever with no credit card required. When you are ready to upgrade, you can do so from your account settings at any time. Your existing monitoring configuration and data are preserved.',
  },
  {
    q: 'What happens if I exceed my plan limits?',
    a: 'You will receive an in-app notification and an email when you approach your service limit. You can upgrade your plan at any time to add more capacity. We will not automatically disable monitoring without warning.',
  },
  {
    q: 'Do you offer a discount for annual billing?',
    a: 'Yes. Switching to annual billing on the Professional plan saves 20%, bringing the effective monthly cost from RM20 to RM16.',
  },
  {
    q: 'Can I cancel at any time?',
    a: 'Yes. You can cancel your subscription from your account settings at any time. Your plan remains active until the end of the current billing period. We do not charge cancellation fees.',
  },
  {
    q: 'What payment methods are accepted?',
    a: 'We accept all major credit and debit cards (Visa, Mastercard, American Express) via Stripe. Enterprise customers can also pay by bank transfer or purchase order.',
  },
  {
    q: 'Is there a free trial for the Professional plan?',
    a: 'Yes. New accounts receive a 14-day free trial of Professional features automatically. No credit card is required to start the trial. At the end of the trial period, your account reverts to the Starter plan unless you add a payment method.',
  },
  {
    q: 'How does Enterprise pricing work?',
    a: 'Enterprise pricing is customised based on your service count, check frequency requirements, data retention needs, and support SLA. Contact our sales team for a quote — most Enterprise agreements are finalised within a week.',
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
.hero-content { position: relative; z-index: 1; max-width: 680px; margin: 0 auto; }
.page-eyebrow {
  display: flex; align-items: center; gap: 16px; justify-content: center;
  margin-bottom: 24px; font-size: 11px; font-weight: 600; letter-spacing: 0.25em; color: #D4AF37;
}
.eyebrow-line { width: 40px; height: 1px; background: rgba(212,175,55,0.4); }
.page-title {
  font-family: 'Marcellus', Georgia, serif;
  font-size: clamp(36px, 6vw, 64px);
  font-weight: 400; letter-spacing: 0.06em; color: #F2F0E4; line-height: 1.1; margin-bottom: 20px;
}
.gold { color: #D4AF37; }
.page-sub { font-size: 16px; color: #666; font-weight: 300; line-height: 1.8; max-width: 520px; margin: 0 auto; }

/* Plans */
.plans-section { padding: 80px 32px 100px; border-bottom: 1px solid rgba(212,175,55,0.1); }
.plans-inner { max-width: 1100px; margin: 0 auto; }

.billing-toggle {
  display: flex; align-items: center; justify-content: center; gap: 4px; margin-bottom: 56px;
}
.toggle-btn {
  font-family: 'Josefin Sans', 'Inter', sans-serif;
  font-size: 11px; font-weight: 600; letter-spacing: 0.18em; color: #555;
  background: transparent; border: 1px solid rgba(212,175,55,0.15);
  padding: 10px 24px; cursor: pointer;
  transition: color 0.3s, border-color 0.3s, background 0.3s;
  display: flex; align-items: center; gap: 10px;
}
.toggle-btn:hover { color: #F2F0E4; border-color: rgba(212,175,55,0.4); }
.toggle-btn--active { color: #0A0A0A; background: #D4AF37; border-color: #D4AF37; }
.save-badge {
  font-size: 9px; font-weight: 700; letter-spacing: 0.1em; color: #0A0A0A;
  background: rgba(10,10,10,0.2); padding: 2px 7px;
}
.toggle-btn:not(.toggle-btn--active) .save-badge {
  color: #D4AF37; background: rgba(212,175,55,0.12);
}

.pricing-grid {
  display: grid; grid-template-columns: repeat(3, 1fr); gap: 32px; align-items: stretch;
}

.plan-card {
  position: relative; background: #141414; border: 1px solid rgba(212,175,55,0.15);
  padding: 52px 32px 36px; display: flex; flex-direction: column;
  transition: border-color 0.35s, box-shadow 0.35s, transform 0.35s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}
.plan-card:hover {
  border-color: rgba(212,175,55,0.45);
  box-shadow: 0 16px 48px rgba(0,0,0,0.4), 0 0 0 1px rgba(212,175,55,0.08);
  transform: translateY(-6px);
}
.plan-card--featured { border-color: rgba(212,175,55,0.5); background: #16140e; }
.plan-card--featured:hover {
  border-color: #D4AF37;
  box-shadow: 0 20px 56px rgba(212,175,55,0.15), 0 0 0 1px rgba(212,175,55,0.2);
}

.plan-corner { position: absolute; width: 14px; height: 14px; pointer-events: none; }
.plan-corner--tl { top: 6px; left: 6px; border-top: 2px solid rgba(212,175,55,0.25); border-left: 2px solid rgba(212,175,55,0.25); }
.plan-corner--br { bottom: 6px; right: 6px; border-bottom: 2px solid rgba(212,175,55,0.25); border-right: 2px solid rgba(212,175,55,0.25); }
.plan-card--featured .plan-corner--tl,
.plan-card--featured .plan-corner--br { border-color: #D4AF37; }

.plan-badge {
  position: absolute; top: -13px; left: 50%; transform: translateX(-50%);
  font-size: 9px; font-weight: 700; letter-spacing: 0.25em; color: #0A0A0A;
  background: #D4AF37; padding: 5px 14px; white-space: nowrap;
}

.plan-name { font-family: 'Marcellus', Georgia, serif; font-size: 18px; letter-spacing: 0.15em; color: #F2F0E4; margin-bottom: 8px; }
.plan-desc { font-size: 16px; color: #555; font-weight: 300; line-height: 1.6; margin-bottom: 20px; }
.plan-price { display: flex; align-items: baseline; gap: 4px; margin-bottom: 6px; }
.plan-amount { font-family: 'Marcellus', Georgia, serif; font-size: 36px; color: #D4AF37; line-height: 1; }
.plan-period { font-size: 13px; color: #555; font-weight: 300; }
.plan-annual-note { font-size: 11px; color: #555; font-weight: 300; margin-bottom: 12px; }
.plan-divider { height: 1px; background: rgba(212,175,55,0.15); margin: 18px 0 24px; }
.plan-feature-list { list-style: none; padding: 0; margin: 0 0 32px; display: flex; flex-direction: column; gap: 10px; flex: 1; }
.plan-feature-list li { font-size: 16px; color: #888; font-weight: 300; display: flex; align-items: flex-start; gap: 10px; line-height: 1.5; }
.plan-check { color: #D4AF37; font-size: 7px; flex-shrink: 0; margin-top: 4px; }
.plan-cta {
  display: block; text-align: center; font-size: 11px; font-weight: 600; letter-spacing: 0.2em;
  color: #D4AF37; border: 1px solid rgba(212,175,55,0.4); padding: 13px; text-decoration: none;
  transition: background 0.3s, color 0.3s, border-color 0.3s; margin-top: auto;
}
.plan-cta:hover { background: rgba(212,175,55,0.08); border-color: #D4AF37; }
.plan-cta--featured { background: #D4AF37; color: #0A0A0A; border-color: #D4AF37; }
.plan-cta--featured:hover { background: #F2E8C4; color: #0A0A0A; }
.plan-note { font-size: 11px; color: #444; text-align: center; margin-top: 12px; letter-spacing: 0.05em; }

/* Comparison table */
.comparison-section { padding: 100px 32px; background: #0E0E0E; border-bottom: 1px solid rgba(212,175,55,0.1); }
.comparison-inner { max-width: 1100px; margin: 0 auto; }
.section-label { font-size: 10px; font-weight: 600; letter-spacing: 0.3em; color: #D4AF37; margin-bottom: 16px; }
.section-label.centered { text-align: center; }
.section-heading {
  font-family: 'Marcellus', Georgia, serif;
  font-size: clamp(28px, 4vw, 44px); font-weight: 400; color: #F2F0E4;
  letter-spacing: 0.05em; line-height: 1.2; margin-bottom: 56px;
}
.section-heading.centered { text-align: center; }

.comparison-table { border: 1px solid rgba(212,175,55,0.12); }

.comp-row {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  border-bottom: 1px solid rgba(212,175,55,0.07);
}
.comp-row:last-child { border-bottom: none; }
.comp-row--header { background: rgba(212,175,55,0.03); }
.comp-row--cta { background: rgba(212,175,55,0.02); border-top: 1px solid rgba(212,175,55,0.12); }

.comp-group-header {
  font-size: 9px; font-weight: 700; letter-spacing: 0.25em; color: #D4AF37;
  padding: 12px 20px;
  background: rgba(212,175,55,0.04); border-bottom: 1px solid rgba(212,175,55,0.08);
}

.comp-cell {
  padding: 13px 20px;
  font-size: 16px; color: #777; font-weight: 300;
  border-right: 1px solid rgba(212,175,55,0.07);
  display: flex; align-items: center;
}
.comp-cell:last-child { border-right: none; }
.comp-cell--feature { color: #ccc; font-weight: 300; }
.comp-cell--plan {
  font-size: 10px; font-weight: 600; letter-spacing: 0.2em; color: #888;
  justify-content: center;
}
.comp-cell--featured { background: rgba(212,175,55,0.04); color: #D4AF37; }
.comp-row--cta .comp-cell { justify-content: center; padding: 20px; }

.comp-yes { color: #D4AF37; font-size: 8px; }
.comp-no { color: #333; font-size: 16px; }
.comp-val { color: #888; font-size: 16px; }

.comp-cta-btn {
  font-size: 10px; font-weight: 700; letter-spacing: 0.18em;
  color: #D4AF37; border: 1px solid rgba(212,175,55,0.3); text-decoration: none;
  padding: 9px 18px; transition: background 0.3s, border-color 0.3s;
}
.comp-cta-btn:hover { background: rgba(212,175,55,0.08); border-color: #D4AF37; }
.comp-cta-btn--gold { background: #D4AF37; color: #0A0A0A; border-color: #D4AF37; }
.comp-cta-btn--gold:hover { background: #F2E8C4; }

/* FAQ */
.faq-section { padding: 100px 32px; border-bottom: 1px solid rgba(212,175,55,0.1); }
.faq-inner { max-width: 800px; margin: 0 auto; }
.faq-list { display: flex; flex-direction: column; }
.faq-item {
  border: 1px solid rgba(212,175,55,0.12); border-bottom: none; cursor: pointer;
  transition: background 0.3s; user-select: none;
}
.faq-item:last-child { border-bottom: 1px solid rgba(212,175,55,0.12); }
.faq-item:hover { background: rgba(212,175,55,0.02); }
.faq-item--open { background: rgba(212,175,55,0.04); }
.faq-question {
  display: flex; align-items: center; justify-content: space-between; gap: 20px;
  padding: 22px 24px; font-size: 16px; font-weight: 500; letter-spacing: 0.03em; color: #F2F0E4;
}
.faq-toggle {
  font-size: 20px; color: #D4AF37; flex-shrink: 0;
  width: 24px; height: 24px; display: flex; align-items: center; justify-content: center;
  border: 1px solid rgba(212,175,55,0.3);
  transition: transform 0.35s cubic-bezier(0.4, 0, 0.2, 1);
}
.faq-item--open .faq-toggle { transform: rotate(45deg); }
.faq-answer {
  max-height: 0; overflow: hidden; padding: 0 24px;
  font-size: 16px; color: #888; line-height: 1.8; font-weight: 300;
  border-top: 1px solid transparent;
  transition: max-height 0.45s cubic-bezier(0.4, 0, 0.2, 1), padding 0.45s cubic-bezier(0.4, 0, 0.2, 1), border-color 0.3s;
}
.faq-item--open .faq-answer { max-height: 400px; padding: 16px 24px 22px; border-top-color: rgba(212,175,55,0.1); }

/* CTA */
.cta-section { padding: 100px 32px; text-align: center; background: #141414; border-top: 1px solid rgba(212,175,55,0.15); }
.cta-inner { max-width: 600px; margin: 0 auto; }
.cta-title { font-family: 'Marcellus', Georgia, serif; font-size: clamp(28px, 5vw, 52px); font-weight: 400; color: #F2F0E4; letter-spacing: 0.06em; line-height: 1.15; margin-bottom: 16px; }
.cta-desc { font-size: 16px; color: #666; font-weight: 300; margin-bottom: 36px; }
.cta-buttons { display: flex; align-items: center; justify-content: center; gap: 16px; flex-wrap: wrap; margin-bottom: 16px; }
.cta-note { font-size: 11px; color: #3a3a3a; letter-spacing: 0.1em; }

.btn-primary { font-size: 11px; font-weight: 700; letter-spacing: 0.2em; color: #0A0A0A; background: #D4AF37; text-decoration: none; padding: 14px 32px; transition: background 0.3s, box-shadow 0.3s; display: inline-block; }
.btn-primary:hover { background: #F2E8C4; box-shadow: 0 0 20px rgba(212,175,55,0.3); }
.btn-outline { font-size: 11px; font-weight: 700; letter-spacing: 0.2em; color: #D4AF37; border: 1px solid rgba(212,175,55,0.4); text-decoration: none; padding: 14px 32px; transition: background 0.3s, border-color 0.3s; display: inline-block; }
.btn-outline:hover { background: rgba(212,175,55,0.07); border-color: #D4AF37; }

@media (max-width: 900px) {
  .pricing-grid { grid-template-columns: 1fr; max-width: 480px; margin: 0 auto; }
  .comp-row { grid-template-columns: 1.5fr 1fr 1fr 1fr; }
  .comp-cell { padding: 11px 12px; font-size: 14px; }
  .comp-cell--feature { font-size: 14px; }
}
@media (max-width: 640px) {
  .page-hero { padding: 120px 20px 72px; }
  .plans-section, .comparison-section, .faq-section, .cta-section { padding: 72px 20px; }
  .comp-row { grid-template-columns: 1.2fr 1fr 1fr 1fr; }
  .comp-cell { padding: 10px 8px; font-size: 13px; }
  .comp-group-header { padding: 10px 12px; }
  .billing-toggle { flex-direction: column; }
}
</style>
