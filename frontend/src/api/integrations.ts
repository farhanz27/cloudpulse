import client from './client'
import type { Integration } from '@/types'

export const integrationsApi = {
  list: () =>
    client.get<Integration[]>('/integrations').then((r) => r.data),

  create: (payload: { type: string; name: string; config: Record<string, unknown>; enabled?: boolean }) =>
    client.post<Integration>('/integrations', payload).then((r) => r.data),

  update: (id: number, payload: { name?: string; config?: Record<string, unknown>; enabled?: boolean }) =>
    client.patch<Integration>(`/integrations/${id}`, payload).then((r) => r.data),

  delete: (id: number) =>
    client.delete(`/integrations/${id}`),

  test: (id: number) =>
    client.post<{ ok: boolean }>(`/integrations/${id}/test`).then((r) => r.data),
}

export const telegramApi = {
  startLink: () =>
    client.post<{ code: string; bot_username: string; expires_in_minutes: number }>('/telegram/start-link').then((r) => r.data),

  linkStatus: (code: string) =>
    client.get<{ linked: boolean; integration_id: number | null }>(`/telegram/link-status/${code}`).then((r) => r.data),
}
