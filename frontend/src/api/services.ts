import client from './client'
import type { ServiceWithStatus, ServiceCreate, ServiceUpdate } from '@/types'

export const servicesApi = {
  list: () =>
    client.get<ServiceWithStatus[]>('/services').then((r) => r.data),

  get: (id: string) =>
    client.get<ServiceWithStatus>(`/services/${id}`).then((r) => r.data),

  create: (data: ServiceCreate) =>
    client.post<ServiceWithStatus>('/services', data).then((r) => r.data),

  update: (id: string, data: ServiceUpdate) =>
    client.patch<ServiceWithStatus>(`/services/${id}`, data).then((r) => r.data),

  delete: (id: string) =>
    client.delete(`/services/${id}`),

  toggleActive: (id: string, isActive: boolean) =>
    client.patch<ServiceWithStatus>(`/services/${id}`, { is_active: isActive }).then((r) => r.data),

  checkNow: (id: string) =>
    client.post<ServiceWithStatus>(`/services/${id}/check`).then((r) => r.data),

  testNotification: (id: string) =>
    client.post<{ ok: boolean; channels_notified: number }>(`/services/${id}/test-notification`).then((r) => r.data),

  mute: (id: string, minutes: number | null) =>
    client.post<ServiceWithStatus>(`/services/${id}/mute`, { minutes }).then((r) => r.data),
}
