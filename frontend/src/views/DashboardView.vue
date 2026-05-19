<template>
  <div class="dashboard">
    <div class="page-header">
      <div>
        <h1 class="page-title">Dashboard</h1>
        <p class="page-subtitle">Monitor your services in real-time</p>
      </div>
      <Button label="+ Add Monitor" @click="showAddModal = true" />
    </div>

    <div class="kpi-grid">
      <div class="kpi-card kpi-total">
        <div class="kpi-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75">
            <rect x="3" y="3" width="7" height="7" rx="1.5" />
            <rect x="14" y="3" width="7" height="7" rx="1.5" />
            <rect x="3" y="14" width="7" height="7" rx="1.5" />
            <rect x="14" y="14" width="7" height="7" rx="1.5" />
          </svg>
        </div>
        <div class="kpi-body">
          <span class="kpi-label">Total services</span>
          <span class="kpi-value">{{ totalServices }}</span>
        </div>
      </div>

      <div class="kpi-card kpi-up">
        <div class="kpi-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75">
            <path d="M12 22c5.523 0 10-4.477 10-10S17.523 2 12 2 2 6.477 2 12s4.477 10 10 10z" />
            <path d="M8 12l2.5 2.5L16 9" stroke-linecap="round" stroke-linejoin="round" />
          </svg>
        </div>
        <div class="kpi-body">
          <span class="kpi-label">Up</span>
          <span class="kpi-value">{{ upCount }}</span>
        </div>
      </div>

      <div class="kpi-card kpi-down">
        <div class="kpi-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75">
            <path d="M12 22c5.523 0 10-4.477 10-10S17.523 2 12 2 2 6.477 2 12s4.477 10 10 10z" />
            <path d="M15 9l-6 6M9 9l6 6" stroke-linecap="round" />
          </svg>
        </div>
        <div class="kpi-body">
          <span class="kpi-label">Down</span>
          <span class="kpi-value">{{ downCount }}</span>
        </div>
      </div>

      <div class="kpi-card kpi-degraded">
        <div class="kpi-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75">
            <path d="M12 9v4M12 17h.01" stroke-linecap="round" />
            <path d="M12 22c5.523 0 10-4.477 10-10S17.523 2 12 2 2 6.477 2 12s4.477 10 10 10z" />
          </svg>
        </div>
        <div class="kpi-body">
          <span class="kpi-label">Degraded</span>
          <span class="kpi-value">{{ degradedCount }}</span>
        </div>
      </div>
    </div>

    <div class="table-section card">
      <div class="table-toolbar">
        <div class="search-wrap">
          <IconField>
            <InputIcon class="pi pi-search" />
            <InputText
              v-model="searchQuery"
              placeholder="Search by name or URL…"
              autocomplete="off"
              aria-label="Search services"
              class="search-input"
            />
          </IconField>
        </div>
        <div class="toolbar-right">
          <Select
            v-model="statusFilter"
            :options="statusOptions"
            option-label="label"
            option-value="value"
            aria-label="Filter by status"
            class="toolbar-select"
          />
          <Select
            v-model="selectedDays"
            :options="uptimePeriodOptions"
            option-label="label"
            option-value="value"
            aria-label="Uptime period"
            class="toolbar-select toolbar-select--narrow"
          />
          <div class="view-toggle" role="group" aria-label="View mode">
            <button
              type="button"
              :class="['view-btn', { active: viewMode === 'table' }]"
              title="Table view"
              @click="viewMode = 'table'"
            >
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="16" height="16">
                <path d="M3 6h18M3 12h18M3 18h18" stroke-linecap="round" />
              </svg>
            </button>
            <button
              type="button"
              :class="['view-btn', { active: viewMode === 'grid' }]"
              title="Grid view"
              @click="viewMode = 'grid'"
            >
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="16" height="16">
                <rect x="3" y="3" width="7" height="7" rx="1" />
                <rect x="14" y="3" width="7" height="7" rx="1" />
                <rect x="3" y="14" width="7" height="7" rx="1" />
                <rect x="14" y="14" width="7" height="7" rx="1" />
              </svg>
            </button>
          </div>
        </div>
      </div>

      <!-- Skeleton loading -->
      <div v-if="servicesStore.loading" class="skeleton-table">
        <div v-for="i in 4" :key="i" class="skeleton-row">
          <div class="skeleton-cell skeleton-cell--wide"><div class="skeleton-line"></div><div class="skeleton-line skeleton-line--short"></div></div>
          <div class="skeleton-cell"><div class="skeleton-badge"></div></div>
          <div class="skeleton-cell skeleton-cell--num"><div class="skeleton-line skeleton-line--short"></div></div>
          <div class="skeleton-cell"><div class="skeleton-bars"></div></div>
          <div class="skeleton-cell"><div class="skeleton-line skeleton-line--short"></div></div>
          <div class="skeleton-cell skeleton-cell--narrow"></div>
        </div>
      </div>

      <div v-else-if="servicesStore.error" class="error">{{ servicesStore.error }}</div>

      <!-- Empty state -->
      <div v-else-if="!servicesStore.services.length" class="empty-state">
        <svg class="empty-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.25">
          <polyline points="1,12 6,12 8,6 10,18 12,8 14,15 16,12 23,12" stroke-linecap="round" stroke-linejoin="round" />
        </svg>
        <p class="empty-title">No monitors yet</p>
        <p class="empty-sub">Add your first monitor to start tracking uptime and latency.</p>
        <Button label="+ Add Monitor" @click="showAddModal = true" />
      </div>

      <!-- Table view -->
      <div v-else-if="viewMode === 'table'" class="table-scroll">
        <table class="services-table">
          <thead>
            <tr>
              <th>Monitor</th>
              <th>Status</th>
              <th class="num">Response</th>
              <th>Uptime ({{ selectedDays }}d)</th>
              <th>Last check</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="svc in filteredServices" :key="svc.id" :class="rowClass(svc)" @click="goDetail(svc.id)">
              <td class="cell-service">
                <span class="service-name">
                  {{ svc.name }}
                  <span v-if="!svc.is_active" class="pill pill-paused">Paused</span>
                  <span v-else-if="isMuted(svc)" class="pill pill-muted">Muted</span>
                </span>
                <span class="service-url" :title="svc.url">{{ svc.url }}</span>
                <span v-if="uptimeSinceHint(svc)" class="uptime-since-hint">{{ uptimeSinceHint(svc) }}</span>
              </td>
              <td>
                <StatusBadge :status="svc.is_active ? svc.current_status : null" :animated="svc.is_active" />
              </td>
              <td class="num mono">
                {{ svc.last_response_time_ms != null ? `${Math.round(svc.last_response_time_ms)} ms` : '—' }}
              </td>
              <td class="cell-uptime">
                <UptimeBars :bars="uptimeBars[svc.id] ?? []" :dates="uptimeDates" />
                <span class="uptime-pct" :class="uptimeClass(svc.uptime_percent)">
                  {{ svc.uptime_percent != null ? `${svc.uptime_percent}%` : '—' }}
                </span>
              </td>
              <td class="cell-time">{{ formatRelative(svc.last_checked_at) }}</td>
              <td class="cell-actions" @click.stop>
                <div class="menu-wrap" :class="{ open: menuOpenId === svc.id }">
                  <button class="btn-menu" title="Actions" @click="menuOpenId = menuOpenId === svc.id ? null : svc.id">
                    <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16" aria-hidden="true">
                      <circle cx="12" cy="5" r="1.5" /><circle cx="12" cy="12" r="1.5" /><circle cx="12" cy="19" r="1.5" />
                    </svg>
                  </button>
                  <div v-if="menuOpenId === svc.id" class="action-menu">
                    <button class="action-item" @click="toggleActive(svc); menuOpenId = null">
                      <svg v-if="svc.is_active" viewBox="0 0 24 24" fill="currentColor" width="14" height="14"><rect x="6" y="4" width="4" height="16" rx="1" /><rect x="14" y="4" width="4" height="16" rx="1" /></svg>
                      <svg v-else viewBox="0 0 24 24" fill="currentColor" width="14" height="14"><path d="M8 5v14l11-7z" /></svg>
                      {{ svc.is_active ? 'Pause' : 'Resume' }}
                    </button>
                    <button class="action-item" @click="openEdit(svc); menuOpenId = null">
                      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14"><path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7" stroke-linecap="round" stroke-linejoin="round" /><path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z" stroke-linecap="round" stroke-linejoin="round" /></svg>
                      Edit
                    </button>
                    <button class="action-item action-item--detail" @click="goDetail(svc.id); menuOpenId = null">
                      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14"><path d="M9 18l6-6-6-6" stroke-linecap="round" stroke-linejoin="round" /></svg>
                      View detail
                    </button>
                  </div>
                </div>
              </td>
            </tr>
            <tr v-if="!filteredServices.length">
              <td colspan="6" class="empty-cell">No services match your filters.</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Grid view -->
      <div v-else class="service-grid">
        <div
          v-for="svc in filteredServices"
          :key="svc.id"
          :class="['service-card', cardStatusClass(svc)]"
          @click="goDetail(svc.id)"
        >
          <div class="sc-header">
            <div class="sc-status-wrap">
              <StatusBadge :status="svc.is_active ? svc.current_status : null" :animated="svc.is_active" />
            </div>
            <div class="sc-actions" @click.stop>
              <button
                class="btn-icon-action"
                :title="svc.is_active ? 'Pause' : 'Resume'"
                @click="toggleActive(svc)"
              >
                <svg v-if="svc.is_active" viewBox="0 0 24 24" fill="currentColor" width="14" height="14">
                  <rect x="6" y="4" width="4" height="16" rx="1" />
                  <rect x="14" y="4" width="4" height="16" rx="1" />
                </svg>
                <svg v-else viewBox="0 0 24 24" fill="currentColor" width="14" height="14">
                  <path d="M8 5v14l11-7z" />
                </svg>
              </button>
              <button class="btn-icon-action" title="Edit" @click="openEdit(svc)">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14">
                  <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7" stroke-linecap="round" stroke-linejoin="round" />
                  <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z" stroke-linecap="round" stroke-linejoin="round" />
                </svg>
              </button>
            </div>
          </div>

          <div class="sc-body">
            <p class="sc-name">
              {{ svc.name }}
              <span v-if="!svc.is_active" class="pill pill-paused">Paused</span>
              <span v-else-if="isMuted(svc)" class="pill pill-muted">Muted</span>
            </p>
            <p class="sc-url" :title="svc.url">{{ truncateUrl(svc.url, 40) }}</p>
            <p v-if="uptimeSinceHint(svc)" class="sc-incident-hint">{{ uptimeSinceHint(svc) }}</p>
          </div>

          <div class="sc-uptime-bars">
            <UptimeBars :bars="uptimeBars[svc.id] ?? []" :dates="uptimeDates" />
          </div>
          <div class="sc-metrics">
            <div class="sc-metric">
              <span class="sc-metric-label">Response</span>
              <span class="sc-metric-value mono">{{ svc.last_response_time_ms != null ? `${Math.round(svc.last_response_time_ms)}ms` : '—' }}</span>
            </div>
            <div class="sc-metric">
              <span class="sc-metric-label">Uptime {{ selectedDays }}d</span>
              <span class="sc-metric-value mono" :class="uptimeClass(svc.uptime_percent)">{{ svc.uptime_percent != null ? `${svc.uptime_percent}%` : '—' }}</span>
            </div>
            <div class="sc-metric">
              <span class="sc-metric-label">Last check</span>
              <span class="sc-metric-value">{{ formatRelative(svc.last_checked_at) }}</span>
            </div>
          </div>
        </div>

        <div v-if="!filteredServices.length" class="empty-cell" style="grid-column: 1/-1; text-align:center; padding: 40px;">
          No services match your filters.
        </div>
      </div>
    </div>

    <AddServiceModal
      :visible="showAddModal"
      @close="showAddModal = false"
      @created="onServiceCreated"
    />

    <EditServiceModal
      :visible="showEditModal"
      :service="editingService"
      @close="showEditModal = false"
      @updated="servicesStore.fetchAll()"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useServicesStore } from '@/stores/services'
