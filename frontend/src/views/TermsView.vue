<template>
  <div class="page">
    <LandingNav />

    <div class="page-hero">
      <div class="page-eyebrow">
        <div class="eyebrow-line" aria-hidden="true"></div>
        <span>LEGAL</span>
        <div class="eyebrow-line" aria-hidden="true"></div>
      </div>
      <h1 class="page-title">Terms of <span class="gold">Service</span></h1>
      <p class="page-sub">Last updated: January 1, {{ year }}</p>
    </div>

    <div class="page-content">
      <div class="legal-body">
        <section class="legal-section" v-for="section in sections" :key="section.title">
          <h2 class="legal-heading">{{ section.title }}</h2>
          <p class="legal-text" v-for="para in section.paragraphs" :key="para">{{ para }}</p>
        </section>
      </div>
    </div>

    <LandingFooter />
  </div>
</template>

<script setup lang="ts">
import LandingNav from '@/components/landing/LandingNav.vue'
import LandingFooter from '@/components/landing/LandingFooter.vue'

const year = new Date().getFullYear()

const sections = [
  {
    title: '1. Acceptance of Terms',
    paragraphs: [
      'By accessing or using CloudPulse ("Service"), you agree to be bound by these Terms of Service ("Terms"). If you do not agree to these Terms, you may not use the Service.',
      'These Terms apply to all users of the Service, including users who are also contributors of content, information, and other materials.',
    ],
  },
  {
    title: '2. Description of Service',
    paragraphs: [
      'CloudPulse provides uptime monitoring, alerting, and incident management services for cloud infrastructure and web endpoints. The Service is provided on a subscription basis with tiers as described on our Pricing page.',
      'We reserve the right to modify, suspend, or discontinue the Service (or any part thereof) at any time, with or without notice.',
    ],
  },
  {
    title: '3. User Accounts',
    paragraphs: [
      'You must register for an account to access certain features of the Service. You are responsible for maintaining the confidentiality of your account credentials and for all activities that occur under your account.',
      'You agree to notify us immediately of any unauthorized use of your account. CloudPulse will not be liable for any losses arising from unauthorized account use.',
    ],
  },
  {
    title: '4. Acceptable Use',
    paragraphs: [
      'You agree not to misuse the Service or help anyone else do so. Prohibited uses include: using the Service to monitor systems you do not own or have explicit permission to monitor, generating excessive or abusive traffic, attempting to reverse-engineer or extract source code, and violating any applicable law or regulation.',
    ],
  },
  {
    title: '5. Payment and Billing',
    paragraphs: [
      'Paid plans are billed in advance on a monthly or annual basis. All fees are non-refundable except as required by law or as explicitly stated in our refund policy.',
      'We reserve the right to change pricing with 30 days notice. Continued use of the Service after pricing changes constitutes acceptance of the new pricing.',
    ],
  },
  {
    title: '6. Limitation of Liability',
    paragraphs: [
      'To the maximum extent permitted by law, CloudPulse shall not be liable for any indirect, incidental, special, consequential, or punitive damages resulting from your use of or inability to use the Service.',
      'Our total liability for any claim related to the Service shall not exceed the amount you paid for the Service in the 12 months preceding the claim.',
    ],
  },
  {
    title: '7. Contact',
    paragraphs: [
      'If you have questions about these Terms, please contact us at legal@cloudpulse.io or visit our Contact page.',
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
  text-align: center;
  padding: 140px 32px 80px;
  border-bottom: 1px solid rgba(212,175,55,0.1);
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
  font-size: clamp(36px, 6vw, 64px);
  font-weight: 400;
  letter-spacing: 0.08em;
  color: #F2F0E4;
  margin-bottom: 16px;
}
.gold { color: #D4AF37; }
.page-sub {
  font-size: 13px;
  color: #555;
  font-weight: 300;
  letter-spacing: 0.06em;
}

.page-content {
  max-width: 760px;
  margin: 0 auto;
  padding: 80px 32px;
}

.legal-body {
  display: flex;
  flex-direction: column;
  gap: 40px;
}

.legal-section { display: flex; flex-direction: column; gap: 12px; }

.legal-heading {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 16px;
  letter-spacing: 0.1em;
  color: #D4AF37;
  padding-bottom: 10px;
  border-bottom: 1px solid rgba(212,175,55,0.15);
}

.legal-text {
  font-size: 14px;
  color: #777;
  line-height: 1.85;
  font-weight: 300;
}

@media (max-width: 600px) {
  .page-content { padding: 48px 20px; }
  .page-hero { padding: 120px 20px 60px; }
}
</style>
