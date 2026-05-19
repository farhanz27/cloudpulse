<template>
  <div class="alerts-page">
    <!-- Page header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Event Log</h1>
        <p class="page-subtitle">Raw monitoring events — downtime, recovery, and latency spikes</p>
      </div>
    </div>

    <!-- Toolbar: tabs + filters + dismiss all -->
    <div class="toolbar">
      <div class="toolbar-left">
        <div class="tab-group">
          <button :class="['tab-btn', { active: tab === 'active' }]" @click="tab = 'active'">
            Active
            <span v-if="activeCount > 0" class="tab-count">{{ activeCount }}</span>
          </button>
          <button :class="['tab-btn', { active: tab === 'history' }]" @click="tab = 'history'">
            History
          </button>
        </div>
      </div>
      <div class="toolbar-right">
        <Select
          v-model="serviceFilter"
          :options="serviceOptions"
          option-label="name"
          option-value="id"
          placeholder="All services"
          class="filter-select"
        />
        <Select
          v-model="typeFilter"
          :options="typeOptions"
          option-label="label"
          option-value="value"
          placeholder="All types"
          class="filter-select"
        />
        <Button
          v-if="tab === 'active' && filteredActive.length > 0"
          label="Dismiss all"
          severity="secondary"
          outlined
          class="toolbar-btn"
          :loading="dismissingAll"
          @click="dismissAll"
        />
      </div>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="empty-state">Loading…</div>

    <template v-else>
      <!-- Empty state -->
      <div v-if="!filtered.length" class="empty-state">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.25" width="48" height="48" class="empty-icon" aria-hidden="true">
          <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9" stroke-linecap="round" stroke-linejoin="round" />
          <path d="M13.73 21a2 2 0 0 1-3.46 0" stroke-linecap="round" />
          <line x1="2" y1="2" x2="22" y2="22" stroke-linecap="round" />
        </svg>
        <p class="empty-title">No {{ tab === 'active' ? 'active' : 'dismissed' }} alerts</p>
        <p v-if="serviceFilter || typeFilter" class="empty-sub">Try removing filters to see more results</p>
      </div>

      <!-- Alert cards -->
      <div v-else class="alert-list">
        <div
          v-for="alert in filtered"
          :key="alert.id"
          :class="['alert-card', accentClass(alert), { 'alert-card--dismissed': tab === 'history' }]"
        >
          <!-- Card header -->
          <div class="card-header">
            <div class="card-header-left">
              <span :class="['type-pill', pillClass(alert)]">{{ formatType(alert.alert_type) }}</span>
              <router-link :to="`/services/${alert.service_id}`" class="service-link">
                {{ alert.service_name || 'Unknown service' }}
              </router-link>
            </div>
            <div class="card-header-right">
              <time class="card-time" :title="new Date(alert.created_at).toLocaleString()">
                {{ formatRelative(alert.created_at) }}
              </time>
              <Button
                v-if="tab === 'active'"
                label="Dismiss"
                severity="secondary"
                outlined
                size="small"
                @click="dismiss(alert.id)"
              />
              <span v-else class="card-dismissed-chip">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="11" height="11" aria-hidden="true">
                  <path d="M20 6L9 17l-5-5" stroke-linecap="round" stroke-linejoin="round" />
                </svg>
                Dismissed
              </span>
            </div>
          </div>

          <!-- Message body -->
          <div class="card-body">
            <p class="card-message">{{ cleanMessage(alert.message) }}</p>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { alertsApi } from '@/api/alerts'
import { servicesApi } from '@/api/services'
import type { Alert, ServiceWithStatus } from '@/types'
import Button from 'primevue/button'
import Select from 'primevue/select'

const route = useRoute()

const tab = ref<'active' | 'history'>('active')
const serviceFilter = ref('')
const typeFilter = ref('')
const alerts = ref<Alert[]>([])
const services = ref<ServiceWithStatus[]>([])
const loading = ref(false)
const dismissingAll = ref(false)

const serviceOptions = computed(() => [
  { id: '', name: 'All services' },
  ...services.value.map((s) => ({ id: s.id, name: s.name })),
])

const typeOptions = [
  { value: '', label: 'All types' },
  { value: 'DOWNTIME', label: 'Downtime' },
  { value: 'HIGH_LATENCY', label: 'High Latency' },
  { value: 'RECOVERY', label: 'Recovery' },
]

const activeCount = computed(() => alerts.value.filter((a) => !a.acknowledged).length)
const filteredActive = computed(() =>
  alerts.value.filter((a) => !a.acknowledged &&
    (!serviceFilter.value || a.service_id === serviceFilter.value) &&
    (!typeFilter.value || a.alert_type === typeFilter.value)
  )
)

const filtered = computed(() => {
  const isActive = tab.value === 'active'
  return alerts.value.filter((a) =>
    (isActive ? !a.acknowledged : a.acknowledged) &&
    (!serviceFilter.value || a.service_id === serviceFilter.value) &&
    (!typeFilter.value || a.alert_type === typeFilter.value)
  )
})

