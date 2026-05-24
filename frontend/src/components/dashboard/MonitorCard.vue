<template>
  <div class="service-card" :class="[statusBorderClass, { 'card-paused': !service.is_active }]" @click="router.push(`/monitors/${service.id}`)">

    <!-- Left: dot + url on one line, uptime-since below -->
    <div class="card-left">
      <div class="url-row">
        <StatusBadge :status="service.is_active ? service.current_status : null" :animated="service.is_active" :dot-only="true" />
        <span class="service-url" :title="service.url">{{ service.url }}</span>
        <span v-if="!service.is_active" class="pill pill-paused">Paused</span>
        <span v-else-if="isMuted" class="pill pill-muted">Muted</span>
      </div>
      <span v-if="uptimeSince" class="uptime-since" :class="uptimeSinceClass">{{ uptimeSince }}</span>
    </div>

    <!-- Center: uptime bars + % below -->
    <div class="card-bars">
      <UptimeBars :bars="uptimeBars" :dates="uptimeDates" size="sm" />
      <span class="bars-pct" :class="uptimePctClass">
        {{ service.uptime_percent != null ? `${service.uptime_percent}%` : '—' }}
      </span>
    </div>

    <!-- Right: action button -->
    <div class="card-right">
      <button class="btn-menu" title="Actions" @click.stop="$emit('openMenu', $event)">
        <svg viewBox="0 0 24 24" fill="currentColor" width="15" height="15" aria-hidden="true">
          <circle cx="12" cy="5" r="1.5" /><circle cx="12" cy="12" r="1.5" /><circle cx="12" cy="19" r="1.5" />
        </svg>
      </button>
    </div>

  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import type { MonitorWithStatus } from '@/types'
import StatusBadge from '@/components/common/StatusBadge.vue'
import UptimeBars from '@/components/common/UptimeBars.vue'

const props = defineProps<{
  service: MonitorWithStatus
  uptimeBars: (number | null)[]
  uptimeDates: string[]
  uptimeSince: string | null
}>()

defineEmits<{ openMenu: [event: MouseEvent] }>()

const router = useRouter()

const isMuted = computed(() =>
  !!props.service.muted_until && new Date(props.service.muted_until) > new Date()
)

const uptimeSinceClass = computed(() =>
  props.service.current_status === 'DOWN' ? 'hint-down' : 'hint-up'
)

const statusBorderClass = computed(() => {
  if (!props.service.is_active) return ''
  switch (props.service.current_status) {
    case 'DOWN': return 'border-down'
    case 'DEGRADED': return 'border-degraded'
    case 'UP': return 'border-up'
    default: return ''
  }
})

const uptimePctClass = computed(() => {
  const pct = props.service.uptime_percent
  if (pct === null) return ''
  if (pct >= 99) return 'text-success'
  if (pct >= 95) return 'text-warning'
  return 'text-danger'
})
</script>

<style scoped>
.service-card {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 12px 16px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-left: 3px solid var(--border);
  border-radius: var(--radius);
  cursor: pointer;
  transition: transform 0.15s, box-shadow 0.15s, border-color 0.15s;
}
.service-card:hover {
  transform: translateY(-1px);
  box-shadow: var(--shadow-lg);
  border-color: var(--border-strong);
}
.border-up       { border-left-color: var(--success); }
.border-down     { border-left-color: var(--danger); }
.border-degraded { border-left-color: var(--warning); }
.card-paused     { opacity: 0.55; }

/* Left section */
.card-left {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
  min-width: 0;
}

.url-row {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
}

.service-url {
  font-size: 13px;
  font-weight: 600;
  font-family: var(--font-mono);
  color: var(--text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  min-width: 0;
}

.uptime-since {
  font-size: 11px;
  opacity: 0.85;
  padding-left: 17px;
}
.hint-up   { color: var(--success); }
.hint-down { color: var(--danger); }

/* Center: bars + % inline */
.card-bars {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}
.card-bars :deep(.uptime-bars--sm .bar) {
  height: 24px;
  border-radius: 3px;
}

.bars-pct {
  font-size: 11px;
  font-weight: 700;
  font-family: var(--font-mono);
  color: var(--text-secondary);
}

/* Right: button only */
.card-right {
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

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

/* Action button */
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
  flex-shrink: 0;
  transition: color 0.15s, background 0.15s, border-color 0.15s;
}
.btn-menu:hover {
  color: var(--text-primary);
  background: var(--bg-hover);
  border-color: var(--border);
}

.text-success { color: var(--success); }
.text-warning { color: var(--warning); }
.text-danger  { color: var(--danger); }
</style>
