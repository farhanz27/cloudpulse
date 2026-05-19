<template>
  <div :class="['uptime-bars', `uptime-bars--${props.size ?? 'sm'}`]">
    <div
      v-for="(pct, i) in bars"
      :key="i"
      :class="['bar', barClass(pct)]"
      :title="tooltip(pct, i)"
    ></div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{
  bars: (number | null)[]
  dates?: string[]
  size?: 'sm' | 'lg'
}>()

function barClass(pct: number | null) {
  if (pct === null) return 'bar-none'
  if (pct === 100) return 'bar-perfect'
  if (pct >= 95)   return 'bar-good'
  if (pct >= 50)   return 'bar-warn'
  if (pct > 0)     return 'bar-bad'
  return 'bar-down'
}

function tooltip(pct: number | null, i: number) {
  const date = props.dates?.[i] ?? `Day ${i + 1}`
  if (pct === null) return `${date}: No data`
  return `${date}: ${pct}%`
}
</script>

<style scoped>
.uptime-bars {
  display: flex;
  align-items: flex-end;
  gap: 2px;
}

/* Small variant (dashboard table) */
.uptime-bars--sm .bar {
  width: 5px;
  height: 20px;
  border-radius: 2px;
  flex-shrink: 0;
  transition: opacity 0.15s;
  cursor: default;
}

/* Large variant (service detail) */
.uptime-bars--lg {
  gap: 3px;
  flex-wrap: nowrap;
}
.uptime-bars--lg .bar {
  flex: 1;
  min-width: 4px;
  height: 32px;
  border-radius: 3px;
  flex-shrink: 0;
  transition: opacity 0.15s, transform 0.15s;
  cursor: default;
}
.uptime-bars--lg .bar:hover {
  opacity: 0.8;
  transform: scaleY(1.08);
  transform-origin: bottom;
}

.bar:hover { opacity: 0.75; }

.bar-none    { background: var(--bg-hover); }
.bar-perfect { background: var(--success); }
.bar-good    { background: #86efac; }
.bar-warn    { background: var(--warning); }
.bar-bad     { background: #f97316; }
.bar-down    { background: var(--danger); }
</style>
