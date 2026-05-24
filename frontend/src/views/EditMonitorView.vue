<template>
  <div class="monitor-form-page">

    <div class="form-header">
      <button class="back-btn" @click="router.back()">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="13" height="13">
          <path d="M19 12H5M12 19l-7-7 7-7" stroke-linecap="round" stroke-linejoin="round" />
        </svg>
        Back
      </button>
      <h1 class="page-title">Edit Monitor</h1>
    </div>

    <div v-if="loading" class="loading-state">Loading…</div>

    <form v-else @submit.prevent="submit">

      <!-- Card 1: Endpoint & Notifications -->
      <div class="form-card">

        <div class="form-group">
          <label for="edit-url">URL</label>
          <InputText id="edit-url" v-model="form.url" required placeholder="https://api.example.com/health" class="w-full" />
        </div>

        <div class="card-divider" />

        <div class="form-group no-mb">
          <label>Notifications</label>
          <div class="notification-list">
            <label class="check-label">
              <input type="checkbox" v-model="emailEnabled" class="check-input" />
              Email
              <span class="channel-hint">{{ auth.user?.email }}</span>
            </label>
            <label v-for="intg in integrations" :key="intg.id" class="check-label">
              <input type="checkbox" :value="intg.id" v-model="form.integration_ids" class="check-input" />
              {{ intg.type.charAt(0) + intg.type.slice(1).toLowerCase() }}
            </label>
            <p v-if="integrations.length === 0" class="no-channels">
              No channels configured —
              <router-link to="/app/integrations" class="link">add one in Integrations</router-link>
            </p>
          </div>
        </div>

      </div>

      <!-- Card 2: Monitoring Settings -->
      <div class="form-card">

        <div class="form-group">
          <label>Check Interval</label>
          <p class="field-desc">Your monitor will be checked {{ intervalDescription }}.</p>
          <StepSlider v-model="form.check_interval_seconds" :steps="INTERVAL_STEPS" />
          <p class="field-hint">Intervals below 5 min require a Pro plan.</p>
        </div>

        <div class="card-divider" />

        <div class="advanced-toggle" @click="showAdvanced = !showAdvanced">
          <svg :class="['chevron', { open: showAdvanced }]" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14">
            <path d="M9 18l6-6-6-6" stroke-linecap="round" stroke-linejoin="round" />
          </svg>
          Advanced Settings
        </div>

        <div class="advanced-wrap" :class="{ open: showAdvanced }">
          <div class="advanced-body">
            <div class="advanced-inner">

              <div class="form-group no-mb">
                <label>Request Timeout</label>
                <p class="field-desc">The request timeout is {{ timeoutLabel }}. The shorter the timeout the earlier we mark website as down.</p>
                <Slider v-model="form.timeout_seconds" :min="1" :max="60" :step="1" class="w-full" />
                <div class="guide-labels">
                  <span v-for="g in TIMEOUT_GUIDES" :key="g.value" class="guide-label" :style="guideStyle(g.value)">{{ g.label }}</span>
                </div>
              </div>

              <div class="inner-divider" />

              <div class="form-group no-mb">
                <label>Slow Response Time Alert</label>
                <p class="field-desc">You'll receive a notification if the response time exceeds your set threshold. Once it drops back below the threshold, you'll be notified again, and the incident will be marked as resolved.</p>
                <div class="latency-row">
                  <input v-model.number="form.latency_threshold_ms" type="number" class="p-inputtext latency-input" />
                  <span class="input-unit">ms</span>
                </div>
              </div>

              <div class="inner-divider" />

              <div class="form-group no-mb">
                <label>Up HTTP Status Codes</label>
                <p class="field-desc">We will create incident when we receive HTTP status code other than defined below.</p>
                <div class="status-chip-input">
                  <span v-for="(code, i) in form.accepted_status_codes" :key="i" class="status-chip">
                    {{ code }}
                    <button type="button" class="chip-remove" @click="form.accepted_status_codes.splice(i, 1)">×</button>
                  </span>
                  <input
                    v-model="statusCodeInput"
                    class="chip-text-input"
                    placeholder="Add code…"
                    @keydown.enter.prevent="addStatusCode"
                    @blur="addStatusCode"
                  />
                </div>
              </div>

              <div class="inner-divider" />

              <div class="form-group no-mb">
                <label>IP Version</label>
                <div class="half-width">
                  <Select v-model="form.ip_version" :options="ipVersionOptions" option-label="label" option-value="value" class="w-full" />
                </div>
                <p class="field-hint">Default uses IPv4 first, then IPv6 only if IPv4 isn't available.</p>
              </div>

            </div><!-- /advanced-inner -->
          </div>
        </div>

      </div>

      <div class="form-actions">
        <Button type="button" label="Cancel" severity="secondary" outlined @click="router.back()" />
        <Button type="submit" :label="submitting ? 'Saving…' : 'Save Changes'" :loading="submitting" />
      </div>

    </form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useToast } from 'primevue/usetoast'
