<template>
  <router-link :to="`/services/${service.id}`" class="service-card card">
    <div class="card-header">
      <h3 class="service-name">{{ service.name }}</h3>
      <div class="header-badges">
        <span v-if="service.keep_alive" class="keep-alive-tag">Keep-Alive</span>
        <StatusBadge :status="service.current_status" />
      </div>
    </div>
    <p class="service-url">{{ service.url }}</p>
    <div class="card-stats">
      <div class="stat">
        <span class="stat-label">Response</span>
        <span class="stat-value" :class="latencyClass">
          {{ service.last_response_time_ms ? `${Math.round(service.last_response_time_ms)}ms` : '—' }}
        </span>
      </div>
      <div class="stat">
        <span class="stat-label">Uptime (24h)</span>
        <span class="stat-value" :class="uptimeClass">
          {{ service.uptime_percent !== null ? `${service.uptime_percent}%` : '—' }}
        </span>
      </div>
      <div class="stat">
        <span class="stat-label">Last Check</span>
        <span class="stat-value">{{ lastChecked }}</span>
      </div>
    </div>
  </router-link>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { ServiceWithStatus } from '@/types'
import StatusBadge from '@/components/common/StatusBadge.vue'

const props = defineProps<{ service: ServiceWithStatus }>()

const latencyClass = computed(() => {
  const ms = props.service.last_response_time_ms
  if (!ms) return ''
  if (ms < 500) return 'text-success'
  if (ms < 2000) return 'text-warning'
  return 'text-danger'
})

const uptimeClass = computed(() => {
  const pct = props.service.uptime_percent
  if (pct === null) return ''
  if (pct >= 99) return 'text-success'
  if (pct >= 95) return 'text-warning'
  return 'text-danger'
})

const lastChecked = computed(() => {
  if (!props.service.last_checked_at) return '—'
  const d = new Date(props.service.last_checked_at)
  const diff = Math.floor((Date.now() - d.getTime()) / 1000)
  if (diff < 60) return `${diff}s ago`
  if (diff < 3600) return `${Math.floor(diff / 60)}m ago`
  return `${Math.floor(diff / 3600)}h ago`
})
</script>

<style scoped>
.service-card {
  display: block;
  text-decoration: none;
  color: inherit;
  transition: all 0.2s ease;
}
.service-card:hover {
  border-color: var(--accent);
  box-shadow: var(--shadow-lg);
  transform: translateY(-2px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 8px;
}

.service-name {
  font-size: 16px;
  font-weight: 600;
  min-width: 0;
  flex: 1;
  line-height: 1.3;
}

.header-badges {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.service-url {
  font-size: 13px;
  color: var(--text-muted);
  font-family: var(--font-mono);
  margin-bottom: 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.stat {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-label {
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  color: var(--text-muted);
}

.stat-value {
  font-size: 15px;
  font-weight: 600;
  font-family: var(--font-mono);
}

.text-success { color: var(--success); }
.text-warning { color: var(--warning); }
.text-danger { color: var(--danger); }

.keep-alive-tag {
  font-size: 10px;
  padding: 4px 8px;
  border-radius: 4px;
  background: rgba(59, 130, 246, 0.15);
  color: var(--accent);
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  white-space: nowrap;
}
</style>
