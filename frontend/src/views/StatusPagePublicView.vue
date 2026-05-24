<template>
  <div class="public-page">
    <!-- Loading -->
    <div v-if="loading" class="loading-wrap">
      <div class="spinner"></div>
      <p>Loading status page…</p>
    </div>

    <!-- Error -->
    <div v-else-if="error" class="error-wrap">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" width="48" height="48">
        <circle cx="12" cy="12" r="10" stroke-linecap="round" stroke-linejoin="round" />
        <path d="M12 8v4M12 16h.01" stroke-linecap="round" />
      </svg>
      <h2>Page not found</h2>
      <p>This status page does not exist or has been removed.</p>
    </div>

    <!-- Content -->
    <template v-else-if="page">
      <!-- Header -->
      <header class="pub-header">
        <div class="header-inner">
          <div class="header-left">
            <h1 class="pub-title">{{ page.name }}</h1>
            <p v-if="page.description" class="pub-desc">{{ page.description }}</p>
          </div>
          <div class="header-right">
            <span class="status-label-heading">Service status</span>
            <span class="last-updated-row">
              Last updated {{ lastUpdatedTime }}
              <span class="sep-pipe">|</span>
              Next update in {{ nextUpdateCountdown }}s
            </span>
          </div>
        </div>
      </header>

      <!-- Overall status card -->
      <section class="pub-section">
        <div class="content-wrap">
          <div :class="['overall-card', overallCardClass]">
            <div class="overall-icon-wrap">
              <svg v-if="page.overall_status === 'OPERATIONAL'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="20" height="20">
                <path d="M20 6L9 17l-5-5" stroke-linecap="round" stroke-linejoin="round" />
              </svg>
              <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="20" height="20">
                <path d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z" stroke-linecap="round" stroke-linejoin="round" />
                <path d="M12 9v4M12 17h.01" stroke-linecap="round" />
              </svg>
            </div>
            <div class="overall-content">
              <span class="overall-headline">{{ bannerHeadline }}</span>
              <span class="overall-subtext">{{ bannerSubtext }}</span>
            </div>
          </div>
        </div>
      </section>

      <!-- Services -->
      <section class="pub-section services-section">
        <div class="content-wrap">
          <div v-if="page.services.length === 0" class="no-services">No services on this status page.</div>
          <div v-else class="services-panel">
            <div v-for="svc in page.services" :key="svc.id" class="service-row">
              <!-- Row header -->
              <div class="svc-header">
                <div class="svc-header-left">
                  <span :class="['svc-status-dot', dotClass(svc.current_status)]"></span>
                  <span class="svc-name">{{ svc.name }}</span>
                </div>
                <span class="svc-uptime-pct" v-if="svc.uptime_percent != null">
                  {{ svc.uptime_percent.toFixed(2) }}% uptime
                </span>
              </div>

              <!-- Uptime bars -->
              <div class="bars-track" v-if="svc.uptime_bars.length">
                <div
                  v-for="(val, i) in svc.uptime_bars"
                  :key="i"
                  :class="['bar', barClass(val)]"
                >
                  <div class="bar-tooltip-box">
                    <span class="tt-date">{{ formatBarDate(svc.uptime_dates[i]) }}</span>
                    <span class="tt-val">{{ val != null ? val.toFixed(1) + '% uptime' : 'No data' }}</span>
                    <span v-if="val != null && val < 99" :class="['tt-status', val < 80 ? 'tt-danger' : 'tt-warn']">
                      {{ val < 80 ? 'Outage' : 'Degraded performance' }}
                    </span>
                    <span v-else-if="val == null" class="tt-nodata">No records</span>
                    <span v-else class="tt-ok">No incidents</span>
                  </div>
                </div>
              </div>

              <div class="bars-meta-row">
                <span class="bars-meta-label">90 days ago</span>
                <span class="bars-meta-label">Today</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- Footer -->
      <footer class="pub-footer">
        <div class="footer-inner">
          <div class="footer-links">
            <a href="#" class="footer-link">Privacy Policy</a>
            <span class="footer-sep">|</span>
            <a href="#" class="footer-link">Terms of Service</a>
          </div>
          <span class="footer-brand">Status page by CloudPulse</span>
        </div>
      </footer>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { statusPagesApi } from '@/api/statusPages'
import type { StatusPagePublic } from '@/types'

const props = defineProps<{ slug: string }>()

const page = ref<StatusPagePublic | null>(null)
const loading = ref(true)
const error = ref(false)
const loadedAt = ref(new Date())
const tick = ref(new Date())

let refreshTimer: ReturnType<typeof setInterval> | null = null
let clockTimer: ReturnType<typeof setInterval> | null = null

async function load() {
  error.value = false
  try {
    page.value = await statusPagesApi.getPublic(props.slug)
    loadedAt.value = new Date()
    tick.value = new Date()
  } catch {
    error.value = true
    page.value = null
  } finally {
    loading.value = false
  }
}

