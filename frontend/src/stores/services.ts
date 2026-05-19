import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { ServiceWithStatus, ServiceCreate, ServiceUpdate } from '@/types'
import { servicesApi } from '@/api/services'

export const useServicesStore = defineStore('services', () => {
  const services = ref<ServiceWithStatus[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function fetchAll() {
    loading.value = true
    error.value = null
    try {
      services.value = await servicesApi.list()
    } catch (e: any) {
      error.value = e.message || 'Failed to fetch services'
    } finally {
      loading.value = false
    }
  }

  async function create(data: ServiceCreate) {
    const svc = await servicesApi.create(data)
    services.value.push(svc)
    return svc
  }

  async function update(id: string, data: ServiceUpdate) {
    const svc = await servicesApi.update(id, data)
    const idx = services.value.findIndex((s) => s.id === id)
    if (idx !== -1) services.value[idx] = svc
    return svc
  }

  async function remove(id: string) {
    await servicesApi.delete(id)
    services.value = services.value.filter((s) => s.id !== id)
  }

  return { services, loading, error, fetchAll, create, update, remove }
})
