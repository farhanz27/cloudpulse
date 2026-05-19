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
          <h1 class="pub-title">{{ page.name }}</h1>
          <p v-if="page.description" class="pub-desc">{{ page.description }}</p>

          <!-- Overall status banner -->
          <div :class="['status-banner', bannerClass]">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="20" height="20" aria-hidden="true">
              <path v-if="page.overall_status === 'OPERATIONAL'" d="M22 11.08V12a10 10 0 11-5.93-9.14" stroke-linecap="round" stroke-linejoin="round" />
              <polyline v-if="page.overall_status === 'OPERATIONAL'" points="22 4 12 14.01 9 11.01" stroke-linecap="round" stroke-linejoin="round" />
              <path v-else d="M12 9v4M12 17h.01" stroke-linecap="round" stroke-linejoin="round" />
              <path v-if="page.overall_status !== 'OPERATIONAL'" d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z" stroke-linecap="round" stroke-linejoin="round" />
            </svg>
            <span>{{ bannerText }}</span>
          </div>

          <p class="last-updated">Last updated {{ lastUpdated }}</p>
        </div>
      </header>

      <!-- Services -->
      <main class="pub-main">
        <div class="services-card">
          <h2 class="services-heading">Services</h2>

          <div v-if="page.services.length === 0" class="no-services">No services on this status page.</div>

          <div v-else class="service-rows">
            <div v-for="svc in page.services" :key="svc.id" class="service-row">
              <div class="svc-info">
                <span class="svc-name">{{ svc.name }}</span>
                <span class="svc-uptime" v-if="svc.uptime_percent != null">
                  {{ svc.uptime_percent.toFixed(1) }}% uptime (30d)
                </span>
              </div>

              <!-- Uptime bars -->
              <div class="bars-wrap" v-if="svc.uptime_bars.length">
                <div
                  v-for="(val, i) in svc.uptime_bars"
                  :key="i"
                  :class="['bar', barClass(val)]"
                  :title="svc.uptime_dates[i] ? `${svc.uptime_dates[i]}: ${val != null ? val.toFixed(1) + '%' : 'No data'}` : ''"
                ></div>
              </div>

              <span :class="['svc-status-badge', statusBadgeClass(svc.current_status)]">
                {{ statusLabel(svc.current_status) }}
              </span>
            </div>
          </div>
        </div>
      </main>

      <!-- Footer -->
      <footer class="pub-footer">
        <span>Powered by CloudPulse</span>
        <span class="footer-sep">·</span>
        <span>Auto-refreshes every 60s</span>
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
const now = ref(new Date())

let refreshTimer: ReturnType<typeof setInterval> | null = null
let clockTimer: ReturnType<typeof setInterval> | null = null

async function load() {
  error.value = false
  try {
    page.value = await statusPagesApi.getPublic(props.slug)
    now.value = new Date()
  } catch {
    error.value = true
    page.value = null
  } finally {
    loading.value = false
  }
}

const bannerClass = computed(() => {
  switch (page.value?.overall_status) {
    case 'OPERATIONAL': return 'banner-ok'
    case 'DEGRADED': return 'banner-warn'
    case 'OUTAGE': return 'banner-danger'
    default: return 'banner-unknown'
  }
})

const bannerText = computed(() => {
  switch (page.value?.overall_status) {
    case 'OPERATIONAL': return 'All systems operational'
    case 'DEGRADED': return 'Partial system outage'
    case 'OUTAGE': return 'Major outage in progress'
    default: return 'Status unknown'
  }
})

const lastUpdated = computed(() => {
  const diff = Math.floor((Date.now() - now.value.getTime()) / 1000)
  if (diff < 10) return 'just now'
  if (diff < 60) return `${diff}s ago`
  return `${Math.floor(diff / 60)}m ago`
})

function barClass(val: number | null) {
  if (val == null) return 'bar-nodata'
  if (val >= 99) return 'bar-up'
  if (val >= 80) return 'bar-partial'
  return 'bar-down'
}

function statusLabel(status: string | null) {
  switch (status) {
    case 'UP': return 'Operational'
    case 'DOWN': return 'Outage'
    case 'DEGRADED': return 'Degraded'
    default: return 'Unknown'
  }
}

function statusBadgeClass(status: string | null) {
  switch (status) {
    case 'UP': return 'badge-ok'
    case 'DOWN': return 'badge-danger'
    case 'DEGRADED': return 'badge-warn'
    default: return 'badge-unknown'
  }
}

