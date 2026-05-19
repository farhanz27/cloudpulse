<template>
  <Dialog
    v-model:visible="isVisible"
    modal
    header="Add Monitor"
    :style="{ width: '520px' }"
    :pt="dialogPt"
  >
    <form @submit.prevent="submit">
      <div class="form-group">
        <label for="name">Monitor Name</label>
        <InputText id="name" v-model="form.name" required placeholder="My API" class="w-full" />
      </div>

      <div class="form-group">
        <label for="url">URL</label>
        <InputText id="url" v-model="form.url" required placeholder="https://api.example.com/health" class="w-full" />
      </div>

      <div class="form-group">
        <label>Notifications</label>
        <div class="notification-types">
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
            <router-link to="/integrations" class="link" @click="isVisible = false">add one in Integrations</router-link>
          </p>
        </div>
      </div>

      <div class="form-group">
        <label>Check Interval</label>
        <div class="segment-group">
          <button
            v-for="(lbl, i) in INTERVAL_LABELS"
            :key="i"
            type="button"
            :class="['segment-btn', { active: intervalIndex === i }]"
            @click="intervalIndex = i"
          >{{ lbl }}</button>
        </div>
      </div>

      <div class="form-group">
        <label>Timeout <span class="field-value">{{ form.timeout_seconds }}s</span></label>
        <Slider v-model="form.timeout_seconds" :min="1" :max="60" :step="1" class="w-full" />
        <div class="slider-bounds">
          <span>1s</span><span>60s</span>
        </div>
      </div>

      <div class="form-actions">
        <Button type="button" label="Cancel" severity="secondary" outlined @click="isVisible = false" />
        <Button type="submit" :label="submitting ? 'Adding…' : 'Add Monitor'" :loading="submitting" />
      </div>
    </form>
  </Dialog>
</template>

<script setup lang="ts">
import { reactive, ref, computed, watch, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { integrationsApi } from '@/api/integrations'
import type { Integration } from '@/types'
import Dialog from 'primevue/dialog'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import Slider from 'primevue/slider'

const INTERVAL_VALUES = [30, 60, 300, 1800, 3600, 86400]
const INTERVAL_LABELS = ['30s', '1m', '5m', '30m', '1h', '24h']

const props = defineProps<{ visible: boolean }>()
const emit = defineEmits<{ close: []; created: [] }>()

const isVisible = computed({
  get: () => props.visible,
  set: () => emit('close'),
})

const dialogPt = { root: { style: 'max-width: 92vw' } }

const auth = useAuthStore()
const submitting = ref(false)
const intervalIndex = ref(1)
const emailEnabled = ref(true)
const integrations = ref<Integration[]>([])

const form = reactive({
  name: '',
  url: '',
  notify_email: auth.user?.email ?? '',
  check_interval_seconds: 60,
  timeout_seconds: 10,
  is_active: true,
  keep_alive: false,
  latency_threshold_ms: 5000,
  integration_ids: [] as number[],
})

watch(intervalIndex, (idx) => {
  form.check_interval_seconds = INTERVAL_VALUES[idx]
})

watch(emailEnabled, (enabled) => {
  form.notify_email = enabled ? (auth.user?.email ?? '') : ''
})

onMounted(async () => {
  integrations.value = await integrationsApi.list()
})

async function submit() {
  submitting.value = true
  try {
    const { useServicesStore } = await import('@/stores/services')
    const store = useServicesStore()
    await store.create({ ...form })
    emit('created')
    emit('close')
    resetForm()
  } finally {
    submitting.value = false
  }
}

function resetForm() {
  form.name = ''
  form.url = ''
  emailEnabled.value = true
  form.notify_email = auth.user?.email ?? ''
  intervalIndex.value = 1
  form.check_interval_seconds = 60
  form.timeout_seconds = 10
  form.is_active = true
  form.keep_alive = false
  form.latency_threshold_ms = 5000
  form.integration_ids = []
}
</script>

<style scoped>
.w-full { width: 100%; }

.form-group {
  margin-bottom: 28px;
}

.form-group label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
}

.notification-types {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.field-value {
  font-weight: 700;
  color: var(--accent);
  text-transform: none;
  letter-spacing: 0;
}

.check-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 13px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: var(--text-secondary);
  margin: 0;
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
  font-size: 11px;
  font-weight: 400;
  text-transform: none;
  letter-spacing: 0;
  color: var(--text-muted);
  font-family: monospace;
}

.channel-badge {
  font-size: 10px;
  font-weight: 700;
  letter-spacing: 0.06em;
  padding: 2px 6px;
  border-radius: 4px;
  text-transform: uppercase;
  color: #fff;
}
.badge-telegram { background: #229ED9; }
.badge-discord  { background: #5865F2; }
.badge-slack    { background: #4A154B; }
.badge-teams    { background: #6264A7; }
.badge-webhook  { background: #64748b; }

.no-channels {
  font-size: 12px;
  color: var(--text-muted);
  margin: 0;
  text-transform: none;
  letter-spacing: 0;
  font-weight: 400;
}
.link { color: var(--accent); text-decoration: none; }
.link:hover { text-decoration: underline; }

/* Segment group */
.segment-group {
  display: flex;
  border-radius: var(--radius-sm);
  overflow: hidden;
  border: 1px solid var(--border);
}

.segment-btn {
  flex: 1;
  padding: 8px 4px;
  font-size: 12px;
  font-weight: 600;
  font-family: var(--font-body);
  background: transparent;
  color: var(--text-muted);
  border: none;
  border-right: 1px solid var(--border);
  cursor: pointer;
  transition: background 0.15s, color 0.15s;
  letter-spacing: 0.04em;
}

.segment-btn:last-child { border-right: none; }
.segment-btn:hover:not(.active) { background: var(--bg-hover); color: var(--text-primary); }
.segment-btn.active { background: var(--accent); color: #0A0A0A; font-weight: 700; }

/* Slider */
.slider-bounds {
  display: flex;
  justify-content: space-between;
  font-size: 11px;
  color: var(--text-muted);
  margin-top: 8px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 32px;
}
</style>