import { servicesApi } from '@/api/services'
import { metricsApi } from '@/api/metrics'
import { usePolling } from '@/composables/usePolling'
import type { ServiceWithStatus, LastIncident } from '@/types'
import AddServiceModal from '@/components/dashboard/AddServiceModal.vue'
import EditServiceModal from '@/components/dashboard/EditServiceModal.vue'
import StatusBadge from '@/components/common/StatusBadge.vue'
import UptimeBars from '@/components/common/UptimeBars.vue'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import IconField from 'primevue/iconfield'
import InputIcon from 'primevue/inputicon'
import Select from 'primevue/select'

const router = useRouter()
const servicesStore = useServicesStore()
const showAddModal = ref(false)
const showEditModal = ref(false)
const editingService = ref<ServiceWithStatus | null>(null)
const menuOpenId = ref<string | null>(null)
const viewMode = ref<'table' | 'grid'>(
  (localStorage.getItem('cp_view') as 'table' | 'grid') ?? 'table'
)

const dayOptions = [7, 30, 45, 90] as const
const selectedDays = ref<7 | 30 | 45 | 90>(
  (parseInt(localStorage.getItem('cp_uptime_days') ?? '45') as 7 | 30 | 45 | 90) || 45
)
const uptimePeriodOptions = [
  { label: '7d', value: 7 },
  { label: '30d', value: 30 },
  { label: '45d', value: 45 },
  { label: '90d', value: 90 },
]

