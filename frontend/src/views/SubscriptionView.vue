<template>
  <div class="billing-page">
    <div class="page-header">
      <h1 class="page-title">Subscription</h1>
    </div>

    <div class="sections">

      <!-- Plan cards -->
      <div class="plan-cards">

        <!-- Free -->
        <div class="plan-card plan-card--active">
          <div class="plan-card-header">
            <span class="plan-name">Free</span>
            <span class="plan-current-badge">Current plan</span>
          </div>
          <p class="plan-price">RM0 <span class="plan-price-period">/ month</span></p>
          <p class="plan-desc">Everything you need to get started with uptime monitoring.</p>
          <ul class="plan-features">
            <li>Up to 5 monitors</li>
            <li>1 status page</li>
            <li>2 integrations</li>
            <li>5-minute checks</li>
            <li>30-day history</li>
          </ul>
          <button class="plan-btn plan-btn--current" disabled>Current plan</button>
        </div>

        <!-- Pro -->
        <div class="plan-card plan-card--pro">
          <div class="plan-card-header">
            <span class="plan-name plan-name--pro">Pro</span>
            <span class="plan-soon-badge">Coming soon</span>
          </div>
          <p class="plan-price plan-price--pro">RM20 <span class="plan-price-period">/ month</span></p>
          <p class="plan-desc">For teams that need more power and fewer limits.</p>
          <ul class="plan-features">
            <li>Up to 50 monitors</li>
            <li>Up to 10 status pages</li>
            <li>All notification channels</li>
            <li>1-minute check intervals</li>
            <li>12-month history</li>
            <li>SSL certificate monitoring</li>
            <li>Priority support</li>
          </ul>
          <button class="plan-btn plan-btn--upgrade" disabled>Upgrade to Pro</button>
        </div>

      </div>

      <!-- Usage -->
      <section class="section card">
        <h2 class="section-title">Usage</h2>

        <div v-if="loading" class="usage-loading">Loading usage…</div>
        <div v-else-if="usageError" class="usage-loading">Failed to load usage — refresh the page to try again.</div>

        <div v-else class="usage-list">
          <div class="usage-item">
            <div class="usage-header">
              <span class="usage-label">Monitors</span>
              <span class="usage-count">
                {{ usage.monitors.used }} / {{ usage.monitors.limit }}
              </span>
            </div>
            <div class="usage-bar">
              <div class="usage-fill" :style="{ width: pct(usage.monitors) + '%' }"></div>
            </div>
          </div>

          <div class="usage-item">
            <div class="usage-header">
              <span class="usage-label">Status Pages</span>
              <span class="usage-count">
                {{ usage.status_pages.used }} / {{ usage.status_pages.limit }}
              </span>
            </div>
            <div class="usage-bar">
              <div class="usage-fill" :style="{ width: pct(usage.status_pages) + '%' }"></div>
            </div>
          </div>

          <div class="usage-item">
            <div class="usage-header">
              <span class="usage-label">Integrations</span>
              <span class="usage-count">
                {{ usage.integrations.used }} / {{ usage.integrations.limit }}
              </span>
            </div>
            <div class="usage-bar">
              <div class="usage-fill" :style="{ width: pct(usage.integrations) + '%' }"></div>
            </div>
          </div>
        </div>
      </section>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { usageApi, type UsageResponse, type QuotaItem } from '@/api/usage'

const loading = ref(true)
const usageError = ref(false)
const usage = ref<UsageResponse>({
  plan: 'free',
  monitors: { used: 0, limit: 5 },
  status_pages: { used: 0, limit: 1 },
  integrations: { used: 0, limit: 2 },
  min_check_interval_seconds: 60,
})

function pct(q: QuotaItem): number {
  return Math.min(100, Math.round((q.used / q.limit) * 100))
}

onMounted(async () => {
  try {
    usage.value = await usageApi.get()
  } catch {
    usageError.value = true
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.billing-page { width: 100%; }

.page-header { margin-bottom: 32px; }
.page-title {
  font-size: 30px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.sections {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* ── Plan cards ── */
.plan-cards {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.plan-card {
  background: var(--bg-secondary);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 28px;
  display: flex;
  flex-direction: column;
  gap: 0;
}

.plan-card--pro {
  border-color: rgba(212, 175, 55, 0.35);
  background: linear-gradient(160deg, rgba(212, 175, 55, 0.04) 0%, var(--bg-secondary) 60%);
}

.plan-card-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
}

.plan-name {
  font-size: 18px;
  font-weight: 700;
  color: var(--text-primary);
}
.plan-name--pro { color: var(--accent); }

.plan-current-badge {
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.07em;
  text-transform: uppercase;
  color: var(--text-muted);
  background: var(--bg-hover);
  border: 1px solid var(--border);
  padding: 2px 8px;
  border-radius: 999px;
}

.plan-soon-badge {
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.07em;
  text-transform: uppercase;
  color: var(--accent);
  background: rgba(212, 175, 55, 0.1);
  border: 1px solid rgba(212, 175, 55, 0.25);
  padding: 2px 8px;
  border-radius: 999px;
}

.plan-price {
  font-size: 32px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 10px;
  line-height: 1;
}
.plan-price--pro { color: var(--accent); }
.plan-price-period {
  font-size: 14px;
  font-weight: 400;
  color: var(--text-muted);
}

.plan-desc {
  font-size: 13px;
  color: var(--text-muted);
  line-height: 1.5;
  margin-bottom: 20px;
}

.plan-features {
  list-style: none;
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 28px;
  flex: 1;
}

.plan-features li {
  font-size: 13px;
  color: var(--text-secondary);
  padding-left: 18px;
  position: relative;
}
.plan-features li::before {
  content: '–';
  position: absolute;
  left: 0;
  color: var(--text-muted);
}
.plan-card--pro .plan-features li::before {
  content: '✓';
  color: var(--accent);
  font-size: 11px;
  top: 1px;
}

.plan-btn {
  width: 100%;
  padding: 10px;
  border-radius: var(--radius-sm);
  font-size: 13px;
  font-weight: 600;
  border: none;
  cursor: default;
  transition: opacity 0.15s;
}

.plan-btn--current {
  background: var(--bg-hover);
  color: var(--text-muted);
  border: 1px solid var(--border);
}

.plan-btn--upgrade {
  background: var(--accent);
  color: #000;
  opacity: 0.45;
}

/* ── Usage ── */
.section-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 20px;
}

.usage-loading {
  font-size: 13px;
  color: var(--text-muted);
}

.usage-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.usage-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 8px;
}

.usage-label {
  font-size: 13px;
  font-weight: 500;
  color: var(--text-secondary);
}

.usage-count {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-muted);
}

.usage-bar {
  height: 6px;
  background: var(--bg-hover);
  border-radius: 999px;
  overflow: hidden;
}

.usage-fill {
  height: 100%;
  background: var(--accent);
  border-radius: 999px;
  transition: width 0.4s ease;
}

@media (max-width: 640px) {
  .plan-cards { grid-template-columns: 1fr; }
}
</style>
