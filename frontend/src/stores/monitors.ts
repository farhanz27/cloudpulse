import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { MonitorWithStatus, MonitorCreate, MonitorUpdate } from '@/types'
import { monitorsApi } from '@/api/monitors'

export const useMonitorsStore = defineStore('monitors', () => {
  const services = ref<MonitorWithStatus[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function fetchAll() {
    if (!services.value.length) loading.value = true
    error.value = null
    try {
      services.value = await monitorsApi.list()
    } catch (e: any) {
      error.value = e.message || 'Failed to fetch services'
    } finally {
      loading.value = false
    }
  }

  async function create(data: MonitorCreate) {
    const svc = await monitorsApi.create(data)
    services.value.push(svc)
    return svc
  }

  async function update(id: string, data: MonitorUpdate) {
    const svc = await monitorsApi.update(id, data)
    const idx = services.value.findIndex((s) => s.id === id)
    if (idx !== -1) services.value[idx] = svc
    return svc
  }

  async function remove(id: string) {
    await monitorsApi.delete(id)
    services.value = services.value.filter((s) => s.id !== id)
  }

  return { services, loading, error, fetchAll, create, update, remove }
})
