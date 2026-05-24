<template>
  <div class="dashboard">
    <div class="page-header">
      <div>
        <h1 class="page-title">Monitors</h1>
      </div>
      <Button label="Add monitor" icon="pi pi-plus" @click="router.push('/monitors/new')" />
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

    <div class="filters-bar">
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
      </div>
    </div>

    <div class="cards-section">
      <div v-if="monitorsStore.error" class="error">{{ monitorsStore.error }}</div>

      <div v-else class="table-wrap">
        <table class="mon-table">
          <thead>
            <tr>
              <th>Monitor</th>
              <th>Uptime (24h)</th>
              <th>Response</th>
              <th>Last check</th>
              <th>Status</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <!-- Skeleton -->
            <template v-if="monitorsStore.loading">
              <tr v-for="i in 5" :key="i" class="skel-row">
                <td><div class="skel" style="width:160px;height:13px" /></td>
                <td><div class="skel" style="width:52px;height:20px;border-radius:20px" /></td>
                <td><div class="skel" style="width:120px;height:20px" /></td>
                <td><div class="skel" style="width:48px;height:13px" /></td>
                <td><div class="skel" style="width:72px;height:13px" /></td>
                <td></td>
              </tr>
            </template>

            <!-- Empty (no monitors at all) -->
            <tr v-else-if="!monitorsStore.services.length">
              <td colspan="6" class="empty-cell">
                <div class="empty-state">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" width="36" height="36" class="empty-icon">
                    <polyline points="1,12 6,12 8,6 10,18 12,8 14,15 16,12 23,12" stroke-linecap="round" stroke-linejoin="round" />
                  </svg>
                  <p class="empty-title">No monitors yet</p>
                  <p class="empty-desc">Add your first monitor to start tracking uptime and latency.</p>
                  <Button label="+ Add monitor" @click="router.push('/monitors/new')" />
                </div>
              </td>
            </tr>

            <!-- Rows -->
            <template v-else>
              <tr
                v-for="svc in filteredServices"
                :key="svc.id"
                class="mon-row"
                :class="{ 'row-paused': !svc.is_active }"
                @click="goDetail(svc.id)"
              >
                <td class="td-monitor">
                  <span class="mon-name">{{ svc.name }}</span>
                  <span class="mon-url">{{ svc.url }}</span>
                </td>
                <td>
                  <div class="bars-cell">
                    <UptimeBars :bars="uptimeBars[svc.id] ?? new Array(24).fill(null)" :dates="uptimeDates" size="sm" />
                    <span class="bars-pct" :class="uptimePctClass(svc)">
                      {{ svc.uptime_percent != null ? `${svc.uptime_percent}%` : '—' }}
                    </span>
                  </div>
                </td>
                <td class="td-mono">
                  {{ svc.last_response_time_ms != null ? `${svc.last_response_time_ms} ms` : '—' }}
                </td>
                <td class="td-muted">
                  {{ svc.last_checked_at ? relativeTime(svc.last_checked_at) : '—' }}
                </td>
                <td>
                  <div class="status-cell">
                    <StatusBadge :status="svc.is_active ? svc.current_status : null" :animated="svc.is_active" :dot-only="false" />
                    <span v-if="!svc.is_active" class="pill pill-paused">Paused</span>
                    <span v-else-if="isMuted(svc)" class="pill pill-muted">Muted</span>
                  </div>
                </td>
                <td class="td-actions" @click.stop>
                  <button class="btn-menu" @click="openMenu($event, svc)">
                    <svg viewBox="0 0 24 24" fill="currentColor" width="15" height="15">
                      <circle cx="12" cy="5" r="1.5" /><circle cx="12" cy="12" r="1.5" /><circle cx="12" cy="19" r="1.5" />
                    </svg>
                  </button>
                </td>
              </tr>
              <tr v-if="!filteredServices.length">
                <td colspan="6" class="empty-filter">No services match your filters.</td>
              </tr>
            </template>
          </tbody>
        </table>
      </div>
    </div>

    <Menu ref="menuRef" :model="menuItems" popup />

    <Dialog v-model:visible="showDeleteDialog" modal header="Delete Monitor" :style="{ width: '400px' }" @hide="deleteTarget = null">
      <p class="delete-text">Delete <strong>{{ deleteTarget?.name }}</strong>? This cannot be undone.</p>
      <template #footer>
        <Button label="Cancel" severity="secondary" outlined @click="showDeleteDialog = false" />
        <Button label="Delete" severity="danger" :loading="deleting" @click="doDelete" />
      </template>
    </Dialog>

  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useMonitorsStore } from '@/stores/monitors'
