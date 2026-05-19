<template>
  <span :class="['badge', badgeClass]">
    <span :class="['dot', { 'dot-pulse': animated && (status === 'DOWN' || status === 'DEGRADED') }]"></span>
    {{ label }}
  </span>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{ status: string | null; animated?: boolean }>()

const badgeClass = computed(() => {
  switch (props.status) {
    case 'UP': return 'badge-up'
    case 'DOWN': return 'badge-down'
    case 'DEGRADED': return 'badge-degraded'
    default: return 'badge-unknown'
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

.dot-pulse {
  animation: dot-pulse 1.4s ease-out infinite;
}

@keyframes dot-pulse {
  0%   { box-shadow: 0 0 0 0 currentColor; opacity: 1; }
  70%  { box-shadow: 0 0 0 6px transparent; opacity: 0.8; }
  100% { box-shadow: 0 0 0 0 transparent; }
}
</style>