import { monitorsApi } from '@/api/monitors'
import { integrationsApi } from '@/api/integrations'
import type { Integration } from '@/types'
import InputText from 'primevue/inputtext'
import Slider from 'primevue/slider'
import Select from 'primevue/select'
import Button from 'primevue/button'
import StepSlider from '@/components/common/StepSlider.vue'

const INTERVAL_STEPS = (() => {
  const steps: Array<{ label: string; value: number }> = []
  const labeledMinutes = new Set([5, 15, 30])
  for (let m = 5; m <= 59; m++) {
    steps.push({ label: labeledMinutes.has(m) ? `${m}m` : '', value: m * 60 })
  }
  const labeledHours = new Set([1, 12, 24])
  for (let h = 1; h <= 24; h++) {
    steps.push({ label: labeledHours.has(h) ? `${h}h` : '', value: h * 3600 })
  }
  return steps
})()

const TIMEOUT_GUIDES = [
  { label: '1s',  value: 1 },
  { label: '15s', value: 15 },
  { label: '30s', value: 30 },
  { label: '45s', value: 45 },
  { label: '60s', value: 60 },
]

const ipVersionOptions = [
  { label: 'Default (IPv4 → IPv6)', value: 'DEFAULT' },
  { label: 'IPv4 only', value: 'IPV4' },
  { label: 'IPv6 only', value: 'IPV6' },
]

const props = defineProps<{ id: string }>()
const router = useRouter()
const auth = useAuthStore()
const toast = useToast()
const submitting = ref(false)
const loading = ref(true)
const emailEnabled = ref(true)
const showAdvanced = ref(false)
const integrations = ref<Integration[]>([])

const statusCodeInput = ref('')

const form = reactive({
  url: '',
  notify_email: null as string | null,
  check_interval_seconds: 300,
  timeout_seconds: 30,
  accepted_status_codes: ['2xx', '3xx'] as string[],
  is_active: true,
  latency_threshold_ms: 5000,
  ip_version: 'DEFAULT',
  integration_ids: [] as number[],
})

const intervalDescription = computed(() => {
  const v = form.check_interval_seconds
  if (v < 60) return `every ${v} seconds`
  if (v === 60) return 'every minute'
  if (v < 3600) return `every ${v / 60} minutes`
  if (v === 3600) return 'every hour'
  return `every ${v / 3600} hours`
})

const timeoutLabel = computed(() =>
  form.timeout_seconds === 1 ? '1 second' : `${form.timeout_seconds} seconds`
)

function guideStyle(val: number): Record<string, string> {
  const pct = (val - 1) / 59 * 100
  if (val === 1) return { left: '0%' }
  if (val === 60) return { right: '0%', left: 'auto' }
  return { left: `${pct}%`, transform: 'translateX(-50%)' }
}

watch(emailEnabled, (enabled) => {
  form.notify_email = enabled ? (auth.user?.email ?? '') : ''
})

onMounted(async () => {
  const [svc, intgList] = await Promise.all([
    monitorsApi.get(props.id),
    integrationsApi.list(),
  ])
  integrations.value = intgList
  form.url = svc.url
  emailEnabled.value = !!svc.notify_email
  form.notify_email = svc.notify_email ?? null
  form.check_interval_seconds = Math.max(svc.check_interval_seconds, 300)
  form.timeout_seconds = svc.timeout_seconds
  form.accepted_status_codes = svc.expected_status_code === 200 ? ['2xx', '3xx'] : [String(svc.expected_status_code)]
  form.is_active = svc.is_active
  form.latency_threshold_ms = svc.latency_threshold_ms
  form.ip_version = svc.ip_version ?? 'DEFAULT'
  form.integration_ids = [...(svc.integration_ids ?? [])]
  loading.value = false
})

function addStatusCode() {
  const val = statusCodeInput.value.trim()
  if (val && !form.accepted_status_codes.includes(val)) {
    form.accepted_status_codes.push(val)
  }
  statusCodeInput.value = ''
}

function resolveExpectedStatusCode(codes: string[]): number {
  for (const c of codes) {
    if (/^\d+$/.test(c)) return parseInt(c)
  }
  return 200
}

