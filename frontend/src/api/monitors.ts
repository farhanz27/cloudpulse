import client from './client'
import type { MonitorWithStatus, MonitorCreate, MonitorUpdate } from '@/types'

export const monitorsApi = {
  list: () =>
    client.get<MonitorWithStatus[]>('/services').then((r) => r.data),

  get: (id: string) =>
    client.get<MonitorWithStatus>(`/services/${id}`).then((r) => r.data),

  create: (data: MonitorCreate) =>
    client.post<MonitorWithStatus>('/services', data).then((r) => r.data),

  update: (id: string, data: MonitorUpdate) =>
    client.patch<MonitorWithStatus>(`/services/${id}`, data).then((r) => r.data),

  delete: (id: string) =>
    client.delete(`/services/${id}`),

  toggleActive: (id: string, isActive: boolean) =>
    client.patch<MonitorWithStatus>(`/services/${id}`, { is_active: isActive }).then((r) => r.data),

  checkNow: (id: string) =>
    client.post<MonitorWithStatus>(`/services/${id}/check`).then((r) => r.data),

  testNotification: (id: string) =>
    client.post<{ ok: boolean; channels_notified: number }>(`/services/${id}/test-notification`).then((r) => r.data),

  mute: (id: string, minutes: number | null) =>
    client.post<MonitorWithStatus>(`/services/${id}/mute`, { minutes }).then((r) => r.data),

  clearAllHealthLogs: () =>
    client.delete('/services/health-logs'),
}