onMounted(() => {
  load()
  refreshTimer = setInterval(load, 60_000)
  clockTimer = setInterval(() => { now.value = new Date() }, 10_000)
})

onUnmounted(() => {
  if (refreshTimer) clearInterval(refreshTimer)
  if (clockTimer) clearInterval(clockTimer)
})
</script>

<style scoped>
.public-page {
  min-height: 100vh;
  background: var(--bg-primary, #0f172a);
  color: var(--text-primary, #f1f5f9);
  font-family: system-ui, -apple-system, sans-serif;
}

/* Loading */
.loading-wrap, .error-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
  min-height: 100vh;
  padding: 40px;
  color: var(--text-muted, #64748b);
  text-align: center;
}
.error-wrap h2 { font-size: 20px; font-weight: 700; color: var(--text-primary); margin: 0; }
.error-wrap p { font-size: 15px; margin: 0; }
.spinner {
  width: 32px;
  height: 32px;
  border: 3px solid var(--border, #1e293b);
  border-top-color: var(--accent, #3b82f6);
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

/* Header */
.pub-header {
  background: var(--bg-secondary, #1e293b);
  border-bottom: 1px solid var(--border, #334155);
  padding: 48px 20px 36px;
}
.header-inner {
  max-width: 760px;
  margin: 0 auto;
}
.pub-title { font-size: 32px; font-weight: 800; margin: 0 0 8px; color: var(--text-primary); }
.pub-desc { font-size: 18px; color: var(--text-secondary, #94a3b8); margin: 0 0 24px; }

.status-banner {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 12px 20px;
  border-radius: 10px;
  font-size: 17px;
  font-weight: 700;
  margin-bottom: 12px;
}
.banner-ok { background: rgba(34,197,94,0.12); color: #22c55e; }
.banner-warn { background: rgba(245,158,11,0.12); color: #f59e0b; }
.banner-danger { background: rgba(239,68,68,0.12); color: #ef4444; }
.banner-unknown { background: rgba(148,163,184,0.12); color: #94a3b8; }

.last-updated { font-size: 14px; color: var(--text-muted, #64748b); margin: 0; }

/* Main */
.pub-main {
  max-width: 760px;
  margin: 36px auto;
  padding: 0 20px;
}

.services-card {
  background: var(--bg-secondary, #1e293b);
  border: 1px solid var(--border, #334155);
  border-radius: 14px;
  overflow: hidden;
}

.services-heading {
  font-size: 14px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: var(--text-muted, #64748b);
  padding: 14px 20px;
  border-bottom: 1px solid var(--border, #334155);
  margin: 0;
}

.no-services { padding: 32px 20px; font-size: 16px; color: var(--text-muted, #64748b); }

.service-rows { display: flex; flex-direction: column; }

.service-row {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 14px 20px;
  border-bottom: 1px solid var(--border, #334155);
  flex-wrap: wrap;
}
.service-row:last-child { border-bottom: none; }

.svc-info { display: flex; flex-direction: column; gap: 2px; min-width: 140px; flex: 1; }
.svc-name { font-size: 16px; font-weight: 600; color: var(--text-primary); }
.svc-uptime { font-size: 14px; color: var(--text-muted, #64748b); }

.bars-wrap {
  display: flex;
  align-items: center;
  gap: 2px;
  flex: 2;
  min-width: 0;
}
.bar {
  height: 24px;
  flex: 1;
  min-width: 3px;
  max-width: 12px;
  border-radius: 3px;
  transition: transform 0.15s;
}
.bar:hover { transform: scaleY(1.2); }
.bar-up { background: #22c55e; }
.bar-partial { background: #f59e0b; }
.bar-down { background: #ef4444; }
.bar-nodata { background: #334155; }

.svc-status-badge {
  display: inline-block;
  padding: 3px 10px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  white-space: nowrap;
}
.badge-ok { background: rgba(34,197,94,0.12); color: #22c55e; }
.badge-danger { background: rgba(239,68,68,0.12); color: #ef4444; }
.badge-warn { background: rgba(245,158,11,0.12); color: #f59e0b; }
.badge-unknown { background: rgba(148,163,184,0.12); color: #94a3b8; }

/* Footer */
.pub-footer {
  text-align: center;
  padding: 32px 20px;
  font-size: 14px;
  color: var(--text-muted, #64748b);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}
.footer-sep { opacity: 0.4; }
</style>