async function submit() {
  submitting.value = true
  try {
    const { useMonitorsStore } = await import('@/stores/monitors')
    const store = useMonitorsStore()
    const { accepted_status_codes, ...formRest } = form
    await store.update(props.id, {
      ...formRest,
      notify_email: emailEnabled.value ? (auth.user?.email ?? '') : '',
      expected_status_code: resolveExpectedStatusCode(accepted_status_codes),
    })
    toast.add({ severity: 'success', summary: 'Monitor updated', detail: 'Changes saved successfully', life: 3000 })
    router.back()
  } catch (e: unknown) {
    const msg = e instanceof Error ? e.message : 'Failed to save changes'
    toast.add({ severity: 'error', summary: 'Save failed', detail: msg, life: 6000 })
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.monitor-form-page {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.form-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 28px;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: transparent;
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  color: var(--text-muted);
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  cursor: pointer;
  transition: color 0.15s, border-color 0.15s;
  font-family: var(--font-body);
  flex-shrink: 0;
}
.back-btn:hover { color: var(--text-primary); border-color: var(--text-muted); }

.page-title { font-size: 22px; font-weight: 700; letter-spacing: -0.02em; }

.loading-state { color: var(--text-muted); font-size: 14px; padding: 40px 0; }

.form-card {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 32px;
  margin-bottom: 16px;
}

.card-divider {
  border: none;
  border-top: 1px solid var(--border);
  margin: 0 0 28px;
}

.w-full { width: 100%; }

.form-group { margin-bottom: 28px; }
.form-group.no-mb { margin-bottom: 0; }

.form-group label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
  font-size: 13px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: var(--text-muted);
}

.field-desc {
  font-size: 14px;
  color: var(--text-secondary);
  margin: 0 0 16px;
  font-weight: 400;
  text-transform: none;
  letter-spacing: 0;
  line-height: 1.5;
}

.field-hint {
  font-size: 13px;
  color: var(--text-muted);
  margin: 6px 0 0;
  font-weight: 400;
  text-transform: none;
  letter-spacing: 0;
}

.guide-labels {
  position: relative;
  height: 16px;
  margin-top: 10px;
}

.guide-label {
  position: absolute;
  top: 0;
  font-size: 12px;
  color: var(--text-muted);
  font-family: var(--font-mono);
  white-space: nowrap;
  line-height: 1;
}

/* Notifications — horizontal wrap */
.notification-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px 28px;
}

.check-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: var(--text-secondary);
}
.check-label:hover { color: var(--text-primary); }

.check-input {
  width: 15px;
  height: 15px;
  accent-color: var(--accent);
  cursor: pointer;
  flex-shrink: 0;
}

.channel-hint {
  font-size: 12px;
  font-weight: 400;
  text-transform: none;
  letter-spacing: 0;
  color: var(--text-muted);
  font-family: monospace;
}

.no-channels {
  font-size: 13px;
  color: var(--text-muted);
  margin: 0;
  text-transform: none;
  letter-spacing: 0;
  font-weight: 400;
}
.link { color: var(--accent); text-decoration: none; }
.link:hover { text-decoration: underline; }

.inner-divider {
  border: none;
  border-top: 1px solid var(--border);
  margin: 28px 0;
}

.latency-row {
  display: flex;
  align-items: center;
  gap: 8px;
  max-width: 50%;
}
.latency-input { flex: 1; }
.input-unit {
  font-size: 12px;
  color: var(--text-muted);
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.06em;
}

.status-chip-input {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 6px;
  padding: 6px 10px;
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  min-height: 38px;
  cursor: text;
  max-width: 50%;
}
.status-chip {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 2px 8px;
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid var(--border);
  border-radius: 4px;
  font-size: 13px;
  font-family: var(--font-mono);
  font-weight: 600;
  color: var(--text-primary);
  letter-spacing: 0.04em;
}
.chip-remove {
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;
  color: var(--text-muted);
  font-size: 14px;
  line-height: 1;
  display: flex;
  align-items: center;
  font-family: var(--font-body);
  transition: color 0.15s;
}
.chip-remove:hover { color: var(--text-primary); }
.chip-text-input {
  border: none;
  outline: none;
  background: transparent;
  font-size: 14px;
  color: var(--text-primary);
  font-family: var(--font-body);
  min-width: 80px;
  flex: 1;
  padding: 0;
}
.chip-text-input::placeholder { color: var(--text-muted); }

.half-width { max-width: 50%; }

/* Advanced Settings */
.advanced-toggle {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: var(--text-secondary);
  cursor: pointer;
  user-select: none;
  transition: color 0.15s;
}
.advanced-toggle:hover { color: var(--text-primary); }
.chevron { transition: transform 0.2s; flex-shrink: 0; }
.chevron.open { transform: rotate(90deg); }

.advanced-wrap {
  display: grid;
  grid-template-rows: 0fr;
  transition: grid-template-rows 0.3s ease;
}
.advanced-wrap.open { grid-template-rows: 1fr; }
.advanced-body { overflow: hidden; }
.advanced-inner { padding-top: 28px; }

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 8px;
}
</style>
