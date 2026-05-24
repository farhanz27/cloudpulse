<template>
  <div class="incidents-view">
    <div class="page-header">
      <div>
        <h1 class="page-title">Incidents</h1>
      </div>
    </div>

    <!-- Filters -->
    <div class="filters-bar">
      <IconField class="search-wrap">
        <InputIcon class="pi pi-search" />
        <InputText
          v-model="searchQuery"
          placeholder="Search monitors…"
          autocomplete="off"
          class="search-input"
        />
      </IconField>

      <div class="filters-right">
        <Select
          v-model="filterStatus"
          :options="statusOptions"
          option-label="label"
          option-value="value"
          class="status-select"
        />
        <Select
          v-model="dateFilter"
          :options="dateOptions"
          option-label="label"
          option-value="value"
          class="date-select"
        />
      </div>
    </div>

    <!-- Loading skeleton -->
    <div v-if="loading" class="table-wrap">
      <table class="incidents-table">
        <thead>
          <tr>
            <th>Monitor</th><th>Started</th><th>Recovered</th><th>Duration</th><th>Root Cause</th><th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="i in 8" :key="i" class="skel-row">
            <td><div class="skel" style="width:120px"></div></td>
            <td><div class="skel" style="width:90px"></div></td>
            <td><div class="skel" style="width:90px"></div></td>
            <td><div class="skel" style="width:60px"></div></td>
            <td><div class="skel" style="width:180px"></div></td>
            <td><div class="skel" style="width:70px"></div></td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Error -->
    <div v-else-if="loadError" class="table-wrap">
      <table class="incidents-table">
        <tbody>
          <tr>
            <td colspan="6" class="empty-cell">
              <div class="empty-state">Failed to load incidents — refresh to try again.</div>
            </td>
          </tr>
        </tbody>
      </table>
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
            <th>Root Cause</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="incidents.length === 0">
            <td colspan="6" class="empty-cell">
              <div class="empty-state">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" width="36" height="36" class="empty-icon" aria-hidden="true">
                  <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" stroke-linecap="round" stroke-linejoin="round" />
                </svg>
                <p class="empty-title">No incidents recorded</p>
                <p class="empty-desc">Incidents open automatically when a monitor stays down for over 5 minutes.</p>
              </div>
            </td>
          </tr>
          <tr v-else v-for="inc in incidents" :key="inc.id">
            <td>
              <router-link :to="`/monitors/${inc.service_id}`" class="svc-link">
                {{ inc.service_name }}
              </router-link>
            </td>
            <td class="ts-cell">{{ formatDate(inc.started_at) }}</td>
            <td class="ts-cell">{{ inc.recovered_at ? formatDate(inc.recovered_at) : '—' }}</td>
            <td class="dur-cell">{{ inc.duration_seconds ? formatDuration(inc.duration_seconds) : (inc.status === 'open' ? ongoingDuration(inc.started_at) : '—') }}</td>
            <td class="cause-cell">{{ inc.root_cause || '—' }}</td>
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

    <div class="footer-row" v-if="!loading && !loadError">
      <p class="total-count">{{ total }} incident{{ total !== 1 ? 's' : '' }} total</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { incidentsApi } from '@/api/incidents'
import type { IncidentSummary } from '@/types'
import Select from 'primevue/select'
import InputText from 'primevue/inputtext'
import IconField from 'primevue/iconfield'
import InputIcon from 'primevue/inputicon'

const PAGE_SIZE = 20

const incidents = ref<IncidentSummary[]>([])
const total = ref(0)
const loading = ref(true)
const loadError = ref(false)
const page = ref(1)
const searchQuery = ref('')
const filterStatus = ref<'all' | 'open' | 'resolved'>('all')
const dateFilter = ref(0)

const totalPages = computed(() => Math.max(1, Math.ceil(total.value / PAGE_SIZE)))

const statusOptions = [
  { value: 'all' as const, label: 'All Status' },
  { value: 'open' as const, label: 'Open' },
  { value: 'resolved' as const, label: 'Resolved' },
]

const dateOptions = [
  { label: 'All time', value: 0 },
  { label: 'Today', value: 1 },
  { label: 'Last 7 days', value: 7 },
  { label: 'Last 30 days', value: 30 },
  { label: 'Last 90 days', value: 90 },
]

async function load() {
  loading.value = true
  loadError.value = false
  try {
    const params: Parameters<typeof incidentsApi.list>[0] = {
      limit: PAGE_SIZE,
      offset: (page.value - 1) * PAGE_SIZE,
    }
    if (searchQuery.value.trim()) params.search = searchQuery.value.trim()
    if (filterStatus.value !== 'all') params.status = filterStatus.value
    if (dateFilter.value > 0) {
      const since = new Date()
      since.setDate(since.getDate() - dateFilter.value)
      since.setHours(0, 0, 0, 0)
      params.since = since.toISOString()
    }
    const data = await incidentsApi.list(params)
    incidents.value = data.items
    total.value = data.total
  } catch {
    loadError.value = true
  } finally {
    loading.value = false
  }
}

watch([searchQuery, filterStatus, dateFilter], () => { page.value = 1; load() })
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

onMounted(load)
</script>

<style scoped>
.incidents-view { width: 100%; }

.page-header {
  margin-bottom: 24px;
}
.page-title { font-size: 30px; font-weight: 700; color: var(--text-primary); margin: 0 0 4px; }

.filters-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.filters-right {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.search-wrap { flex: 1; min-width: 160px; max-width: 280px; }
.search-input { width: 100%; }
.search-wrap :deep(.p-inputtext),
.search-wrap :deep(.p-iconfield) { height: 36px; }
.search-wrap :deep(.p-inputtext) { padding-top: 0; padding-bottom: 0; }

.status-select { min-width: 140px; }
.status-select :deep(.p-select) { height: 36px; }
.status-select :deep(.p-select-label) { padding-top: 0; padding-bottom: 0; line-height: 36px; }

.date-select { min-width: 140px; }
.date-select :deep(.p-select) { height: 36px; }
.date-select :deep(.p-select-label) { padding-top: 0; padding-bottom: 0; line-height: 36px; }

.table-wrap {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 12px;
  overflow-x: auto;
}

.incidents-table {
  width: 100%;
  min-height: 100%;
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
.incidents-table tbody tr:hover td { background: var(--bg-hover); }
.incidents-table tbody tr:has(.empty-cell):hover td { background: transparent; }

.svc-link {
  font-weight: 600;
  color: var(--accent);
  text-decoration: none;
}
.svc-link:hover { text-decoration: underline; }

.ts-cell { color: var(--text-secondary); font-size: 14px; white-space: nowrap; }
.dur-cell { color: var(--text-secondary); font-size: 14px; white-space: nowrap; }
.cause-cell {
  color: var(--text-muted);
  font-size: 13px;
  max-width: 280px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

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
.page-info { font-size: 14px; color: var(--text-muted); }

.footer-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 10px;
  flex-shrink: 0;
}
.total-count { font-size: 14px; color: var(--text-muted); }

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