function cleanMessage(msg: string) {
  return msg.replace(/[*`]/g, '').replace(/\\n/g, '\n').trim()
}

function formatType(type: string) {
  switch (type) {
    case 'DOWNTIME': return 'Downtime'
    case 'HIGH_LATENCY': return 'High Latency'
    case 'RECOVERY': return 'Recovery'
    default: return type.replace('_', ' ')
  }
}

function formatRelative(iso: string) {
  const diff = Math.floor((Date.now() - new Date(iso).getTime()) / 1000)
  if (diff < 60) return `${diff}s ago`
  if (diff < 3600) return `${Math.floor(diff / 60)}m ago`
  if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`
  return new Date(iso).toLocaleDateString()
}

function accentClass(alert: Alert) {
  switch (alert.alert_type) {
    case 'DOWNTIME': return 'accent-danger'
    case 'HIGH_LATENCY': return 'accent-warning'
    case 'RECOVERY': return 'accent-success'
    default: return ''
  }
}

function pillClass(alert: Alert) {
  switch (alert.alert_type) {
    case 'DOWNTIME': return 'pill-danger'
    case 'HIGH_LATENCY': return 'pill-warning'
    case 'RECOVERY': return 'pill-success'
    default: return ''
  }
}

async function loadAlerts() {
  loading.value = true
  try {
    alerts.value = await alertsApi.list({ limit: 500 })
  } finally {
    loading.value = false
  }
}

async function dismiss(id: number) {
  await alertsApi.acknowledge(id)
  const a = alerts.value.find((x) => x.id === id)
  if (a) a.acknowledged = true
}

async function dismissAll() {
  dismissingAll.value = true
  try {
    const params: { service_id?: string; alert_type?: string } = {}
    if (serviceFilter.value) params.service_id = serviceFilter.value
    if (typeFilter.value) params.alert_type = typeFilter.value
    await alertsApi.acknowledgeAll(Object.keys(params).length ? params : undefined)
    await loadAlerts()
  } finally {
    dismissingAll.value = false
  }
}

onMounted(async () => {
  if (route.query.service_id) serviceFilter.value = route.query.service_id as string
  const [, svcs] = await Promise.all([loadAlerts(), servicesApi.list()])
  services.value = svcs
})
</script>

<style scoped>
.alerts-page {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

/* Header */
.page-header {
  margin-bottom: 24px;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  letter-spacing: -0.02em;
  margin: 0 0 4px;
}

.page-subtitle {
  font-size: 14px;
  color: var(--text-muted);
  margin: 0;
}

/* Toolbar */
.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 20px;
  flex-wrap: wrap;
  flex-shrink: 0;
}

.toolbar-left { display: flex; align-items: center; gap: 8px; }
.toolbar-right { display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }

.tab-group {
  display: flex;
  background: var(--bg-secondary);
  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 3px;
  gap: 2px;
  height: 2.25rem;
}

.tab-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 0 14px;
  height: 100%;
  border-radius: 8px;
  border: none;
  background: transparent;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-muted);
  cursor: pointer;
  transition: color 0.15s, background 0.15s, box-shadow 0.15s;
}

.tab-btn:hover { color: var(--text-secondary); }

.tab-btn.active {
  background: var(--bg-card);
  color: var(--text-primary);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.18);
}

.tab-count {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 17px;
  height: 17px;
  padding: 0 4px;
  border-radius: 999px;
  background: var(--danger);
  color: #fff;
  font-size: 12px;
  font-weight: 700;
}

.filter-select :deep(.p-select) {
  min-width: 130px;
  height: 2.25rem;
}

.filter-select :deep(.p-select-label) {
  padding-top: 0;
  padding-bottom: 0;
  line-height: 2.25rem;
}


/* Empty state */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 96px 0;
  color: var(--text-muted);
  font-size: 14px;
  flex: 1;
  min-height: 0;
}

.toolbar-btn {
  height: 2.25rem;
  padding-top: 0 !important;
  padding-bottom: 0 !important;
}

.empty-icon { opacity: 0.2; }

.empty-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-secondary);
  margin: 0;
}

.empty-sub {
  font-size: 16px;
  color: var(--text-muted);
  margin: 0;
}

/* Alert cards */
.alert-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex: 1;
  overflow-y: auto;
  min-height: 0;
}

.alert-card {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-left-width: 3px;
  border-left-color: transparent;
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  overflow: hidden;
  transition: box-shadow 0.15s;
}
.alert-card:hover { box-shadow: 0 4px 20px rgba(0, 0, 0, 0.25); }

.alert-card--dismissed { opacity: 0.65; }

.accent-danger  { border-left-color: var(--danger); }
.accent-warning { border-left-color: var(--warning); }
.accent-success { border-left-color: var(--success); }

/* Card header */
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 12px 16px;
  border-bottom: 1px solid var(--border);
  flex-wrap: wrap;
}

.card-header-left {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
}

.type-pill {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  white-space: nowrap;
  flex-shrink: 0;
}
.pill-danger  { background: rgba(239, 68, 68, 0.15);  color: var(--danger); }
.pill-warning { background: rgba(245, 158, 11, 0.15); color: var(--warning); }
.pill-success { background: rgba(34, 197, 94, 0.15);  color: var(--success); }

.service-link {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
  text-decoration: none;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: color 0.15s;
}
.service-link:hover { color: var(--accent); }

.card-header-right {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
}

.card-time {
  font-size: 14px;
  color: var(--text-muted);
  white-space: nowrap;
}


.card-dismissed-chip {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 3px 9px;
  border-radius: 6px;
  background: rgba(34, 197, 94, 0.1);
  color: var(--success);
  font-size: 13px;
  font-weight: 600;
}

/* Card body — the full message */
.card-body {
  padding: 14px 16px;
}

.card-message {
  font-size: 15px;
  color: var(--text-secondary);
  line-height: 1.7;
  white-space: pre-line;
  margin: 0;
  font-family: var(--font-mono);
}
</style>
