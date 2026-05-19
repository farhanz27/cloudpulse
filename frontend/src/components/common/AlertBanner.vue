<template>
  <div v-if="alerts.length" class="alert-banner">
    <div v-for="alert in alerts" :key="alert.id" :class="['alert-item', alertClass(alert)]">
      <div class="alert-content">
        <span class="alert-type">{{ alert.alert_type }}</span>
        <span class="alert-message">{{ cleanMessage(alert.message) }}</span>
        <span class="alert-time">{{ formatTime(alert.created_at) }}</span>
      </div>
      <button
        v-if="!alert.acknowledged"
        class="btn btn-ghost btn-sm"
        @click="$emit('acknowledge', alert.id)"
      >
        Dismiss
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Alert } from '@/types'

defineProps<{ alerts: Alert[] }>()
defineEmits<{ acknowledge: [id: number] }>()

function alertClass(alert: Alert) {
  switch (alert.alert_type) {
    case 'DOWNTIME': return 'alert-danger'
    case 'HIGH_LATENCY': return 'alert-warning'
    case 'RECOVERY': return 'alert-success'
    default: return ''
  }
}

function cleanMessage(msg: string) {
  return msg.replace(/[*`]/g, '')
}

function formatTime(ts: string) {
  return new Date(ts).toLocaleString()
}
</script>

<style scoped>
.alert-banner {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 24px;
}

.alert-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-radius: var(--radius-sm);
  border: 1px solid;
}

.alert-danger {
  background: rgba(239, 68, 68, 0.08);
  border-color: rgba(239, 68, 68, 0.3);
}
.alert-warning {
  background: rgba(245, 158, 11, 0.08);
  border-color: rgba(245, 158, 11, 0.3);
}
.alert-success {
  background: rgba(34, 197, 94, 0.08);
  border-color: rgba(34, 197, 94, 0.3);
}

.alert-content {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
  min-width: 0;
}

.alert-type {
  font-size: 11px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  white-space: nowrap;
}

.alert-danger .alert-type { color: var(--danger); }
.alert-warning .alert-type { color: var(--warning); }
.alert-success .alert-type { color: var(--success); }

.alert-message {
  font-size: 13px;
  color: var(--text-secondary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.alert-time {
  font-size: 12px;
  color: var(--text-muted);
  white-space: nowrap;
}

.btn-sm {
  padding: 4px 12px;
  font-size: 12px;
}
</style>