const uptimeBars = ref<Record<string, (number | null)[]>>({})
const uptimeDates = ref<string[]>([])
const lastIncidents = ref<Record<string, LastIncident>>({})

async function loadUptimeBars() {
  const data = await metricsApi.getUptimeBars(selectedDays.value)
  uptimeBars.value = data.services
  uptimeDates.value = data.dates
}

async function loadLastIncidents() {
  lastIncidents.value = await metricsApi.getLastIncidents()
}

function uptimeSinceHint(svc: ServiceWithStatus): string | null {
  if (svc.current_status === 'DOWN' || !svc.is_active) return null
  const inc = lastIncidents.value[svc.id]
  if (!inc || inc.duration_seconds === null) return null
  const recoveredAt = new Date(inc.last_down_at).getTime() + inc.duration_seconds * 1000
  const upForSec = Math.floor((Date.now() - recoveredAt) / 1000)
  if (upForSec < 0) return null
  return `Up for ${formatDuration(upForSec)}`
}

function formatDuration(seconds: number): string {
  if (seconds < 60) return `${Math.round(seconds)}s`
  if (seconds < 3600) return `${Math.floor(seconds / 60)}m`
  if (seconds < 86400) {
    const h = Math.floor(seconds / 3600)
    const m = Math.floor((seconds % 3600) / 60)
    return m > 0 ? `${h}h ${m}m` : `${h}h`
  }
  return `${Math.floor(seconds / 86400)}d`
}

