<template>
  <span v-if="dotOnly" :class="['status-dot', dotColorClass, { 'dot-pulse': animated && (status === 'DOWN' || status === 'DEGRADED') }]"></span>
  <span v-else :class="['badge', badgeClass]">
    <span :class="['dot', { 'dot-pulse': animated && (status === 'DOWN' || status === 'DEGRADED') }]"></span>
    {{ label }}
  </span>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{ status: string | null; animated?: boolean; dotOnly?: boolean }>()

const badgeClass = computed(() => {
  switch (props.status) {
    case 'UP': return 'badge-up'
    case 'DOWN': return 'badge-down'
    case 'DEGRADED': return 'badge-degraded'
    default: return 'badge-unknown'
  }
})

const dotColorClass = computed(() => {
  switch (props.status) {
    case 'UP': return 'dot-up'
    case 'DOWN': return 'dot-down'
    case 'DEGRADED': return 'dot-degraded'
    default: return 'dot-unknown'
  }
})

const label = computed(() => props.status || 'N/A')
</script>

<style scoped>
.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: currentColor;
  display: inline-block;
  flex-shrink: 0;
}

.status-dot {
  display: inline-block;
  width: 9px;
  height: 9px;
  border-radius: 50%;
  flex-shrink: 0;
}
.dot-up       { background: var(--success); }
.dot-down     { background: var(--danger); }
.dot-degraded { background: var(--warning); }
.dot-unknown  { background: var(--text-muted); }

.dot-pulse {
  animation: dot-pulse 1.4s ease-out infinite;
}

@keyframes dot-pulse {
  0%   { box-shadow: 0 0 0 0 currentColor; opacity: 1; }
  70%  { box-shadow: 0 0 0 6px transparent; opacity: 0.8; }
  100% { box-shadow: 0 0 0 0 transparent; }
}
</style>
