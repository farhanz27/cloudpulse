import client from './client'
import type { HealthLogsPage, MetricsResponse, LastIncident, Incident, ReliabilityMetrics } from '@/types'

export const metricsApi = {
  getLogs: (serviceId: string, params?: { limit?: number; offset?: number }) =>
    client
      .get<HealthLogsPage>(`/metrics/${serviceId}/logs`, {
        params: { limit: params?.limit ?? 100, offset: params?.offset ?? 0 },
      })
      .then((r) => r.data),

  getMetrics: (serviceId: string, rangeHours = 24, resolutionMinutes = 5) =>
    client
      .get<MetricsResponse>(`/metrics/${serviceId}`, {
        params: { range_hours: rangeHours, resolution_minutes: resolutionMinutes },
      })
      .then((r) => r.data),

  getUptimeBars: (days = 45, serviceId?: string) =>
    client
      .get<{ dates: string[]; services: Record<string, (number | null)[]> }>('/metrics/uptime-bars', {
        params: { days, ...(serviceId ? { service_id: serviceId } : {}) },
      })
      .then((r) => r.data),

  getLastIncidents: () =>
    client
      .get<Record<string, LastIncident>>('/metrics/last-incidents')
      .then((r) => r.data),

  getIncidents: (serviceId: string, limit = 20) =>
    client
      .get<Incident[]>(`/metrics/${serviceId}/incidents`, { params: { limit } })
      .then((r) => r.data),

  getReliability: (serviceId: string) =>
    client
      .get<ReliabilityMetrics>(`/metrics/${serviceId}/reliability`)
      .then((r) => r.data),
}