function openEdit(svc: ServiceWithStatus) {
  editingService.value = svc
  showEditModal.value = true
}

const searchQuery = ref('')
const statusFilter = ref<'all' | 'UP' | 'DOWN' | 'DEGRADED' | 'UNKNOWN'>('all')

const statusOptions = [
  { value: 'all' as const, label: 'All Status' },
  { value: 'UP' as const, label: 'Up' },
  { value: 'DOWN' as const, label: 'Down' },
  { value: 'DEGRADED' as const, label: 'Degraded' },
  { value: 'UNKNOWN' as const, label: 'Unknown' },
]

watch(viewMode, (v) => localStorage.setItem('cp_view', v))
watch(selectedDays, (v) => {
  localStorage.setItem('cp_uptime_days', String(v))
  loadUptimeBars()
})

function onDocClick(e: MouseEvent) {
  if (!(e.target as Element).closest('.menu-wrap')) menuOpenId.value = null
}
onMounted(() => document.addEventListener('click', onDocClick, true))
onUnmounted(() => document.removeEventListener('click', onDocClick, true))

async function loadAll() {
  await Promise.all([servicesStore.fetchAll(), loadUptimeBars(), loadLastIncidents()])
}

usePolling(loadAll, 60000)
loadAll()

const totalServices = computed(() => servicesStore.services.length)
const upCount = computed(() => servicesStore.services.filter((s) => s.current_status === 'UP').length)
const downCount = computed(() => servicesStore.services.filter((s) => s.current_status === 'DOWN').length)
const degradedCount = computed(() => servicesStore.services.filter((s) => s.current_status === 'DEGRADED').length)

