import client from './client'
import type { IncidentsPage } from '@/types'

export const incidentsApi = {
  list: (params?: { service_id?: string; status?: 'open' | 'resolved'; search?: string; since?: string; limit?: number; offset?: number }) =>
    client
      .get<IncidentsPage>('/incidents', { params })
      .then((r) => r.data),
}
