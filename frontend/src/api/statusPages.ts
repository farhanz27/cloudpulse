import client from './client'
import type { StatusPage, StatusPageDetail, StatusPagePublic } from '@/types'

export const statusPagesApi = {
  list: () =>
    client.get<StatusPage[]>('/status-pages').then((r) => r.data),

  get: (id: string) =>
    client.get<StatusPageDetail>(`/status-pages/${id}`).then((r) => r.data),

  create: (payload: { name: string; slug: string; description?: string; service_ids: string[] }) =>
    client.post<StatusPage>('/status-pages', payload).then((r) => r.data),

  update: (id: string, payload: { name?: string; slug?: string; description?: string; service_ids?: string[] }) =>
    client.patch<StatusPage>(`/status-pages/${id}`, payload).then((r) => r.data),

  delete: (id: string) =>
    client.delete(`/status-pages/${id}`),

  getPublic: (slug: string) =>
    client.get<StatusPagePublic>(`/status-pages/public/${slug}`).then((r) => r.data),
}
