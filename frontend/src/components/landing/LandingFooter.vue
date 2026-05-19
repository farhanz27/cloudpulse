<template>
  <footer class="lfooter">
    <div class="lfooter-cols">
      <!-- Col 1: Brand -->
      <div class="lfooter-brand-col">
        <router-link to="/" class="lfooter-logo">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75"
            stroke-linecap="round" stroke-linejoin="round" width="20" height="20" aria-hidden="true"
            style="color: #D4AF37;">
            <polyline points="1,12 6,12 8,6 10,18 12,8 14,15 16,12 23,12" />
          </svg>
          <span class="lfooter-logo-name">CLOUDPULSE</span>
        </router-link>
        <p class="lfooter-desc">
          Uptime monitoring, instant alerts, and incident management
          for cloud services — engineered for precision.
        </p>
        <div class="lfooter-socials">
          <a href="https://x.com" target="_blank" rel="noopener" class="social-link" aria-label="X (Twitter)">
            <svg viewBox="0 0 24 24" fill="currentColor" width="17" height="17" aria-hidden="true">
              <path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-4.714-6.231-5.401 6.231H2.746l7.73-8.835L1.254 2.25H8.08l4.253 5.622zm-1.161 17.52h1.833L7.084 4.126H5.117z"/>
            </svg>
          </a>
          <a href="https://github.com" target="_blank" rel="noopener" class="social-link" aria-label="GitHub">
            <svg viewBox="0 0 24 24" fill="currentColor" width="17" height="17" aria-hidden="true">
              <path d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z"/>
            </svg>
          </a>
          <a href="https://linkedin.com" target="_blank" rel="noopener" class="social-link" aria-label="LinkedIn">
            <svg viewBox="0 0 24 24" fill="currentColor" width="17" height="17" aria-hidden="true">
              <path d="M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433a2.062 2.062 0 01-2.063-2.065 2.064 2.064 0 112.063 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z"/>
            </svg>
          </a>
          <a href="https://youtube.com" target="_blank" rel="noopener" class="social-link" aria-label="YouTube">
            <svg viewBox="0 0 24 24" fill="currentColor" width="17" height="17" aria-hidden="true">
              <path d="M23.498 6.186a3.016 3.016 0 00-2.122-2.136C19.505 3.545 12 3.545 12 3.545s-7.505 0-9.377.505A3.017 3.017 0 00.502 6.186C0 8.07 0 12 0 12s0 3.93.502 5.814a3.016 3.016 0 002.122 2.136c1.871.505 9.376.505 9.376.505s7.505 0 9.377-.505a3.015 3.015 0 002.122-2.136C24 15.93 24 12 24 12s0-3.93-.502-5.814zM9.545 15.568V8.432L15.818 12l-6.273 3.568z"/>
            </svg>
          </a>
        </div>
      </div>

      <!-- Col 2: Product -->
      <div class="lfooter-link-col">
        <div class="lfooter-col-title">PRODUCT</div>
        <router-link to="/features" class="lfooter-link">Features</router-link>
        <router-link to="/integrations" class="lfooter-link">Integrations</router-link>
        <router-link to="/pricing" class="lfooter-link">Pricing</router-link>
        <router-link to="/updates" class="lfooter-link">Updates</router-link>
      </div>

      <!-- Col 3: Resources -->
      <div class="lfooter-link-col">
        <div class="lfooter-col-title">RESOURCES</div>
        <router-link to="/docs" class="lfooter-link">Documentation</router-link>
        <router-link to="/support" class="lfooter-link">Support</router-link>
        <a href="/#faq" class="lfooter-link" @click="handleSection($event, 'faq')">FAQ</a>
        <router-link to="/contact" class="lfooter-link">Contact Us</router-link>
      </div>

      <!-- Col 4: Company -->
      <div class="lfooter-link-col">
        <div class="lfooter-col-title">COMPANY</div>
        <router-link to="/about" class="lfooter-link">About</router-link>
        <router-link to="/privacy" class="lfooter-link">Privacy Policy</router-link>
        <router-link to="/terms" class="lfooter-link">Terms of Service</router-link>
        <router-link to="/status/overview" class="lfooter-link">Status Page</router-link>
      </div>
    </div>

    <div class="lfooter-bar">
      <div class="lfooter-bar-inner">
        <p class="lfooter-copy">&copy; {{ year }} CloudPulse. All rights reserved.</p>
        <div class="lfooter-legal">
          <router-link to="/terms" class="lfooter-legal-link">Terms</router-link>
          <span class="lfooter-dot">·</span>
          <router-link to="/privacy" class="lfooter-legal-link">Privacy</router-link>
          <span class="lfooter-dot">·</span>
          <router-link to="/cookies" class="lfooter-legal-link">Cookies</router-link>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
const year = new Date().getFullYear()
</script>

<style scoped>
.lfooter {
  border-top: 1px solid rgba(212,175,55,0.15);
  background: #0A0A0A;
  font-family: 'Josefin Sans', 'Inter', sans-serif;
}

.lfooter-cols {
  max-width: 1280px;
  margin: 0 auto;
  padding: 64px 32px 48px;
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  gap: 48px;
}

.lfooter-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  margin-bottom: 16px;
}

.lfooter-logo-name {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 16px;
  letter-spacing: 0.15em;
  color: #F2F0E4;
}

.lfooter-desc {
  font-size: 15px;
  color: #666;
  font-weight: 300;
  line-height: 1.75;
  margin-bottom: 24px;
}

.lfooter-socials {
  display: flex;
  align-items: center;
  gap: 10px;
}

.social-link {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  border: 1px solid rgba(212,175,55,0.2);
  color: #666;
  text-decoration: none;
  transition: border-color 0.3s, color 0.3s, background 0.3s;
}
.social-link:hover {
  border-color: #D4AF37;
  color: #D4AF37;
  background: rgba(212,175,55,0.06);
}

.lfooter-link-col {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.lfooter-col-title {
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.3em;
  color: #D4AF37;
  margin-bottom: 4px;
}

.lfooter-link {
  font-size: 15px;
  color: #666;
  text-decoration: none;
  cursor: pointer;
  transition: color 0.3s;
  font-weight: 300;
}
.lfooter-link:hover { color: #F2F0E4; }

.lfooter-bar {
  border-top: 1px solid rgba(212,175,55,0.08);
  padding: 20px 0;
}

.lfooter-bar-inner {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 32px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.lfooter-copy {
  font-size: 14px;
  color: #555;
  letter-spacing: 0.05em;
}

.lfooter-legal {
  display: flex;
  align-items: center;
  gap: 10px;
}

.lfooter-legal-link {
  font-size: 14px;
  color: #555;
  text-decoration: none;
  transition: color 0.3s;
}
.lfooter-legal-link:hover { color: #F2F0E4; }

.lfooter-dot {
  font-size: 14px;
  color: #444;
}

@media (max-width: 1024px) {
  .lfooter-cols { grid-template-columns: 1fr 1fr; gap: 40px; }
  .lfooter-brand-col { grid-column: 1 / -1; }
}

@media (max-width: 640px) {
  .lfooter-cols { grid-template-columns: 1fr; gap: 32px; padding: 48px 20px 36px; }
  .lfooter-brand-col { grid-column: auto; }
  .lfooter-bar-inner { padding: 0 20px; flex-direction: column; align-items: flex-start; gap: 8px; }
}
</style>
