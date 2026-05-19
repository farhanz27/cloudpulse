<template>
  <div class="service-detail" v-if="service">

    <!-- ── Section 1: Header ── -->
    <div class="svc-header">
      <div class="svc-title-block">
        <h1 class="svc-name">{{ service.name }}</h1>
        <div class="svc-url-row">
          <a :href="service.url" target="_blank" rel="noopener" class="svc-url">{{ service.url }}</a>
          <button class="icon-action" :title="copied ? 'Copied!' : 'Copy URL'" @click="copyUrl">
            <svg v-if="!copied" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="13" height="13">
              <rect x="9" y="9" width="13" height="13" rx="2" />
              <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1" stroke-linecap="round" />
            </svg>
            <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="13" height="13">
              <path d="M20 6L9 17l-5-5" stroke-linecap="round" stroke-linejoin="round" />
            </svg>
          </button>
          <span v-if="service.region" class="region-chip">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="11" height="11">
              <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7z" stroke-linecap="round" stroke-linejoin="round" />
              <circle cx="12" cy="9" r="2.5" />
            </svg>
            {{ service.region }}
          </span>
        </div>
      </div>

      <div class="svc-actions">
        <button class="action-btn" :disabled="testing" @click="sendTestNotification" title="Send a test notification through all configured channels">
          <svg :class="['action-icon', { spinning: testing }]" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="15" height="15">
            <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9" stroke-linecap="round" stroke-linejoin="round" />
            <path d="M13.73 21a2 2 0 0 1-3.46 0" stroke-linecap="round" stroke-linejoin="round" />
          </svg>
          <span>{{ testing ? 'Sending…' : 'Test Notification' }}</span>
        </button>

        <button class="action-btn" @click="showEditModal = true" title="Edit service settings">
          <svg class="action-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="15" height="15">
            <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7" stroke-linecap="round" stroke-linejoin="round" />
            <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z" stroke-linecap="round" stroke-linejoin="round" />
          </svg>
          <span>Edit</span>
        </button>

        <div class="action-wrap" ref="muteRef">
          <button
            :class="['action-btn', { 'action-btn--active': isMuted }]"
            @click="muteOpen = !muteOpen"
            :title="isMuted ? 'Alerts paused — click to adjust' : 'Pause alert notifications'"
          >
            <svg class="action-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="15" height="15">
              <rect x="6" y="4" width="4" height="16" rx="1" />
              <rect x="14" y="4" width="4" height="16" rx="1" />
            </svg>
            <span>{{ isMuted ? 'Paused' : 'Pause' }}</span>
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="11" height="11">
              <path d="M6 9l6 6 6-6" stroke-linecap="round" stroke-linejoin="round" />
            </svg>
          </button>
          <Transition name="dropdown">
            <div v-if="muteOpen" class="action-dropdown">
              <p class="dropdown-label">Silence alerts for…</p>
              <button v-for="opt in muteOptions" :key="opt.minutes" class="dropdown-item" @click="applyMute(opt.minutes)">
                {{ opt.label }}
              </button>
              <button v-if="isMuted" class="dropdown-item dropdown-item--warn" @click="applyMute(null)">
                Resume alerts
              </button>
            </div>
          </Transition>
        </div>

        <div class="action-wrap" ref="moreRef">
          <button class="action-btn action-btn--icon-only" @click="moreOpen = !moreOpen" title="More actions">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="16" height="16">
              <circle cx="5" cy="12" r="1.5" fill="currentColor" /><circle cx="12" cy="12" r="1.5" fill="currentColor" /><circle cx="19" cy="12" r="1.5" fill="currentColor" />
            </svg>
          </button>
          <Transition name="dropdown">
            <div v-if="moreOpen" class="action-dropdown action-dropdown--right">
              <router-link :to="`/alerts?service_id=${props.id}`" class="dropdown-item" @click="moreOpen = false">
                View event log
              </router-link>
              <div class="dropdown-divider"></div>
              <button class="dropdown-item dropdown-item--danger" @click="deleteService">
                Delete service
              </button>
            </div>
          </Transition>
        </div>
      </div>
    </div>

    <!-- ── Section 2: 3 Stat Cards ── -->
    <div class="stats-grid">

      <!-- Current Status -->
      <div :class="['stat-card', `stat-card--${(service.current_status ?? 'unknown').toLowerCase()}`]">
        <span class="stat-label">Current Status</span>
        <div class="stat-status-row">
          <StatusBadge :status="service.current_status" />
          <span v-if="isMuted" class="muted-chip">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="11" height="11">
              <rect x="6" y="4" width="4" height="16" rx="1" /><rect x="14" y="4" width="4" height="16" rx="1" />
            </svg>
            Paused
          </span>
        </div>
        <span v-if="currentStatusDuration" class="stat-sub">{{ currentStatusDuration }}</span>
        <span v-if="service.consecutive_failures > 0" class="stat-failures">
          {{ service.consecutive_failures }} consecutive failures
        </span>
      </div>

      <!-- Last Check -->
      <div class="stat-card">
        <span class="stat-label">Last Check</span>
        <span class="stat-value">{{ service.last_checked_at ? formatRelativePrecise(service.last_checked_at) : '—' }}</span>
        <span class="stat-sub">Checked every {{ checkIntervalLabel }}</span>
      </div>

      <!-- Last 24 Hours -->
      <div class="stat-card">
        <span class="stat-label">Last 24 Hours</span>
        <span class="stat-value">{{ service.uptime_percent != null ? `${service.uptime_percent}%` : '—' }}</span>
        <span class="stat-sub">
          {{ last24hStats.count }} incident{{ last24hStats.count !== 1 ? 's' : '' }},
          {{ formatDowntime(last24hStats.downSeconds) }} down
        </span>
      </div>

    </div>

    <!-- ── Domain & SSL ── -->
    <div class="card ssl-card">
      <h2 class="section-title" style="margin-bottom:16px">Domain & SSL</h2>
      <div class="ssl-row">
        <div class="ssl-item">
          <span class="ssl-label">Domain valid until</span>
          <span :class="['ssl-value', certStatusClass(service.domain_valid_until)]">
            {{ formatCertDate(service.domain_valid_until) }}
          </span>
          <span v-if="service.domain_valid_until" class="ssl-days">
            {{ certDaysLeftLabel(service.domain_valid_until) }}
          </span>
        </div>
        <div class="ssl-divider"></div>
        <div class="ssl-item">
          <span class="ssl-label">SSL certificate valid until</span>
          <span :class="['ssl-value', certStatusClass(service.ssl_valid_until)]">
            {{ formatCertDate(service.ssl_valid_until) }}
          </span>
          <span v-if="service.ssl_valid_until" class="ssl-days">
            {{ certDaysLeftLabel(service.ssl_valid_until) }}
          </span>
        </div>
      </div>
    </div>

    <!-- ── Section 3: Uptime History + Reliability ── -->
    <div class="card uptime-card">
      <div class="card-section-header">
        <h2 class="section-title">Uptime History</h2>
        <div class="range-tabs">
          <button
            v-for="opt in uptimeDayOptions"
            :key="opt"
            :class="['range-tab', { active: selectedUptimeDays === opt }]"
            @click="selectedUptimeDays = opt"
          >{{ opt }}d</button>
        </div>
      </div>

      <div v-if="uptimeBarsLoading" class="bars-skeleton"></div>
      <div v-else class="bars-wrap">
        <UptimeBars :bars="detailUptimeBars" :dates="detailUptimeDates" size="lg" />
      </div>

      <div class="bars-legend">
        <span class="legend-item"><span class="legend-dot legend-dot--perfect"></span>100%</span>
        <span class="legend-item"><span class="legend-dot legend-dot--good"></span>≥95%</span>
        <span class="legend-item"><span class="legend-dot legend-dot--warn"></span>≥50%</span>
        <span class="legend-item"><span class="legend-dot legend-dot--bad"></span>&lt;50%</span>
        <span class="legend-item"><span class="legend-dot legend-dot--down"></span>0%</span>
        <span class="legend-item"><span class="legend-dot legend-dot--none"></span>No data</span>
      </div>

      <div class="reliability-row">
        <div class="rel-stat">
          <span class="rel-label">MTTR</span>
          <span class="rel-value">{{ reliability ? (reliability.mttr_seconds !== null ? formatDuration(reliability.mttr_seconds) : 'N/A') : '—' }}</span>
          <span class="rel-hint">avg time to recover</span>
        </div>
        <div class="rel-divider"></div>
        <div class="rel-stat">
          <span class="rel-label">Total incidents</span>
          <span class="rel-value">{{ reliability?.total_incidents ?? '—' }}</span>
          <span class="rel-hint">all time</span>
        </div>
        <div class="rel-divider"></div>
        <div class="rel-stat">
          <span class="rel-label">Last 30 days</span>
          <span :class="['rel-value', (reliability?.incidents_last_30d ?? 0) > 0 ? 'rel-value--warn' : '']">
            {{ reliability?.incidents_last_30d ?? '—' }}
          </span>
          <span class="rel-hint">downtime incidents</span>
        </div>
      </div>
    </div>

    <!-- ── Section 4: Response Time ── -->
    <div class="card chart-card">
      <div class="card-section-header">
        <h2 class="section-title">Response Time</h2>
        <div class="range-tabs">
          <button
            v-for="opt in rangeOptions"
            :key="opt.hours"
            :class="['range-tab', { active: selectedRange === opt.hours }]"
            @click="selectRange(opt.hours)"
          >{{ opt.label }}</button>
        </div>
      </div>
      <LatencyChart :key="`lat-${selectedRange}`" :data="metrics" :range-hours="selectedRange" />
    </div>

    <!-- ── Section 5: Recent Incidents ── -->
    <div class="card incidents-card">
      <div class="card-section-header">
        <h2 class="section-title">Recent Incidents</h2>
        <router-link :to="`/incidents?service_id=${props.id}`" class="view-all-link">
          View all
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="12" height="12">
            <path d="M5 12h14M12 5l7 7-7 7" stroke-linecap="round" stroke-linejoin="round" />
          </svg>
        </router-link>
      </div>

      <div v-if="incidentsLoading" class="incidents-placeholder">Loading…</div>
      <div v-else-if="!incidents.length" class="incidents-placeholder">No downtime incidents recorded yet.</div>
      <table v-else class="incidents-table">
        <thead>
          <tr>
            <th>Started</th>
            <th>Recovered</th>
            <th>Duration</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(inc, i) in incidents.slice(0, 8)" :key="i">
            <td class="ts-cell" :title="new Date(inc.started_at).toLocaleString()">{{ formatRelative(inc.started_at) }}</td>
            <td class="ts-cell">
              <span v-if="inc.recovered_at" :title="new Date(inc.recovered_at).toLocaleString()">{{ formatRelative(inc.recovered_at) }}</span>
              <span v-else class="inc-badge inc-badge--open">Still down</span>
            </td>
            <td class="mono-cell">{{ formatDuration(inc.duration_seconds) }}</td>
            <td>
              <span :class="['inc-badge', inc.recovered_at ? 'inc-badge--resolved' : 'inc-badge--open']">
                {{ inc.recovered_at ? 'Resolved' : 'Open' }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

  </div>

  <!-- Loading skeleton -->
  <div v-else class="detail-skeleton">
    <div class="skel-line skel-line--title"></div>
    <div class="skel-line skel-line--subtitle"></div>
    <div class="skel-row">
      <div v-for="i in 3" :key="i" class="skel-card"></div>
    </div>
  </div>

  <EditServiceModal
    :visible="showEditModal"
    :service="service"
    @close="showEditModal = false"
    @updated="onServiceUpdated"
  />
</template>

<script setup lang="ts">
import { ref, watch, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'primevue/usetoast'
import { servicesApi } from '@/api/services'
import { metricsApi } from '@/api/metrics'
import type { ServiceWithStatus, MetricPoint, Incident, ReliabilityMetrics } from '@/types'
import { usePolling } from '@/composables/usePolling'
import StatusBadge from '@/components/common/StatusBadge.vue'
import UptimeBars from '@/components/common/UptimeBars.vue'
import LatencyChart from '@/components/charts/LatencyChart.vue'
import EditServiceModal from '@/components/dashboard/EditServiceModal.vue'

const props = defineProps<{ id: string }>()
const router = useRouter()
const toast = useToast()

const showEditModal = ref(false)
const testing = ref(false)
const copied = ref(false)
const muteOpen = ref(false)
const moreOpen = ref(false)
const muteRef = ref<HTMLElement | null>(null)
const moreRef = ref<HTMLElement | null>(null)

const muteOptions = [
  { label: '30 minutes', minutes: 30 },
  { label: '1 hour',     minutes: 60 },
  { label: '4 hours',    minutes: 240 },
  { label: '24 hours',   minutes: 1440 },
]

const service = ref<ServiceWithStatus | null>(null)
const metrics = ref<MetricPoint[]>([])
const selectedRange = ref(24)

const incidents = ref<Incident[]>([])
const incidentsLoading = ref(false)
const reliability = ref<ReliabilityMetrics | null>(null)

const uptimeDayOptions = [7, 30, 45, 90] as const
const selectedUptimeDays = ref<7 | 30 | 45 | 90>(45)
const detailUptimeBars = ref<(number | null)[]>([])
const detailUptimeDates = ref<string[]>([])
const uptimeBarsLoading = ref(false)

const rangeOptions = [
  { label: '1h',  hours: 1 },
  { label: '6h',  hours: 6 },
  { label: '24h', hours: 24 },
  { label: '7d',  hours: 168 },
]

// Ticks every second for live durations
const now = ref(Date.now())
let ticker: ReturnType<typeof setInterval>

const isMuted = computed(() => {
  const m = service.value?.muted_until
  return !!m && new Date(m) > new Date()
})

const checkIntervalLabel = computed(() => {
  if (!service.value) return '—'
  const secs = service.value.check_interval_seconds
  if (secs < 60) return `${secs}s`
  const m = Math.floor(secs / 60)
  return m === 1 ? '1m' : `${m}m`
})

const uptimeSince = computed((): Date | null => {
  if (!service.value) return null
  if (service.value.current_status === 'UP') {
    const lastRecovery = incidents.value
      .filter(i => i.recovered_at)
      .map(i => new Date(i.recovered_at!))
      .sort((a, b) => b.getTime() - a.getTime())[0]
    return lastRecovery ?? new Date(service.value.created_at)
  }
  if (service.value.current_status === 'DOWN') {
    const open = incidents.value.find(i => !i.recovered_at)
    if (open) return new Date(open.started_at)
  }
  return null
})

const currentStatusDuration = computed((): string => {
  if (!service.value || !uptimeSince.value) return ''
  const secs = Math.floor((now.value - uptimeSince.value.getTime()) / 1000)
  const h = Math.floor(secs / 3600)
  const m = Math.floor((secs % 3600) / 60)
  const s = secs % 60
  const status = service.value.current_status
  const label = status === 'DOWN' ? 'Down for' : 'Currently up for'
  return `${label} ${h}h ${m}m ${s}s`
})

const last24hStats = computed(() => {
  const cutoff = now.value - 24 * 3600 * 1000
  const recent = incidents.value.filter(i => new Date(i.started_at).getTime() > cutoff)
  const totalDown = recent.reduce((sum, i) => sum + (i.duration_seconds || 0), 0)
  return { count: recent.length, downSeconds: totalDown }
})

let loadServiceSeq = 0

function resolutionMinutesForRange(hours: number): number {
  if (hours <= 1) return 1
  if (hours <= 6) return 5
  if (hours <= 24) return 15
  return 60
}

function selectRange(hours: number) { selectedRange.value = hours }

function formatRelative(iso: string) {
  const diff = Math.floor((Date.now() - new Date(iso).getTime()) / 1000)
  if (diff < 60) return `${diff}s ago`
  if (diff < 3600) return `${Math.floor(diff / 60)}m ago`
  if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`
  return new Date(iso).toLocaleDateString()
}

function formatRelativePrecise(iso: string): string {
  const diff = Math.floor((now.value - new Date(iso).getTime()) / 1000)
  if (diff < 60) return `${diff}s ago`
  const m = Math.floor(diff / 60)
  const s = diff % 60
  if (diff < 3600) return s > 0 ? `${m}m, ${s}s ago` : `${m}m ago`
  const h = Math.floor(diff / 3600)
  const rm = Math.floor((diff % 3600) / 60)
  return rm > 0 ? `${h}h, ${rm}m ago` : `${h}h ago`
}

function formatDuration(seconds: number): string {
  if (seconds < 60) return `${Math.round(seconds)}s`
  if (seconds < 3600) return `${Math.floor(seconds / 60)}m`
  const h = Math.floor(seconds / 3600)
  const m = Math.floor((seconds % 3600) / 60)
  if (seconds < 86400) return m > 0 ? `${h}h ${m}m` : `${h}h`
  return `${Math.floor(seconds / 86400)}d`
}

function formatDowntime(secs: number): string {
  if (secs === 0) return '0m'
  if (secs < 60) return `${secs}s`
  if (secs < 3600) return `${Math.floor(secs / 60)}m`
  return `${Math.floor(secs / 3600)}h ${Math.floor((secs % 3600) / 60)}m`
}

function formatCertDate(iso: string | null | undefined): string {
  if (!iso) return '—'
  return new Date(iso).toLocaleDateString(undefined, { month: 'short', day: 'numeric', year: 'numeric' })
}

function certDaysLeft(iso: string | null | undefined): number | null {
  if (!iso) return null
  return Math.floor((new Date(iso).getTime() - Date.now()) / 86_400_000)
}

function certDaysLeftLabel(iso: string | null | undefined): string {
  const days = certDaysLeft(iso)
  if (days === null) return ''
  if (days < 0) return 'Expired'
  if (days === 0) return 'Expires today'
  return `${days}d remaining`
}

function certStatusClass(iso: string | null | undefined): string {
  const days = certDaysLeft(iso)
  if (days === null) return ''
  if (days < 0) return 'cert--expired'
  if (days < 7) return 'cert--danger'
  if (days < 30) return 'cert--warn'
  return 'cert--ok'
}

async function loadServiceAndMetrics() {
  const seq = ++loadServiceSeq
  const range = selectedRange.value
  try {
    const [svc, metricsData] = await Promise.all([
      servicesApi.get(props.id),
      metricsApi.getMetrics(props.id, range, resolutionMinutesForRange(range)),
    ])
    if (seq !== loadServiceSeq) return
    service.value = svc
    metrics.value = metricsData.data
  } catch (e) {
    if (seq === loadServiceSeq) console.error(e)
  }
}

async function loadIncidents() {
  incidentsLoading.value = true
  try {
    incidents.value = await metricsApi.getIncidents(props.id)
  } finally {
    incidentsLoading.value = false
  }
}

async function loadReliability() {
  reliability.value = await metricsApi.getReliability(props.id)
}

async function loadUptimeBars() {
  uptimeBarsLoading.value = true
  try {
    const data = await metricsApi.getUptimeBars(selectedUptimeDays.value, props.id)
    detailUptimeBars.value = data.services[props.id] ?? []
    detailUptimeDates.value = data.dates
  } finally {
    uptimeBarsLoading.value = false
  }
}

async function loadAll() {
  await Promise.all([loadServiceAndMetrics(), loadIncidents(), loadReliability(), loadUptimeBars()])
}

async function sendTestNotification() {
  testing.value = true
  try {
    const result = await servicesApi.testNotification(props.id)
    if (result.channels_notified > 0) {
      toast.add({ severity: 'success', summary: 'Test sent', detail: `Notified ${result.channels_notified} channel${result.channels_notified !== 1 ? 's' : ''}`, life: 4000 })
    } else {
      toast.add({ severity: 'warn', summary: 'No channels reached', detail: 'No active notification channels are configured for this monitor', life: 5000 })
    }
  } catch {
    toast.add({ severity: 'error', summary: 'Failed to send', detail: 'Could not send test notification', life: 4000 })
  } finally {
    testing.value = false
  }
}

async function copyUrl() {
  if (!service.value) return
  await navigator.clipboard.writeText(service.value.url)
  copied.value = true
  setTimeout(() => { copied.value = false }, 2000)
}

async function applyMute(minutes: number | null) {
  muteOpen.value = false
  const updated = await servicesApi.mute(props.id, minutes)
  service.value = updated
}

async function deleteService() {
  moreOpen.value = false
  if (!confirm(`Delete "${service.value?.name}"? This cannot be undone.`)) return
  await servicesApi.delete(props.id)
  router.push('/')
}

async function onServiceUpdated() {
  await loadServiceAndMetrics()
}

function onClickOutside(e: MouseEvent) {
  if (muteRef.value && !muteRef.value.contains(e.target as Node)) muteOpen.value = false
  if (moreRef.value && !moreRef.value.contains(e.target as Node)) moreOpen.value = false
}

onMounted(() => {
  document.addEventListener('click', onClickOutside, true)
  ticker = setInterval(() => { now.value = Date.now() }, 1000)
})
onUnmounted(() => {
  document.removeEventListener('click', onClickOutside, true)
  clearInterval(ticker)
})

watch(selectedRange, () => loadServiceAndMetrics())
watch(selectedUptimeDays, () => loadUptimeBars())

usePolling(loadAll, 30_000)
loadAll()
</script>

<style scoped>
/* ── Section 1: Header ── */
.svc-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.svc-title-block { min-width: 0; flex: 1; }

.svc-name {
  font-size: 24px;
  font-weight: 700;
  letter-spacing: -0.02em;
  color: var(--text-primary);
  margin: 0 0 6px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.svc-url-row { display: flex; align-items: center; gap: 6px; }

.svc-url {
  font-size: 14px;
  color: var(--text-muted);
  font-family: var(--font-mono);
  text-decoration: none;
  word-break: break-all;
  transition: color 0.15s;
}
.svc-url:hover { color: var(--accent); }

.region-chip {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 2px 8px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 600;
  color: var(--text-muted);
  background: var(--bg-hover);
  border: 1px solid var(--border);
  white-space: nowrap;
  flex-shrink: 0;
}

.icon-action {
  flex-shrink: 0;
  display: flex; align-items: center; justify-content: center;
  width: 22px; height: 22px;
  border-radius: 5px; border: none;
  background: transparent; color: var(--text-muted);
  cursor: pointer; transition: color 0.15s, background 0.15s;
}
.icon-action:hover { color: var(--text-primary); background: var(--bg-hover); }

.svc-actions { display: flex; align-items: center; gap: 6px; flex-shrink: 0; flex-wrap: wrap; }

.action-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 7px 12px;
  border-radius: 8px;
  border: 1px solid var(--border);
  background: var(--bg-secondary);
  color: var(--text-secondary);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  white-space: nowrap;
  transition: color 0.15s, background 0.15s, border-color 0.15s;
}
.action-btn:hover:not(:disabled) { color: var(--text-primary); border-color: var(--text-muted); background: var(--bg-hover); }
.action-btn:disabled { opacity: 0.5; cursor: default; }
.action-btn--active { color: var(--warning) !important; border-color: rgba(245,158,11,0.4) !important; background: rgba(245,158,11,0.06) !important; }
.action-btn--icon-only { padding: 7px 9px; }
.action-icon { flex-shrink: 0; }

.action-wrap { position: relative; }

.action-dropdown {
  position: absolute;
  top: calc(100% + 6px);
  left: 0;
  min-width: 180px;
  background: var(--bg-secondary);
  border: 1px solid var(--border);
  border-radius: 10px;
  box-shadow: 0 12px 40px rgba(0,0,0,0.45);
  overflow: hidden;
  z-index: 300;
}
.action-dropdown--right { left: auto; right: 0; }

.dropdown-label {
  padding: 10px 14px 6px;
  font-size: 12px; font-weight: 700; text-transform: uppercase;
  letter-spacing: 0.06em; color: var(--text-muted); margin: 0;
}
.dropdown-item {
  display: block; width: 100%; text-align: left;
  padding: 9px 14px; font-size: 14px; color: var(--text-secondary);
  background: transparent; border: none; cursor: pointer; text-decoration: none;
  transition: background 0.12s, color 0.12s;
}
.dropdown-item:hover { background: var(--bg-hover); color: var(--text-primary); }
.dropdown-item--warn { color: var(--warning); border-top: 1px solid var(--border); }
.dropdown-item--warn:hover { background: rgba(245,158,11,0.08); }
.dropdown-item--danger { color: var(--danger); }
.dropdown-item--danger:hover { background: rgba(239,68,68,0.08); color: var(--danger); }
.dropdown-divider { height: 1px; background: var(--border); margin: 4px 0; }

@keyframes spin { to { transform: rotate(360deg); } }
.spinning { animation: spin 0.7s linear infinite; }

.dropdown-enter-active, .dropdown-leave-active { transition: opacity 0.15s, transform 0.15s; }
.dropdown-enter-from, .dropdown-leave-to { opacity: 0; transform: translateY(-4px); }

/* ── Section 2: Stat Cards ── */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 14px;
  margin-bottom: 14px;
}

.stat-card {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-top: 3px solid var(--border);
  border-radius: var(--radius);
  padding: 18px 20px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.stat-card--up      { border-top-color: var(--success); }
.stat-card--down    { border-top-color: var(--danger); }
.stat-card--degraded { border-top-color: var(--warning); }

.stat-label {
  font-size: 12px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: var(--text-muted);
}

.stat-status-row { display: flex; align-items: center; gap: 8px; margin: 2px 0; }

.stat-value {
  font-size: 20px;
  font-weight: 700;
  font-family: var(--font-mono);
  color: var(--text-primary);
  letter-spacing: -0.02em;
  line-height: 1.2;
  margin: 2px 0;
}

.stat-sub {
  font-size: 12px;
  color: var(--text-muted);
}

.stat-failures {
  font-size: 13px;
  font-weight: 600;
  color: var(--danger);
}

.muted-chip {
  display: inline-flex; align-items: center; gap: 4px;
  padding: 2px 8px; border-radius: 6px;
  font-size: 12px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.04em;
  background: rgba(245,158,11,0.12); color: var(--warning);
}

@media (max-width: 640px) {
  .stats-grid { grid-template-columns: 1fr; }
}

/* ── Domain & SSL ── */
.ssl-card {
  margin-bottom: 14px;
}

.ssl-row {
  display: flex;
  align-items: flex-start;
  gap: 0;
  flex-wrap: wrap;
  row-gap: 16px;
}

.ssl-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
  min-width: 180px;
}

.ssl-label {
  font-size: 12px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: var(--text-muted);
}

.ssl-value {
  font-size: 16px;
  font-weight: 700;
  color: var(--text-primary);
  font-family: var(--font-mono);
}
.ssl-value.cert--ok      { color: var(--success); }
.ssl-value.cert--warn    { color: var(--warning); }
.ssl-value.cert--danger  { color: var(--danger); }
.ssl-value.cert--expired { color: var(--danger); }

.ssl-days {
  font-size: 12px;
  color: var(--text-muted);
}

.ssl-divider {
  width: 1px;
  height: 52px;
  background: var(--border);
  flex-shrink: 0;
  margin: 0 28px;
  align-self: center;
}

@media (max-width: 520px) {
  .ssl-divider { display: none; }
}

/* ── Section 3: Uptime card ── */
.uptime-card { margin-bottom: 16px; }

.card-section-header {
  display: flex; align-items: center; justify-content: space-between;
  gap: 12px; margin-bottom: 16px;
}

.section-title {
  font-size: 15px; font-weight: 700; color: var(--text-primary); margin: 0;
}

.range-tabs {
  display: flex;
  border: 1px solid var(--border);
  border-radius: 7px;
  overflow: hidden;
}
.range-tab {
  padding: 5px 12px; font-size: 12px; font-weight: 500;
  background: transparent; border: none; color: var(--text-muted);
  cursor: pointer; transition: color 0.15s, background 0.15s;
}
.range-tab:not(:last-child) { border-right: 1px solid var(--border); }
.range-tab.active { background: var(--accent); color: #fff; }
.range-tab:hover:not(.active) { background: var(--bg-hover); color: var(--text-primary); }

.bars-wrap { padding: 4px 0 8px; }

.bars-skeleton {
  height: 32px; border-radius: 6px;
  background: var(--bg-hover);
  animation: shimmer 1.4s ease-in-out infinite;
  margin-bottom: 8px;
}
@keyframes shimmer { 0%, 100% { opacity: 1; } 50% { opacity: 0.4; } }

.bars-legend {
  display: flex; gap: 14px; flex-wrap: wrap;
  margin-bottom: 16px; padding-bottom: 16px;
  border-bottom: 1px solid var(--border);
}
.legend-item { display: flex; align-items: center; gap: 5px; font-size: 13px; color: var(--text-muted); }
.legend-dot { width: 10px; height: 10px; border-radius: 2px; flex-shrink: 0; }
.legend-dot--perfect { background: var(--success); }
.legend-dot--good    { background: #86efac; }
.legend-dot--warn    { background: var(--warning); }
.legend-dot--bad     { background: #f97316; }
.legend-dot--down    { background: var(--danger); }
.legend-dot--none    { background: var(--bg-hover); border: 1px solid var(--border); }

.reliability-row {
  display: flex; align-items: center; gap: 0;
  padding-top: 4px; flex-wrap: wrap; row-gap: 12px;
}
.rel-stat { display: flex; flex-direction: column; gap: 2px; flex: 1; min-width: 100px; }
.rel-label { font-size: 12px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.06em; color: var(--text-muted); }
.rel-value { font-size: 20px; font-weight: 700; font-family: var(--font-mono); color: var(--text-primary); letter-spacing: -0.02em; line-height: 1.2; }
.rel-value--warn { color: var(--warning); }
.rel-hint { font-size: 13px; color: var(--text-muted); }
.rel-divider { width: 1px; height: 44px; background: var(--border); flex-shrink: 0; margin: 0 24px; }

@media (max-width: 580px) {
  .reliability-row { flex-direction: column; }
  .rel-divider { width: 100%; height: 1px; margin: 4px 0; }
}

/* ── Section 4: Chart ── */
.chart-card { margin-bottom: 16px; }

/* ── Section 5: Incidents ── */
.view-all-link {
  display: inline-flex; align-items: center; gap: 4px;
  font-size: 13px; font-weight: 500; color: var(--accent);
  text-decoration: none; white-space: nowrap; transition: opacity 0.15s;
}
.view-all-link:hover { opacity: 0.75; }

.incidents-placeholder {
  padding: 28px 0; text-align: center;
  font-size: 13px; color: var(--text-muted);
}

.incidents-table { width: 100%; border-collapse: collapse; }
.incidents-table th {
  text-align: left; padding: 10px 16px;
  font-size: 12px; font-weight: 700; text-transform: uppercase;
  letter-spacing: 0.05em; color: var(--text-muted);
  border-bottom: 1px solid var(--border); background: var(--bg-secondary);
}
.incidents-table td {
  padding: 12px 16px; border-bottom: 1px solid var(--border);
  font-size: 13px; color: var(--text-secondary);
}
.incidents-table tbody tr:last-child td { border-bottom: none; }
.incidents-table tbody tr:hover td { background: var(--bg-hover); }

.ts-cell { white-space: nowrap; }
.mono-cell { font-family: var(--font-mono); font-size: 14px; white-space: nowrap; }

.inc-badge {
  display: inline-flex; align-items: center;
  padding: 2px 8px; border-radius: 6px;
  font-size: 12px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.04em;
}
.inc-badge--open     { background: rgba(239,68,68,0.12); color: var(--danger); }
.inc-badge--resolved { background: rgba(34,197,94,0.12); color: var(--success); }

/* ── Loading skeleton ── */
.detail-skeleton { padding: 8px 0; }
.skel-line {
  height: 14px; border-radius: 6px;
  background: var(--bg-hover); animation: shimmer 1.4s ease-in-out infinite;
  margin-bottom: 12px;
}
.skel-line--title { height: 28px; width: 35%; }
.skel-line--subtitle { height: 13px; width: 55%; margin-bottom: 28px; }
.skel-row { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; }
.skel-card { height: 96px; border-radius: 12px; background: var(--bg-hover); animation: shimmer 1.4s ease-in-out infinite; }

/* ── Mobile ── */
@media (max-width: 768px) {
  .svc-header { flex-direction: column; }
  .svc-actions { width: 100%; }
  .action-btn span { display: none; }
  .action-btn { padding: 8px 10px; }
  .action-btn--icon-only { padding: 8px 10px; }
  .skel-row { grid-template-columns: 1fr 1fr; }
}
</style>
