import client from './client'

export interface QuotaItem {
  used: number
  limit: number
}

export interface UsageResponse {
  plan: string
  monitors: QuotaItem
  status_pages: QuotaItem
  integrations: QuotaItem
  min_check_interval_seconds: number
}

export const usageApi = {
  get: () => client.get<UsageResponse>('/usage').then((r) => r.data),
}
