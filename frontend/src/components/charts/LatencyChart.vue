<template>
  <div class="chart-wrapper">
    <Line v-if="chartData" :data="chartData" :options="chartOptions" />
    <p v-else class="no-data">No data available</p>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Line } from 'vue-chartjs'
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler,
} from 'chart.js'
import type { MetricPoint } from '@/types'

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend, Filler)

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
        label: 'Avg Response Time (ms)',
        data: props.data.map((p) => p.avg_response_time_ms),
        borderColor: '#3b82f6',
        backgroundColor: 'rgba(59, 130, 246, 0.1)',
        fill: true,
        tension: 0.3,
        pointRadius: 0,
        pointHoverRadius: 4,
      },
      {
        label: 'Max Response Time (ms)',
        data: props.data.map((p) => p.max_response_time_ms),
        borderColor: 'rgba(239, 68, 68, 0.5)',
        borderDash: [4, 4],
        fill: false,
        tension: 0.3,
        pointRadius: 0,
        pointHoverRadius: 4,
      },
    ],
  }
})

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  interaction: { intersect: false, mode: 'index' as const },
  plugins: {
    legend: {
      labels: { color: '#94a3b8', font: { size: 12 } },
    },
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
      grid: { color: 'rgba(51, 65, 85, 0.5)' },
    },
    y: {
      ticks: { color: '#64748b' },
      grid: { color: 'rgba(51, 65, 85, 0.5)' },
    },
  },
}
</script>

<style scoped>
.chart-wrapper {
  height: 300px;
  position: relative;
}
.no-data {
  color: var(--text-muted);
  text-align: center;
  padding: 60px 0;
}
</style>