const STATUS_ORDER: Record<string, number> = { DOWN: 0, DEGRADED: 1, UNKNOWN: 2, UP: 3 }

const filteredServices = computed(() => {
  let list: ServiceWithStatus[] = [...servicesStore.services]
  const q = searchQuery.value.trim().toLowerCase()
  if (q) list = list.filter((s) => s.name.toLowerCase().includes(q) || s.url.toLowerCase().includes(q))
  if (statusFilter.value !== 'all') {
    if (statusFilter.value === 'UNKNOWN') list = list.filter((s) => !s.current_status)
    else list = list.filter((s) => s.current_status === statusFilter.value)
  }
  list.sort((a, b) => {
    if (!a.is_active && b.is_active) return 1
    if (a.is_active && !b.is_active) return -1
    const ao = STATUS_ORDER[a.current_status ?? 'UNKNOWN'] ?? 2
    const bo = STATUS_ORDER[b.current_status ?? 'UNKNOWN'] ?? 2
    return ao - bo
  })
  return list
})

function isMuted(svc: ServiceWithStatus) {
  return svc.muted_until && new Date(svc.muted_until) > new Date()
}

function rowClass(svc: ServiceWithStatus) {
  if (!svc.is_active) return 'row-link row-paused'
  switch (svc.current_status) {
    case 'DOWN': return 'row-link row-down'
    case 'DEGRADED': return 'row-link row-degraded'
    default: return 'row-link'
  }
}

function cardStatusClass(svc: ServiceWithStatus) {
  if (!svc.is_active) return 'card-paused'
  switch (svc.current_status) {
    case 'DOWN': return 'card-down'
    case 'DEGRADED': return 'card-degraded'
    case 'UP': return 'card-up'
    default: return ''
  }
}

function uptimeClass(pct: number | null) {
  if (pct === null) return ''
  if (pct >= 99) return 'uptime-good'
  if (pct >= 95) return 'uptime-warn'
  return ''
}

async function toggleActive(svc: ServiceWithStatus) {
  await servicesApi.toggleActive(svc.id, !svc.is_active)
  servicesStore.fetchAll()
}

function goDetail(id: string) { router.push(`/services/${id}`) }

function truncateUrl(url: string, max = 48) {
  return url.length <= max ? url : `${url.slice(0, max - 1)}…`
}