import { monitorsApi } from '@/api/monitors'
import { metricsApi } from '@/api/metrics'
import { usePolling } from '@/composables/usePolling'
import type { MonitorWithStatus, LastIncident } from '@/types'
import StatusBadge from '@/components/common/StatusBadge.vue'
import UptimeBars from '@/components/common/UptimeBars.vue'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import IconField from 'primevue/iconfield'
import InputIcon from 'primevue/inputicon'
import Select from 'primevue/select'
import Menu from 'primevue/menu'
import Dialog from 'primevue/dialog'

const router = useRouter()
const monitorsStore = useMonitorsStore()
const menuRef = ref()
const menuService = ref<MonitorWithStatus | null>(null)
const showDeleteDialog = ref(false)
const deleteTarget = ref<MonitorWithStatus | null>(null)
const deleting = ref(false)

const uptimeBars = ref<Record<string, (number | null)[]>>({})
const uptimeDates = ref<string[]>([])
const lastIncidents = ref<Record<string, LastIncident>>({})

async function loadUptimeBars() {
  try {
    const data = await metricsApi.getUptimeBarsHourly()
    uptimeBars.value = data.services
    uptimeDates.value = data.timestamps.map(formatHourLabel)
  } catch {
    // bars stay empty if endpoint is unavailable
  }
}

function formatHourLabel(iso: string): string {
  const start = new Date(iso)
  const end = new Date(start.getTime() + 3_600_000)
  const day = start.toLocaleDateString(undefined, { weekday: 'short', month: 'short', day: 'numeric' })
  const t1 = start.toLocaleTimeString(undefined, { hour: '2-digit', minute: '2-digit', hour12: false })
  const t2 = end.toLocaleTimeString(undefined, { hour: '2-digit', minute: '2-digit', hour12: false })
  return `${day} · ${t1}–${t2}`
}

async function loadLastIncidents() {
  lastIncidents.value = await metricsApi.getLastIncidents()
}

function uptimeSinceHint(svc: MonitorWithStatus): string | null {
  if (!svc.is_active) return null
  const inc = lastIncidents.value[svc.id]

  if (svc.current_status === 'DOWN') {
    if (inc && inc.duration_seconds === null) {
      const downForSec = Math.floor((Date.now() - new Date(inc.last_down_at).getTime()) / 1000)
      return `Down for ${formatDuration(downForSec)}`
    }
    return null
  }

  if (inc && inc.duration_seconds !== null) {
    const recoveredAt = new Date(inc.last_down_at).getTime() + inc.duration_seconds * 1000
    const upForSec = Math.floor((Date.now() - recoveredAt) / 1000)
    if (upForSec >= 0) return `Up for ${formatDuration(upForSec)}`
  }
  if (!inc) {
    const upForSec = Math.floor((Date.now() - new Date(svc.created_at).getTime()) / 1000)
    if (upForSec > 0) return `Up for ${formatDuration(upForSec)}`
  }
  return null
}

