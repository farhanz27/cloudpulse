export interface Service {
  id: string
  name: string
  url: string
  notify_email: string | null
  check_interval_seconds: number
  timeout_seconds: number
  expected_status_code: number
  is_active: boolean
  keep_alive: boolean
  latency_threshold_ms: number
  muted_until: string | null
  consecutive_failures: number
  region: string | null
  integration_ids: number[]
  created_at: string
  updated_at: string
}

export interface ServiceWithStatus extends Service {
  current_status: string | null
  last_response_time_ms: number | null
  uptime_percent: number | null
  last_checked_at: string | null
  ssl_valid_until?: string | null
  domain_valid_until?: string | null
}

export interface ServiceCreate {
  name: string
  url: string
  notify_email?: string | null
  check_interval_seconds?: number
  timeout_seconds?: number
  expected_status_code?: number
  is_active?: boolean
  keep_alive?: boolean
  latency_threshold_ms?: number
  integration_ids?: number[]
}

export interface ServiceUpdate {
  name?: string
  url?: string
  notify_email?: string | null
  check_interval_seconds?: number
  timeout_seconds?: number
  expected_status_code?: number
  is_active?: boolean
  keep_alive?: boolean
  latency_threshold_ms?: number
  integration_ids?: number[]
}

export interface HealthLog {
  id: number
  service_id: string
  status: string
  response_time_ms: number | null
  status_code: number | null
  error_message: string | null
  checked_at: string
}

export interface HealthLogsPage {
  items: HealthLog[]
  total: number
}

export interface MetricPoint {
  timestamp: string
  avg_response_time_ms: number | null
  min_response_time_ms: number | null
  max_response_time_ms: number | null
  uptime_percent: number
  total_checks: number
}

export interface MetricsResponse {
  service_id: string
  range_hours: number
  resolution_minutes: number
  data: MetricPoint[]
}

export interface Alert {
  id: number
  service_id: string
  alert_type: string
  message: string
  notified: boolean
  acknowledged: boolean
  created_at: string
  service_name: string | null
}

// ── Incidents ─────────────────────────────────────────────────────────────────
export interface IncidentSummary {
  id: number
  service_id: string
  service_name: string
  started_at: string
  recovered_at: string | null
  duration_seconds: number
  status: 'open' | 'resolved'
}

export interface IncidentsPage {
  total: number
  items: IncidentSummary[]
}

// ── Status Pages ──────────────────────────────────────────────────────────────
export interface StatusPage {
  id: string
  name: string
  slug: string
  description: string | null
  created_at: string
  service_count: number
}

export interface StatusPageDetail {
  id: string
  name: string
  slug: string
  description: string | null
  created_at: string
  service_ids: string[]
}

export interface PublicServiceStatus {
  id: string
  name: string
  url: string
  current_status: string | null
  uptime_percent: number | null
  uptime_bars: (number | null)[]
  uptime_dates: string[]
}

export interface StatusPagePublic {
  id: string
  name: string
  slug: string
  description: string | null
  overall_status: 'OPERATIONAL' | 'DEGRADED' | 'OUTAGE' | 'UNKNOWN'
  services: PublicServiceStatus[]
}

// ── Integrations ──────────────────────────────────────────────────────────────
export interface Integration {
  id: number
  type: string
  name: string
  config: Record<string, string | number | boolean>
  enabled: boolean
  created_at: string
}

export interface LastIncident {
  last_down_at: string
  duration_seconds: number | null
}

export interface Incident {
  started_at: string
  recovered_at: string | null
  duration_seconds: number
}

export interface ReliabilityMetrics {
  mttr_seconds: number | null
  total_incidents: number
  incidents_last_30d: number
}
