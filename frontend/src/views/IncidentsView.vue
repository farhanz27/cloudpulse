<template>
  <div class="incidents-view">
    <div class="page-header">
      <div>
        <h1 class="page-title">Incidents</h1>
        <p class="page-subtitle">Downtime events across all monitors</p>
      </div>
    </div>

    <!-- Filters -->
    <div class="filters-bar">
      <Select
        v-model="filterServiceId"
        :options="serviceOptions"
        option-label="name"
        option-value="id"
        placeholder="All Monitors"
        class="svc-select"
      />

      <Select
        v-model="filterStatus"
        :options="statusOptions"
        option-label="label"
        option-value="value"
        class="status-select"
      />
    </div>

    <!-- Loading skeleton -->
    <div v-if="loading" class="table-wrap">
      <table class="incidents-table">
        <thead>
          <tr>
            <th>Monitor</th><th>Started</th><th>Recovered</th><th>Duration</th><th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="i in 8" :key="i" class="skel-row">
            <td><div class="skel" style="width:120px"></div></td>
            <td><div class="skel" style="width:90px"></div></td>
            <td><div class="skel" style="width:90px"></div></td>
            <td><div class="skel" style="width:60px"></div></td>
            <td><div class="skel" style="width:70px"></div></td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Empty state -->
    <div v-else-if="incidents.length === 0" class="empty-state">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" width="40" height="40" aria-hidden="true">
        <path d="M12 9v4M12 17h.01" stroke-linecap="round" stroke-linejoin="round" />
        <path d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z" stroke-linecap="round" stroke-linejoin="round" />
      </svg>
      <p>No incidents found</p>
    </div>

    <!-- Table -->
    <div v-else class="table-wrap">
      <table class="incidents-table">
        <thead>
          <tr>
            <th>Monitor</th>
            <th>Started</th>
            <th>Recovered</th>
            <th>Duration</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="inc in incidents" :key="inc.id">
            <td>
              <router-link :to="`/services/${inc.service_id}`" class="svc-link">
                {{ inc.service_name }}
              </router-link>
            </td>
            <td class="ts-cell">{{ formatDate(inc.started_at) }}</td>
            <td class="ts-cell">{{ inc.recovered_at ? formatDate(inc.recovered_at) : '—' }}</td>
            <td class="dur-cell">{{ inc.duration_seconds ? formatDuration(inc.duration_seconds) : (inc.status === 'open' ? ongoingDuration(inc.started_at) : '—') }}</td>
            <td>
              <span :class="['status-badge', inc.status === 'open' ? 'badge-danger' : 'badge-success']">
                {{ inc.status === 'open' ? 'Open' : 'Resolved' }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- Pagination -->
      <div class="pagination" v-if="totalPages > 1">
        <button class="page-btn" :disabled="page === 1" @click="page--">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14">
            <path d="M15 18l-6-6 6-6" stroke-linecap="round" stroke-linejoin="round" />
          </svg>
        </button>
        <span class="page-info">{{ page }} / {{ totalPages }}</span>
        <button class="page-btn" :disabled="page === totalPages" @click="page++">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14">
            <path d="M9 18l6-6-6-6" stroke-linecap="round" stroke-linejoin="round" />
          </svg>
        </button>
      </div>
    </div>

    <div class="footer-row" v-if="!loading">
      <p class="total-count">{{ total }} incident{{ total !== 1 ? 's' : '' }} total</p>
      <router-link to="/alerts" class="event-log-link">
        View raw event log
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="12" height="12">
          <path d="M5 12h14M12 5l7 7-7 7" stroke-linecap="round" stroke-linejoin="round" />
        </svg>
      </router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { incidentsApi } from '@/api/incidents'
import { servicesApi } from '@/api/services'
import type { IncidentSummary, ServiceWithStatus } from '@/types'
import Select from 'primevue/select'

const PAGE_SIZE = 20

const incidents = ref<IncidentSummary[]>([])
const total = ref(0)
const loading = ref(true)
const page = ref(1)
const filterServiceId = ref('')
const filterStatus = ref<'all' | 'open' | 'resolved'>('all')
const services = ref<ServiceWithStatus[]>([])

const totalPages = computed(() => Math.max(1, Math.ceil(total.value / PAGE_SIZE)))

const statusOptions = [
  { value: 'all' as const, label: 'All Status' },
  { value: 'open' as const, label: 'Open' },
  { value: 'resolved' as const, label: 'Resolved' },
]

const serviceOptions = computed(() => [
  { id: '', name: 'All Monitors' },
  ...services.value.map((s) => ({ id: s.id, name: s.name })),
])

async function load() {
  loading.value = true
  try {
    const params: Parameters<typeof incidentsApi.list>[0] = {
      limit: PAGE_SIZE,
      offset: (page.value - 1) * PAGE_SIZE,
    }
    if (filterServiceId.value) params.service_id = filterServiceId.value
    if (filterStatus.value !== 'all') params.status = filterStatus.value
    const data = await incidentsApi.list(params)
    incidents.value = data.items
    total.value = data.total
  } finally {
    loading.value = false
  }
}

watch([filterServiceId, filterStatus], () => { page.value = 1; load() })
watch(page, load)

function formatDate(ts: string) {
  const d = new Date(ts)
  return d.toLocaleString(undefined, { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' })
}

function formatDuration(sec: number) {
  if (sec < 60) return `${sec}s`
  if (sec < 3600) return `${Math.floor(sec / 60)}m`
  const h = Math.floor(sec / 3600)
  const m = Math.floor((sec % 3600) / 60)
  return m > 0 ? `${h}h ${m}m` : `${h}h`
}

function ongoingDuration(startedAt: string) {
  const sec = Math.floor((Date.now() - new Date(startedAt).getTime()) / 1000)
  return formatDuration(sec) + ' (ongoing)'
}

onMounted(async () => {
  const [, svcs] = await Promise.allSettled([load(), servicesApi.list()])
  if (svcs.status === 'fulfilled') services.value = svcs.value
})
</script>

<style scoped>
.incidents-view { width: 100%; height: 100%; display: flex; flex-direction: column; }

.page-header {
  margin-bottom: 24px;
}
.page-title { font-size: 28px; font-weight: 700; color: var(--text-primary); margin: 0 0 4px; }
.page-subtitle { font-size: 15px; color: var(--text-muted); margin: 0; }

.filters-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.svc-select { min-width: 180px; }
.status-select { min-width: 140px; }

.table-wrap {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 12px;
  overflow: auto;
  flex: 1;
  min-height: 0;
}

.incidents-table {
  width: 100%;
  border-collapse: collapse;
}
.incidents-table th {
  padding: 12px 16px;
  font-size: 12px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: var(--text-muted);
  text-align: left;
  border-bottom: 1px solid var(--border);
  background: var(--bg-secondary);
}
.incidents-table td {
  padding: 14px 16px;
  font-size: 15px;
  color: var(--text-primary);
  border-bottom: 1px solid var(--border);
}
.incidents-table tbody tr:last-child td { border-bottom: none; }
.incidents-table tbody tr:hover td { background: var(--bg-hover); }

.svc-link {
  font-weight: 600;
  color: var(--accent);
  text-decoration: none;
}
.svc-link:hover { text-decoration: underline; }

.ts-cell { color: var(--text-secondary); font-size: 15px; white-space: nowrap; }
.dur-cell { color: var(--text-secondary); font-size: 15px; white-space: nowrap; }

.status-badge {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}
.badge-danger { background: rgba(239,68,68,0.12); color: var(--danger); }
.badge-success { background: rgba(34,197,94,0.12); color: var(--success); }

.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 14px;
  border-top: 1px solid var(--border);
}
.page-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  border-radius: 6px;
  border: 1px solid var(--border);
  background: var(--bg-secondary);
  color: var(--text-secondary);
  cursor: pointer;
  transition: background 0.15s, color 0.15s;
}
.page-btn:disabled { opacity: 0.35; cursor: default; }
.page-btn:not(:disabled):hover { background: var(--bg-hover); color: var(--text-primary); }
.page-info { font-size: 15px; color: var(--text-muted); }

.footer-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 10px;
  flex-shrink: 0;
}
.total-count { font-size: 15px; color: var(--text-muted); }
.event-log-link {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: var(--text-muted);
  text-decoration: none;
  transition: color 0.15s;
}
.event-log-link:hover { color: var(--accent); }

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 60px 20px;
  color: var(--text-muted);
  font-size: 14px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 12px;
  flex: 1;
  min-height: 0;
}

/* Skeleton */
.skel-row td { padding: 14px 16px; }
.skel {
  height: 14px;
  border-radius: 6px;
  background: var(--bg-hover);
  animation: shimmer 1.4s ease-in-out infinite;
}
@keyframes shimmer {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.4; }
}
</style>
