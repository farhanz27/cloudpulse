import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Alert } from '@/types'
import { alertsApi } from '@/api/alerts'

export const useAlertsStore = defineStore('alerts', () => {
  const alerts = ref<Alert[]>([])
  const loading = ref(false)

  async function fetchAlerts(params?: { service_id?: string; acknowledged?: boolean }) {
    loading.value = true
    try {
      alerts.value = await alertsApi.list(params)
    } finally {
      loading.value = false
    }
  }

  async function acknowledge(alertId: number) {
    const updated = await alertsApi.acknowledge(alertId)
    const idx = alerts.value.findIndex((a) => a.id === alertId)
    if (idx !== -1) alerts.value[idx] = updated
  }

  return { alerts, loading, fetchAlerts, acknowledge }
})
