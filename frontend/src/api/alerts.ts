import client from './client'
import type { Alert } from '@/types'

export const alertsApi = {
  list: (params?: { service_id?: string; acknowledged?: boolean; limit?: number; offset?: number }) =>
    client.get<Alert[]>('/alerts', { params }).then((r) => r.data),

  acknowledge: (alertId: number) =>
    client.patch<Alert>(`/alerts/${alertId}/acknowledge`).then((r) => r.data),

  acknowledgeAll: (params?: { service_id?: string; alert_type?: string }) =>
    client.post('/alerts/acknowledge-all', null, { params }).then((r) => r.data),
}
