<template>
  <div class="chart-wrapper">
    <Bar v-if="chartData" :data="chartData" :options="chartOptions" />
    <p v-else class="no-data">No data available</p>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Bar } from 'vue-chartjs'
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js'
import type { MetricPoint } from '@/types'

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend)

const props = withDefaults(
  defineProps<{ data: MetricPoint[]; rangeHours?: number }>(),
  { rangeHours: 24 }
)

function formatAxisLabel(iso: string) {
  const d = new Date(iso)
  if (props.rangeHours > 24) {
    return d.toLocaleString([], {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    })
  }
  return d.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}

const chartData = computed(() => {
  if (!props.data.length) return null

  const labels = props.data.map((p) => formatAxisLabel(p.timestamp))

  return {
    labels,
    datasets: [
      {
        label: 'Uptime %',
        data: props.data.map((p) => p.uptime_percent),
        backgroundColor: props.data.map((p) =>
          p.uptime_percent >= 99
            ? 'rgba(34, 197, 94, 0.6)'
            : p.uptime_percent >= 95
            ? 'rgba(245, 158, 11, 0.6)'
            : 'rgba(239, 68, 68, 0.6)'
        ),
        borderRadius: 4,
      },
    ],
  }
})

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: { display: false },
    tooltip: {
      backgroundColor: '#1e293b',
      titleColor: '#f1f5f9',
      bodyColor: '#94a3b8',
      borderColor: '#334155',
      borderWidth: 1,
    },
  },
  scales: {
    x: {
      ticks: { color: '#64748b', maxTicksLimit: 12 },
      grid: { display: false },
    },
    y: {
      min: 0,
      max: 100,
      ticks: { color: '#64748b' },
      grid: { color: 'rgba(51, 65, 85, 0.5)' },
    },
  },
}
</script>

<style scoped>
.chart-wrapper {
  height: 200px;
  position: relative;
}
.no-data {
  color: var(--text-muted);
  text-align: center;
  padding: 40px 0;
}
</style>