const overallCardClass = computed(() => {
  switch (page.value?.overall_status) {
    case 'OPERATIONAL': return 'card-ok'
    case 'DEGRADED':    return 'card-warn'
    case 'OUTAGE':      return 'card-danger'
    default:            return 'card-unknown'
  }
})

const bannerHeadline = computed(() => {
  switch (page.value?.overall_status) {
    case 'OPERATIONAL': return "We're fully operational"
    case 'DEGRADED':    return "We're experiencing a partial outage"
    case 'OUTAGE':      return "We're experiencing a major outage"
    default:            return 'System status is unknown'
  }
})

const bannerSubtext = computed(() => {
  switch (page.value?.overall_status) {
    case 'OPERATIONAL': return "We're not aware of any issues affecting our systems."
    case 'DEGRADED':    return 'Some of our services are experiencing degraded performance.'
    case 'OUTAGE':      return "We're actively investigating and working to restore service."
    default:            return "We're unable to determine the current system status."
  }
})

const lastUpdatedTime = computed(() =>
  loadedAt.value.toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: '2-digit',
    second: '2-digit',
    hour12: true,
  })
)

const nextUpdateCountdown = computed(() => {
  const elapsed = Math.floor((tick.value.getTime() - loadedAt.value.getTime()) / 1000)
  return Math.max(0, 60 - elapsed)
})

function barClass(val: number | null) {
  if (val == null) return 'bar-nodata'
  if (val >= 99)   return 'bar-up'
  if (val >= 80)   return 'bar-partial'
  return 'bar-down'
}

function dotClass(status: string | null) {
  switch (status) {
    case 'UP':       return 'dot-ok'
    case 'DOWN':     return 'dot-danger'
    case 'DEGRADED': return 'dot-warn'
    default:         return 'dot-unknown'
  }
}

function formatBarDate(date: string | undefined) {
  if (!date) return 'Unknown'
  try {
    return new Date(date).toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric', year: 'numeric' })
  } catch {
    return date
  }
}

onMounted(() => {
  load()
  refreshTimer = setInterval(load, 60_000)
  clockTimer = setInterval(() => { tick.value = new Date() }, 1_000)
})

onUnmounted(() => {
  if (refreshTimer) clearInterval(refreshTimer)
  if (clockTimer) clearInterval(clockTimer)
})
</script>

<style scoped>
/* ── Base ─────────────────────────────────────────────────── */
.public-page {
  min-height: 100vh;
  background:
    radial-gradient(ellipse 80% 30% at 50% 0%, #f0f4ff 0%, transparent 70%),
    #ffffff;
  background-attachment: fixed;
  color: #1e293b;
  font-family: 'Inter', system-ui, -apple-system, sans-serif;
  position: relative;
  overflow-x: hidden;
}

/* ── Loading / Error ──────────────────────────────────────── */
.loading-wrap,
.error-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
  min-height: 100vh;
  padding: 40px;
  color: #64748b;
  text-align: center;
}

