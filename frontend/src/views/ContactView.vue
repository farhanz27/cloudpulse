<template>
  <div class="page">
    <LandingNav />

    <div class="page-hero">
      <div class="page-eyebrow">
        <div class="eyebrow-line" aria-hidden="true"></div>
        <span>GET IN TOUCH</span>
        <div class="eyebrow-line" aria-hidden="true"></div>
      </div>
      <h1 class="page-title">Contact <span class="gold">Us</span></h1>
      <p class="page-sub">Our team is ready to help. Reach out and we'll respond within one business day.</p>
    </div>

    <div class="page-content">
      <div class="contact-grid">
        <div class="contact-info">
          <div class="info-item">
            <div class="info-icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75"
                stroke-linecap="round" stroke-linejoin="round" width="18" height="18">
                <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                <polyline points="22,6 12,13 2,6"/>
              </svg>
            </div>
            <div>
              <div class="info-label">EMAIL</div>
              <div class="info-value">support@cloudpulse.io</div>
            </div>
          </div>
          <div class="info-item">
            <div class="info-icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75"
                stroke-linecap="round" stroke-linejoin="round" width="18" height="18">
                <path d="M22 16.92v3a2 2 0 01-2.18 2 19.79 19.79 0 01-8.63-3.07A19.5 19.5 0 013.07 10.8 19.79 19.79 0 01.01 2.19 2 2 0 012 0h3a2 2 0 012 1.72c.127.96.361 1.903.7 2.81a2 2 0 01-.45 2.11L6.09 7.91a16 16 0 006 6l1.27-1.27a2 2 0 012.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0122 16.92z"/>
              </svg>
            </div>
            <div>
              <div class="info-label">PHONE</div>
              <div class="info-value">+60 3-2771 8800</div>
            </div>
          </div>
          <div class="info-item">
            <div class="info-icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75"
                stroke-linecap="round" stroke-linejoin="round" width="18" height="18">
                <circle cx="12" cy="12" r="10"/>
                <polyline points="12,6 12,12 16,14"/>
              </svg>
            </div>
            <div>
              <div class="info-label">HOURS</div>
              <div class="info-value">Mon–Fri, 9am–6pm MYT</div>
            </div>
          </div>
        </div>

        <form class="contact-form" @submit.prevent>
          <div class="form-row">
            <div class="form-field">
              <label class="field-label">NAME</label>
              <input class="field-input" type="text" placeholder="Your name" />
            </div>
            <div class="form-field">
              <label class="field-label">EMAIL</label>
              <input class="field-input" type="email" placeholder="you@company.com" />
            </div>
          </div>
          <div class="form-field">
            <label class="field-label">SUBJECT</label>
            <input class="field-input" type="text" placeholder="How can we help?" />
          </div>
          <div class="form-field">
            <label class="field-label">MESSAGE</label>
            <textarea class="field-input field-textarea" rows="5" placeholder="Describe your question or issue..."></textarea>
          </div>
          <button class="form-submit" type="submit">SEND MESSAGE</button>
        </form>
      </div>
    </div>

    <LandingFooter />
  </div>
</template>

<script setup lang="ts">
import LandingNav from '@/components/landing/LandingNav.vue'
import LandingFooter from '@/components/landing/LandingFooter.vue'
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
  font-size: 16px;
  color: #666;
  font-weight: 300;
  line-height: 1.7;
  max-width: 480px;
  margin: 0 auto;
}

.page-content {
  max-width: 1100px;
  margin: 0 auto;
  padding: 80px 32px;
}

.contact-grid {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 64px;
  align-items: start;
}

.contact-info {
  display: flex;
  flex-direction: column;
  gap: 32px;
}
.info-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}
.info-icon {
  width: 40px;
  height: 40px;
  border: 1px solid rgba(212,175,55,0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #D4AF37;
  flex-shrink: 0;
}
.info-label {
  font-size: 10px;
  font-weight: 600;
  letter-spacing: 0.25em;
  color: #D4AF37;
  margin-bottom: 4px;
}
.info-value {
  font-size: 16px;
  color: #888;
  font-weight: 300;
  line-height: 1.6;
}

.contact-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}
.form-field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.field-label {
  font-size: 10px;
  font-weight: 600;
  letter-spacing: 0.25em;
  color: #D4AF37;
}
.field-input {
  background: #141414;
  border: 1px solid rgba(212,175,55,0.2);
  color: #F2F0E4;
  padding: 12px 16px;
  font-size: 16px;
  font-family: inherit;
  font-weight: 300;
  outline: none;
  transition: border-color 0.3s;
}
.field-input::placeholder { color: #444; }
.field-input:focus { border-color: rgba(212,175,55,0.6); }
.field-textarea { resize: vertical; min-height: 120px; }

.form-submit {
  align-self: flex-start;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.2em;
  color: #0A0A0A;
  background: #D4AF37;
  border: none;
  padding: 14px 36px;
  cursor: pointer;
  transition: background 0.3s, box-shadow 0.3s;
  font-family: inherit;
}
.form-submit:hover {
  background: #F2E8C4;
  box-shadow: 0 0 20px rgba(212,175,55,0.3);
}

@media (max-width: 768px) {
  .contact-grid { grid-template-columns: 1fr; gap: 40px; }
  .form-row { grid-template-columns: 1fr; }
  .page-content { padding: 48px 20px; }
  .page-hero { padding: 120px 20px 60px; }
}
</style>