function formatDuration(seconds: number): string {
  if (seconds < 60) return `${Math.round(seconds)}s`
  if (seconds < 3600) {
    const m = Math.floor(seconds / 60)
    const s = Math.round(seconds % 60)
    return s > 0 ? `${m}m ${s}s` : `${m}m`
  }
  if (seconds < 86400) {
    const h = Math.floor(seconds / 3600)
    const m = Math.floor((seconds % 3600) / 60)
    return m > 0 ? `${h}h ${m}m` : `${h}h`
  }
  const d = Math.floor(seconds / 86400)
  const h = Math.floor((seconds % 86400) / 3600)
  return h > 0 ? `${d}d ${h}h` : `${d}d`
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


function openMenu(event: MouseEvent, svc: MonitorWithStatus) {
  menuService.value = svc
  menuRef.value.toggle(event)
}

const menuItems = computed(() => [
  {
    label: menuService.value?.is_active ? 'Pause' : 'Resume',
    icon: menuService.value?.is_active ? 'pi pi-pause' : 'pi pi-play',
    command: () => menuService.value && toggleActive(menuService.value),
  },
  {
    label: 'Edit',
    icon: 'pi pi-pencil',
    command: () => menuService.value && router.push(`/monitors/${menuService.value.id}/edit`),
  },
  { separator: true },
  {
    label: 'Delete',
    icon: 'pi pi-trash',
    class: 'menu-item-danger',
    command: () => menuService.value && removeMonitor(menuService.value),
  },
])

async function loadAll() {
  await Promise.all([monitorsStore.fetchAll(), loadUptimeBars(), loadLastIncidents()])
}

usePolling(loadAll, 60000)

const totalServices = computed(() => monitorsStore.services.length)
const upCount = computed(() => monitorsStore.services.filter((s) => s.current_status === 'UP').length)
const downCount = computed(() => monitorsStore.services.filter((s) => s.current_status === 'DOWN').length)
const degradedCount = computed(() => monitorsStore.services.filter((s) => s.current_status === 'DEGRADED').length)

const STATUS_ORDER: Record<string, number> = { DOWN: 0, DEGRADED: 1, UNKNOWN: 2, UP: 3 }

const filteredServices = computed(() => {
  let list: MonitorWithStatus[] = [...monitorsStore.services]
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

async function toggleActive(svc: MonitorWithStatus) {
  await monitorsApi.toggleActive(svc.id, !svc.is_active)
  monitorsStore.fetchAll()
}

function removeMonitor(svc: MonitorWithStatus) {
  deleteTarget.value = svc
  showDeleteDialog.value = true
}

async function doDelete() {
  if (!deleteTarget.value) return
  deleting.value = true
  await monitorsApi.delete(deleteTarget.value.id)
  deleting.value = false
  showDeleteDialog.value = false
  monitorsStore.fetchAll()
}

function goDetail(id: string) { router.push(`/monitors/${id}`) }

function isMuted(svc: MonitorWithStatus) {
  return !!svc.muted_until && new Date(svc.muted_until) > new Date()
}

function uptimePctClass(svc: MonitorWithStatus) {
  const pct = svc.uptime_percent
  if (pct === null) return ''
  if (pct >= 99) return 'text-success'
  if (pct >= 95) return 'text-warning'
  return 'text-danger'
}

function relativeTime(iso: string): string {
  const diff = Math.floor((Date.now() - new Date(iso).getTime()) / 1000)
  if (diff < 60) return `${diff}s ago`
  if (diff < 3600) return `${Math.floor(diff / 60)}m ago`
  if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`
  return `${Math.floor(diff / 86400)}d ago`
}
</script>

<style scoped>
.dashboard { height: 100%; display: flex; flex-direction: column; }

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
  flex-shrink: 0;
}

.page-title { font-size: 30px; font-weight: 700; letter-spacing: -0.02em; }

/* KPI grid */
.kpi-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 14px;
  margin-bottom: 28px;
  flex-shrink: 0;
}
@media (max-width: 1024px) { .kpi-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 520px)  { .kpi-grid { grid-template-columns: 1fr; } }

.kpi-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 18px 20px;
  border-radius: var(--radius);
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-left: 3px solid var(--border);
  transition: box-shadow 0.15s;
}
.kpi-card:hover { box-shadow: 0 4px 20px rgba(0,0,0,0.28); }
.kpi-total   { border-left-color: #64748b; }
.kpi-up      { border-left-color: var(--success); }
.kpi-down    { border-left-color: var(--danger); }
.kpi-degraded { border-left-color: var(--warning); }

.kpi-icon {
  width: 44px; height: 44px;
  border-radius: 10px;
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
}
.kpi-icon svg { width: 22px; height: 22px; }
.kpi-total .kpi-icon    { background: rgba(100,116,139,0.12); color: #94a3b8; }
.kpi-up .kpi-icon       { background: rgba(34,197,94,0.12);  color: var(--success); }
.kpi-down .kpi-icon     { background: rgba(239,68,68,0.12);  color: var(--danger); }
.kpi-degraded .kpi-icon { background: rgba(245,158,11,0.12); color: var(--warning); }

.kpi-body { display: flex; flex-direction: column; gap: 2px; min-width: 0; }
.kpi-label { font-size: 12px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.06em; color: var(--text-muted); }
.kpi-value { font-size: 26px; font-weight: 700; font-family: var(--font-mono); letter-spacing: -0.03em; line-height: 1.2; color: var(--text-primary); }
.kpi-up .kpi-value      { color: var(--success); }
.kpi-down .kpi-value    { color: var(--danger); }
.kpi-degraded .kpi-value { color: var(--warning); }

/* Table section */
.cards-section { flex: 1; min-height: 0; overflow: auto; padding: 4px 0; }

.filters-bar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 16px;
  flex-shrink: 0;
}

.toolbar-right { display: flex; align-items: center; gap: 12px; flex-wrap: wrap; }

.search-wrap { flex: 1; min-width: 200px; max-width: 360px; }
.search-input { width: 100%; }
.search-wrap :deep(.p-inputtext),
.search-wrap :deep(.p-iconfield) { height: 36px; }
.search-wrap :deep(.p-inputtext) { padding-top: 0; padding-bottom: 0; }

/* Toolbar dropdowns */
.toolbar-select { min-width: 140px; height: 36px; }
.toolbar-select :deep(.p-select) { height: 36px; }
.toolbar-select :deep(.p-select-label) { padding-top: 0; padding-bottom: 0; line-height: 36px; }

/* Table */
.table-wrap {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  overflow-x: auto;
}

.mon-table {
  width: 100%;
  min-height: 100%;
  border-collapse: collapse;
}

.mon-table th {
  padding: 11px 16px;
  font-size: 11px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: var(--text-muted);
  border-bottom: 1px solid var(--border);
  text-align: left;
  white-space: nowrap;
}

.mon-table td {
  padding: 13px 16px;
  font-size: 13px;
  color: var(--text-secondary);
  border-bottom: 1px solid var(--border);
  vertical-align: middle;
}

.mon-table tbody tr:last-child td { border-bottom: none; }

.mon-row { cursor: pointer; transition: background 0.1s; }
.mon-row:hover td { background: var(--bg-hover); }
tr:has(.empty-cell):hover td { background: transparent; cursor: default; }
.row-paused { opacity: 0.55; }

.td-monitor { min-width: 180px; }
.mon-name { display: block; font-size: 14px; font-weight: 600; color: var(--text-primary); }
.mon-url {
  display: block;
  font-size: 12px;
  font-family: var(--font-mono);
  color: var(--text-muted);
  margin-top: 2px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 260px;
}

.status-cell { display: flex; align-items: center; gap: 8px; }

.bars-cell { display: flex; align-items: center; gap: 8px; }
.bars-pct { font-size: 11px; font-weight: 700; font-family: var(--font-mono); color: var(--text-secondary); flex-shrink: 0; }

.td-mono { font-family: var(--font-mono); white-space: nowrap; }
.td-muted { color: var(--text-muted); white-space: nowrap; }
.td-actions { width: 48px; text-align: right; }

/* Skeleton */
.skel {
  border-radius: var(--radius-sm);
  background: var(--bg-hover);
  animation: shimmer 1.4s ease-in-out infinite;
  display: inline-block;
}
@keyframes shimmer { 0%, 100% { opacity: 1; } 50% { opacity: 0.4; } }

/* Pills */
.pill {
  display: inline-block;
  padding: 1px 6px;
  border-radius: 4px;
  font-size: 10px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  flex-shrink: 0;
}
.pill-paused { background: rgba(100,116,139,0.2); color: var(--text-muted); }
.pill-muted  { background: rgba(245,158,11,0.15); color: var(--warning); }

.text-success { color: var(--success); }
.text-warning { color: var(--warning); }
.text-danger  { color: var(--danger); }

.empty-filter { text-align: center; padding: 32px 16px; color: var(--text-muted); }

/* Empty state */
.empty-cell { padding: 0 !important; border: none !important; vertical-align: middle; }
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 24px;
  text-align: center;
}
.empty-icon { color: var(--text-muted); opacity: 0.4; }
.empty-title { font-size: 16px; font-weight: 700; color: var(--text-primary); margin: 0; }
.empty-desc { font-size: 14px; color: var(--text-muted); margin: 0; max-width: 300px; line-height: 1.6; }

.btn-menu {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border-radius: 6px;
  border: 1px solid transparent;
  background: transparent;
  color: var(--text-muted);
  cursor: pointer;
  transition: color 0.15s, background 0.15s, border-color 0.15s;
}
.btn-menu:hover { color: var(--text-primary); background: var(--bg-hover); border-color: var(--border); }


.error { text-align: center; padding: 32px 20px; color: var(--danger); }

.delete-text { margin: 0; font-size: 14px; color: var(--text-secondary); line-height: 1.6; }

/* Mobile responsive */
@media (max-width: 640px) {
  .page-header { flex-direction: column; gap: 12px; }
  .filters-bar { flex-direction: column; align-items: stretch; }
  .search-wrap { max-width: none; }
  .toolbar-right { flex-wrap: wrap; }
}
</style>

<style>
.menu-item-danger .p-menuitem-link .p-menuitem-text,
.menu-item-danger .p-menuitem-link .p-menuitem-icon {
  color: var(--danger) !important;
}
</style>
