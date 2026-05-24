<template>
  <div class="page">
    <LandingNav />

    <div class="page-hero">
      <div class="page-eyebrow">
        <div class="eyebrow-line" aria-hidden="true"></div>
        <span>LEGAL</span>
        <div class="eyebrow-line" aria-hidden="true"></div>
      </div>
      <h1 class="page-title">Cookie <span class="gold">Policy</span></h1>
      <p class="page-sub">Last updated: January 1, {{ year }}</p>
    </div>

    <div class="page-content">
      <div class="legal-body">
        <section class="legal-section" v-for="section in sections" :key="section.title">
          <h2 class="legal-heading">{{ section.title }}</h2>
          <p class="legal-text" v-for="para in section.paragraphs" :key="para">{{ para }}</p>
          <div class="cookie-table" v-if="section.table">
            <div class="cookie-row cookie-row--header">
              <div class="cookie-cell">Cookie Name</div>
              <div class="cookie-cell">Purpose</div>
              <div class="cookie-cell">Duration</div>
              <div class="cookie-cell">Type</div>
            </div>
            <div class="cookie-row" v-for="row in section.table" :key="row.name">
              <div class="cookie-cell cookie-name">{{ row.name }}</div>
              <div class="cookie-cell">{{ row.purpose }}</div>
              <div class="cookie-cell">{{ row.duration }}</div>
              <div class="cookie-cell">
                <span class="cookie-type" :class="`cookie-type--${row.type}`">{{ row.type }}</span>
              </div>
            </div>
          </div>
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
    title: '1. What Are Cookies',
    paragraphs: [
      'Cookies are small text files placed on your device by websites you visit. They are widely used to make websites work correctly, improve user experience, and provide information to website owners.',
      'CloudPulse uses cookies and similar storage technologies (such as localStorage) to keep you signed in, remember your preferences, and understand how our platform is used.',
    ],
    table: null,
  },
  {
    title: '2. Cookies We Use',
    paragraphs: [
      'We use only the cookies necessary to operate the service. We do not use advertising cookies or sell data to ad networks.',
    ],
    table: [
      { name: 'cp_session', purpose: 'Keeps you authenticated between page loads', duration: 'Session', type: 'essential' },
      { name: 'cp_refresh', purpose: 'Allows session renewal without re-login', duration: '30 days', type: 'essential' },
      { name: 'cp_consent', purpose: 'Stores your cookie consent preference', duration: '12 months', type: 'essential' },
      { name: 'cp_theme', purpose: 'Remembers your display preferences', duration: '12 months', type: 'functional' },
      { name: '_ga', purpose: 'Google Analytics — anonymous page view tracking (only if consent given)', duration: '2 years', type: 'analytics' },
      { name: '_ga_*', purpose: 'Google Analytics session identifier (only if consent given)', duration: '2 years', type: 'analytics' },
    ],
  },
  {
    title: '3. Essential Cookies',
    paragraphs: [
      'Essential cookies are required for the platform to function. Without them, services like authentication and session management cannot work. These cookies cannot be disabled while using CloudPulse.',
      'We do not use essential cookies for any purpose other than operating the service.',
    ],
    table: null,
  },
  {
    title: '4. Functional Cookies',
    paragraphs: [
      'Functional cookies allow us to remember choices you make (such as display preferences) to provide a more personalised experience. These cookies do not track you across other websites.',
    ],
    table: null,
  },
  {
    title: '5. Analytics Cookies',
    paragraphs: [
      'We use Google Analytics to understand how visitors interact with our public pages. This data is anonymous and aggregated — we cannot identify individual users from it.',
      'Analytics cookies are only loaded if you accept our cookie policy. If you decline, no analytics data is collected. You can change your choice at any time by clearing your cookies or contacting us.',
    ],
    table: null,
  },
  {
    title: '6. Third-Party Cookies',
    paragraphs: [
      'We do not allow third-party advertising networks to set cookies on our site. The only third-party cookies present are Google Analytics cookies, and only when you have given consent.',
    ],
    table: null,
  },
  {
    title: '7. Managing Cookies',
    paragraphs: [
      'You can control cookies through your browser settings. Most browsers allow you to view, block, or delete cookies. Note that blocking essential cookies will prevent you from using the CloudPulse dashboard.',
      'To opt out of Google Analytics across all sites you visit, you can use the Google Analytics Opt-out Browser Add-on available at tools.google.com/dlpage/gaoptout.',
      'To withdraw consent on CloudPulse specifically, clear your browser cookies for cloudpulse.io. The consent banner will reappear on your next visit.',
    ],
    table: null,
  },
  {
    title: '8. Contact',
    paragraphs: [
      'If you have questions about our cookie use, contact us at privacy@cloudpulse.io or visit our Contact page.',
    ],
    table: null,
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
.eyebrow-line { width: 40px; height: 1px; background: rgba(212,175,55,0.4); }
.page-title {
  font-family: 'Marcellus', Georgia, serif;
  font-size: clamp(36px, 6vw, 64px);
  font-weight: 400;
  letter-spacing: 0.08em;
  color: #F2F0E4;
  margin-bottom: 16px;
}
.gold { color: #D4AF37; }
.page-sub { font-size: 13px; color: #555; font-weight: 300; letter-spacing: 0.06em; }

.page-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 80px 32px;
}

.legal-body {
  display: flex;
  flex-direction: column;
  gap: 48px;
}

.legal-section { display: flex; flex-direction: column; gap: 14px; }

.legal-heading {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 16px;
  letter-spacing: 0.1em;
  color: #D4AF37;
  padding-bottom: 10px;
  border-bottom: 1px solid rgba(212,175,55,0.15);
}

.legal-text {
  font-size: 16px;
  color: #777;
  line-height: 1.85;
  font-weight: 300;
}

/* Cookie Table */
.cookie-table {
  border: 1px solid rgba(212,175,55,0.12);
  overflow: hidden;
  margin-top: 4px;
}
.cookie-row {
  display: grid;
  grid-template-columns: 1.2fr 2fr 1fr 1fr;
  border-bottom: 1px solid rgba(212,175,55,0.07);
}
.cookie-row:last-child { border-bottom: none; }
.cookie-row--header { background: rgba(212,175,55,0.04); }
.cookie-cell {
  padding: 12px 16px;
  font-size: 16px;
  color: #777;
  font-weight: 300;
  border-right: 1px solid rgba(212,175,55,0.07);
  display: flex;
  align-items: center;
}
.cookie-cell:last-child { border-right: none; }
.cookie-row--header .cookie-cell {
  font-size: 9px;
  font-weight: 600;
  letter-spacing: 0.18em;
  color: #D4AF37;
}
.cookie-name {
  font-family: 'Courier New', monospace;
  font-size: 11px;
  color: #F2F0E4;
}
.cookie-type {
  font-size: 9px;
  font-weight: 700;
  letter-spacing: 0.12em;
  padding: 3px 8px;
}
.cookie-type--essential { background: rgba(212,175,55,0.1); color: #D4AF37; }
.cookie-type--functional { background: rgba(99,102,241,0.1); color: #818cf8; }
.cookie-type--analytics { background: rgba(34,197,94,0.08); color: #22c55e; }

@media (max-width: 640px) {
  .page-content { padding: 48px 20px; }
  .page-hero { padding: 120px 20px 60px; }
  .cookie-row { grid-template-columns: 1fr 1fr; }
  .cookie-row--header .cookie-cell:nth-child(2),
  .cookie-cell:nth-child(2) { grid-column: 1 / -1; border-right: none; border-bottom: 1px solid rgba(212,175,55,0.07); }
}
</style>