function formatRelative(iso: string | null) {
  if (!iso) return '—'
  const diff = Math.floor((Date.now() - new Date(iso).getTime()) / 1000)
  if (diff < 60) return `${diff}s ago`
  if (diff < 3600) return `${Math.floor(diff / 60)}m ago`
  if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`
  return new Date(iso).toLocaleDateString()
}

function onServiceCreated() { servicesStore.fetchAll(); loadLastIncidents() }
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.page-title { font-size: 28px; font-weight: 700; letter-spacing: -0.02em; }
.page-subtitle { color: var(--text-muted); font-size: 15px; margin-top: 4px; }

/* KPI grid */
.kpi-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 28px;
}
@media (max-width: 1024px) { .kpi-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 520px)  { .kpi-grid { grid-template-columns: 1fr; } }

.kpi-card {
  position: relative;
  display: flex;
  align-items: center;
  gap: 18px;
  padding: 20px 20px 22px;
  border-radius: var(--radius);
  border: 1px solid var(--border);
  overflow: hidden;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}
.kpi-card::before {
  content: '';
  position: absolute;
  inset: 0;
  opacity: 0.12;
  pointer-events: none;
}
.kpi-total::before  { background: linear-gradient(135deg, #64748b 0%, #3b82f6 100%); }
.kpi-up::before     { background: linear-gradient(135deg, #22c55e 0%, #16a34a 100%); }
.kpi-down::before   { background: linear-gradient(135deg, #ef4444 0%, #b91c1c 100%); }
.kpi-degraded::before { background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%); }
.kpi-card:hover { transform: translateY(-2px); box-shadow: 0 12px 40px rgba(0,0,0,0.35); }
.kpi-icon { flex-shrink: 0; display: flex; align-items: center; justify-content: center; }
.kpi-icon svg { width: 48px; height: 48px; }
.kpi-total .kpi-icon   { color: #94a3b8; }
.kpi-up .kpi-icon      { color: #4ade80; }
.kpi-down .kpi-icon    { color: #f87171; }
.kpi-degraded .kpi-icon { color: #fbbf24; }
.kpi-body { display: flex; flex-direction: column; gap: 4px; min-width: 0; }
.kpi-label { font-size: 14px; font-weight: 600; text-transform: uppercase; letter-spacing: 0.06em; color: var(--text-muted); }
.kpi-value { font-size: 2rem; font-weight: 700; font-family: var(--font-mono); letter-spacing: -0.03em; line-height: 1.1; color: var(--text-primary); }
.kpi-up .kpi-value      { color: #86efac; }
.kpi-down .kpi-value    { color: #fca5a5; }
.kpi-degraded .kpi-value { color: #fcd34d; }

/* Table section */
.table-section { padding: 0; overflow: hidden; }

.table-toolbar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 16px 20px;
  border-bottom: 1px solid var(--border);
  background: var(--bg-secondary);
}

.toolbar-right { display: flex; align-items: center; gap: 12px; flex-wrap: wrap; }

.search-wrap { flex: 1; min-width: 200px; max-width: 360px; }
.search-input { width: 100%; }
.search-wrap :deep(.p-inputtext),
.search-wrap :deep(.p-iconfield) { height: 36px; }
.search-wrap :deep(.p-inputtext) { padding-top: 0; padding-bottom: 0; }

/* Toolbar dropdowns */
.toolbar-select { min-width: 140px; height: 36px; }
.toolbar-select--narrow { min-width: 90px; }
.toolbar-select :deep(.p-select) { height: 36px; }
.toolbar-select :deep(.p-select-label) { padding-top: 0; padding-bottom: 0; line-height: 36px; }

/* View toggle */
.view-toggle { display: flex; border: 1px solid var(--border); border-radius: var(--radius-sm); overflow: hidden; }
.view-btn { display: flex; align-items: center; justify-content: center; width: 36px; height: 36px; background: transparent; border: none; color: var(--text-muted); cursor: pointer; transition: background 0.15s, color 0.15s; }
.view-btn:hover { color: var(--text-primary); background: var(--bg-hover); }
.view-btn.active { color: var(--text-primary); background: var(--bg-hover); }
.view-btn + .view-btn { border-left: 1px solid var(--border); }

/* Skeleton loader */
.skeleton-table { padding: 8px 0; }
.skeleton-row {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 14px 16px;
  border-bottom: 1px solid var(--border);
}
.skeleton-cell { flex: 1; }
.skeleton-cell--wide { flex: 2; }
.skeleton-cell--num { flex: 0.6; display: flex; justify-content: flex-end; }
.skeleton-cell--narrow { flex: 0.3; }
.skeleton-line {
  height: 12px;
  border-radius: 6px;
  background: linear-gradient(90deg, var(--bg-hover) 25%, var(--border) 50%, var(--bg-hover) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
  margin-bottom: 6px;
}
.skeleton-line--short { width: 55%; margin-bottom: 0; }
.skeleton-badge { height: 22px; width: 56px; border-radius: 11px; background: linear-gradient(90deg, var(--bg-hover) 25%, var(--border) 50%, var(--bg-hover) 75%); background-size: 200% 100%; animation: shimmer 1.5s infinite; }
.skeleton-bars { height: 20px; width: 120px; border-radius: 4px; background: linear-gradient(90deg, var(--bg-hover) 25%, var(--border) 50%, var(--bg-hover) 75%); background-size: 200% 100%; animation: shimmer 1.5s infinite; }
@keyframes shimmer { to { background-position: -200% 0; } }

/* Table */
.table-scroll { overflow-x: auto; }
.services-table { width: 100%; border-collapse: collapse; font-size: 16px; }
.services-table th { text-align: left; padding: 12px 16px; font-size: 12px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.06em; color: var(--text-muted); border-bottom: 1px solid var(--border); background: var(--bg-secondary); white-space: nowrap; }
.services-table th.num, .services-table td.num { text-align: right; }
.services-table td { padding: 14px 16px; border-bottom: 1px solid var(--border); color: var(--text-secondary); vertical-align: middle; }

.row-link { cursor: pointer; transition: background 0.12s; }
.row-link:hover td { background: rgba(51,65,85,0.35); }
.row-down td:first-child     { box-shadow: inset 3px 0 0 var(--danger); }
.row-degraded td:first-child  { box-shadow: inset 3px 0 0 var(--warning); }
.row-paused { opacity: 0.5; }

.cell-service { min-width: 180px; }
.service-name { display: block; font-weight: 600; color: var(--text-primary); }
.service-url  { display: block; font-family: var(--font-mono); font-size: 14px; color: var(--text-muted); margin-top: 2px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 300px; }
.uptime-since-hint { display: block; font-size: 13px; color: var(--success); margin-top: 3px; opacity: 0.8; }

.cell-uptime { white-space: nowrap; }
.uptime-pct { font-family: var(--font-mono); font-size: 14px; color: var(--text-muted); margin-left: 6px; vertical-align: middle; }

.cell-time { font-size: 15px; white-space: nowrap; }
.mono { font-family: var(--font-mono); font-variant-numeric: tabular-nums; }

/* Uptime colors */
.uptime-good { color: var(--success) !important; }
.uptime-warn  { color: var(--warning) !important; }

/* Pills */
.pill { display: inline-block; padding: 2px 8px; border-radius: 6px; font-size: 12px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.04em; }
.pill-paused { background: rgba(100,116,139,0.2); color: var(--text-muted); margin-left: 6px; vertical-align: middle; }
.pill-muted  { background: rgba(245,158,11,0.15); color: var(--warning); margin-left: 6px; vertical-align: middle; }

/* ⋮ Action menu */
.cell-actions { width: 40px; padding: 6px 8px !important; }
.menu-wrap { position: relative; display: flex; justify-content: center; }

.btn-menu {
  display: inline-flex; align-items: center; justify-content: center;
  width: 30px; height: 30px; border-radius: 6px; border: 1px solid transparent;
  background: transparent; color: var(--text-muted); cursor: pointer;
  transition: color 0.15s, background 0.15s, border-color 0.15s;
}
.btn-menu:hover,
.menu-wrap.open .btn-menu {
  color: var(--text-primary); background: var(--bg-hover); border-color: var(--border);
}

.action-menu {
  position: absolute;
  top: calc(100% + 4px);
  right: 0;
  min-width: 148px;
  background: var(--bg-secondary);
  border: 1px solid var(--border);
  border-radius: 10px;
  box-shadow: 0 12px 36px rgba(0,0,0,0.45);
  overflow: hidden;
  z-index: 50;
}

.action-item {
  display: flex; align-items: center; gap: 9px;
  width: 100%; padding: 9px 14px;
  font-size: 13px; color: var(--text-secondary);
  background: transparent; border: none; cursor: pointer; text-align: left;
  transition: background 0.12s, color 0.12s;
}
.action-item:hover { background: var(--bg-hover); color: var(--text-primary); }
.action-item--detail { border-top: 1px solid var(--border); color: var(--accent); }
.action-item--detail:hover { background: rgba(212,175,55,0.08); }

/* Empty state */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 64px 24px;
  text-align: center;
}
.empty-icon { width: 64px; height: 64px; color: var(--text-muted); opacity: 0.4; }
.empty-title { font-size: 18px; font-weight: 600; color: var(--text-primary); }
.empty-sub { font-size: 15px; color: var(--text-muted); max-width: 320px; }
.empty-cell { text-align: center; padding: 40px 16px !important; color: var(--text-muted); }

/* Grid view */
.service-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
  padding: 24px;
}

.service-card {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 16px;
  border-radius: 12px;
  border: 1px solid var(--border);
  border-left: 3px solid var(--border);
  background: var(--bg-primary);
  cursor: pointer;
  transition: transform 0.15s, box-shadow 0.15s, border-color 0.15s;
}
.service-card:hover { transform: translateY(-2px); box-shadow: 0 8px 32px rgba(0,0,0,0.3); }
.card-up       { border-left-color: var(--success); }
.card-down     { border-left-color: var(--danger); }
.card-degraded { border-left-color: var(--warning); }
.card-paused   { border-left-color: var(--border); opacity: 0.6; }

.sc-header { display: flex; justify-content: space-between; align-items: center; }
.sc-status-wrap { display: flex; align-items: center; gap: 7px; }
.sc-actions { display: flex; gap: 4px; }

.sc-body { min-width: 0; }
.sc-name { font-size: 15px; font-weight: 600; color: var(--text-primary); white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.sc-url  { font-size: 15px; font-family: var(--font-mono); color: var(--text-muted); margin-top: 2px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.sc-incident-hint { font-size: 13px; color: var(--text-muted); margin-top: 4px; opacity: 0.75; }

.sc-uptime-bars { padding: 0 2px; }
.sc-metrics { display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; padding-top: 10px; border-top: 1px solid var(--border); }
.sc-metric { display: flex; flex-direction: column; gap: 3px; }
.sc-metric-label { font-size: 13px; font-weight: 600; text-transform: uppercase; letter-spacing: 0.06em; color: var(--text-muted); }
.sc-metric-value { font-size: 15px; font-weight: 600; color: var(--text-secondary); }

.btn-icon-action {
  display: inline-flex; align-items: center; justify-content: center;
  width: 28px; height: 28px; border-radius: 6px; border: 1px solid transparent;
  background: transparent; color: var(--text-muted); cursor: pointer;
  transition: color 0.15s, background 0.15s, border-color 0.15s;
}
.btn-icon-action:hover { color: var(--text-primary); background: var(--bg-hover); border-color: var(--border); }

.error { text-align: center; padding: 32px 20px; color: var(--danger); }

/* Mobile responsive */
@media (max-width: 640px) {
  .page-header { flex-direction: column; gap: 12px; }
  .table-toolbar { flex-direction: column; align-items: stretch; }
  .search-wrap { max-width: none; }
  .toolbar-right { flex-wrap: wrap; justify-content: space-between; }
  .filter-group { order: 2; }
  .uptime-period { order: 3; }
  .view-toggle { order: 1; margin-left: auto; }
  .services-table th:nth-child(5),
  .services-table td:nth-child(5) { display: none; }
}
</style>