.error-wrap h2 { font-size: 20px; font-weight: 700; color: #0f172a; margin: 0; }
.error-wrap p  { font-size: 15px; margin: 0; }

.spinner {
  width: 32px;
  height: 32px;
  border: 3px solid #e2e8f0;
  border-top-color: #3b82f6;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

/* ── Header ───────────────────────────────────────────────── */
.pub-header {
  padding: 52px 24px 40px;
}

.header-inner {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  flex-wrap: wrap;
}

.pub-title {
  font-size: 26px;
  font-weight: 800;
  color: #0f172a;
  margin: 0;
  letter-spacing: -0.4px;
}

.pub-desc {
  font-size: 14px;
  color: #64748b;
  margin: 3px 0 0;
}

.header-right {
  text-align: right;
  flex-shrink: 0;
}

.status-label-heading {
  display: block;
  font-size: 16px;
  font-weight: 700;
  color: #0f172a;
  margin-bottom: 6px;
  letter-spacing: -0.2px;
}

.last-updated-row {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 6px;
  font-size: 13px;
  color: #64748b;
  white-space: nowrap;
}

.sep-pipe { opacity: 0.35; }

/* ── Sections ─────────────────────────────────────────────── */
.pub-section {
  padding: 0 24px 20px;
}

.services-section { padding-bottom: 40px; }

.content-wrap {
  max-width: 800px;
  margin: 0 auto;
}

/* ── Overall status card ──────────────────────────────────── */
.overall-card {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 18px 24px;
  border-radius: 14px;
  border: 1.5px solid;
}

.overall-icon-wrap {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.overall-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.overall-headline {
  font-size: 18px;
  font-weight: 700;
  line-height: 1.3;
}

.overall-subtext {
  font-size: 14px;
  font-weight: 400;
  color: #475569;
}

.card-ok          { background: #f0fdf4; border-color: #bbf7d0; color: #15803d; }
.card-ok .overall-icon-wrap { background: #dcfce7; }

.card-warn        { background: #fffbeb; border-color: #fde68a; color: #b45309; }
.card-warn .overall-icon-wrap { background: #fef3c7; }

.card-danger      { background: #fef2f2; border-color: #fecaca; color: #b91c1c; }
.card-danger .overall-icon-wrap { background: #fee2e2; }

.card-unknown     { background: #f8fafc; border-color: #e2e8f0; color: #475569; }
.card-unknown .overall-icon-wrap { background: #f1f5f9; }

/* ── Services panel ───────────────────────────────────────── */
.no-services {
  padding: 32px 0;
  font-size: 15px;
  color: #64748b;
}

.services-panel {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 14px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04), 0 1px 2px rgba(0, 0, 0, 0.02);
}

.service-row {
  padding: 20px 24px;
  border-bottom: 1px solid #f1f5f9;
}

.service-row:last-child {
  border-bottom: none;
}

.svc-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.svc-header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.svc-status-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  flex-shrink: 0;
}

.dot-ok      { background: #22c55e; box-shadow: 0 0 0 3px rgba(34, 197, 94, 0.18); }
.dot-warn    { background: #f59e0b; box-shadow: 0 0 0 3px rgba(245, 158, 11, 0.18); }
.dot-danger  { background: #ef4444; box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.18); }
.dot-unknown { background: #94a3b8; }

.svc-name {
  font-size: 15px;
  font-weight: 600;
  color: #0f172a;
}

.svc-uptime-pct {
  font-size: 13px;
  font-weight: 600;
  color: #64748b;
}

/* ── Uptime bars ──────────────────────────────────────────── */
.bars-track {
  display: flex;
  align-items: flex-end;
  gap: 2px;
  height: 34px;
  margin-bottom: 6px;
  overflow: visible;
}

.bar {
  flex: 1;
  min-width: 3px;
  height: 34px;
  border-radius: 3px;
  cursor: default;
  position: relative;
  transition: transform 0.12s ease, filter 0.12s ease;
}

.bar:hover {
  transform: scaleY(1.25);
  transform-origin: bottom;
  filter: brightness(1.1);
  z-index: 10;
}

.bar-up      { background: #22c55e; }
.bar-partial { background: #f59e0b; }
.bar-down    { background: #ef4444; }
.bar-nodata  { background: #e2e8f0; }

/* Tooltip */
.bar-tooltip-box {
  position: absolute;
  bottom: calc(100% + 10px);
  left: 50%;
  transform: translateX(-50%);
  background: #1e293b;
  color: #f1f5f9;
  border-radius: 8px;
  padding: 6px 10px;
  white-space: nowrap;
  font-size: 12px;
  opacity: 0;
  pointer-events: none;
  z-index: 100;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.18);
  transition: opacity 0.12s ease;
}

.bar-tooltip-box::after {
  content: '';
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  border: 4px solid transparent;
  border-top-color: #1e293b;
}

.bar:hover .bar-tooltip-box { opacity: 1; }

.tt-date    { font-size: 11px; color: #94a3b8; font-weight: 500; }
.tt-val     { font-size: 12px; font-weight: 700; }
.tt-ok      { font-size: 11px; color: #4ade80; }
.tt-warn    { font-size: 11px; color: #fbbf24; }
.tt-danger  { font-size: 11px; color: #f87171; }
.tt-nodata  { font-size: 11px; color: #64748b; }

/* ── Bars meta labels ─────────────────────────────────────── */
.bars-meta-row {
  display: flex;
  justify-content: space-between;
}

.bars-meta-label {
  font-size: 11px;
  color: #94a3b8;
}

/* ── Footer ───────────────────────────────────────────────── */
.pub-footer {
  padding: 0 24px 48px;
}

.footer-inner {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-top: 1px solid #e2e8f0;
  padding-top: 24px;
  flex-wrap: wrap;
  gap: 12px;
}

.footer-links {
  display: flex;
  align-items: center;
  gap: 10px;
}

.footer-link {
  font-size: 13px;
  color: #64748b;
  text-decoration: none;
}

.footer-link:hover {
  color: #3b82f6;
  text-decoration: underline;
}

.footer-sep   { color: #94a3b8; font-size: 13px; }
.footer-brand { font-size: 13px; color: #94a3b8; }

/* ── Responsive ───────────────────────────────────────────── */
@media (max-width: 600px) {
  .pub-header { padding: 36px 16px 28px; }
  .pub-section { padding-left: 16px; padding-right: 16px; }
  .pub-footer  { padding-left: 16px; padding-right: 16px; }

  .header-inner { flex-direction: column; gap: 16px; }
  .header-right { text-align: left; }
  .last-updated-row { justify-content: flex-start; }

  .service-card { padding: 16px; }
  .svc-header { flex-wrap: wrap; gap: 8px; }
  .footer-inner { flex-direction: column; align-items: flex-start; }
}
</style>
