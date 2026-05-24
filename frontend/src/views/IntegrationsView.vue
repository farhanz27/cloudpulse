<template>
  <div class="integrations-view">
    <div class="page-header">
      <div>
        <h1 class="page-title">Integrations</h1>
      </div>
    </div>

    <div class="intg-list">
      <div v-for="t in resolvedTypes" :key="t.value" class="intg-card">
        <div class="row-icon" :class="`type-${t.value.toLowerCase()}`">
          <component :is="t.icon" />
        </div>

        <div class="row-info">
          <span class="row-name">{{ t.label }}</span>
          <span class="row-desc">{{ t.description }}</span>
        </div>

        <template v-if="loading">
          <div class="skel row-skel-btn" />
        </template>

        <template v-else-if="t.integration">
          <span class="connected-badge">
            <span class="connected-dot" />Connected
          </span>
          <Button
            :icon="testingId === t.integration.id ? 'pi pi-spin pi-spinner' : 'pi pi-ellipsis-v'"
            text
            rounded
            size="small"
            :disabled="testingId === t.integration.id"
            class="menu-btn"
            @click="openMenu($event, t.integration!)"
          />
        </template>

        <template v-else>
          <Button
            v-if="isProOnly(t.value)"
            label="Upgrade Plan"
            outlined
            @click="router.push('/settings?tab=subscription')"
          />
          <Button
            v-else
            label="Connect"
            @click="openCreate(t.value)"
          />
        </template>
      </div>
    </div>

    <Menu ref="menuRef" :model="menuItems" popup />

    <!-- Create / Edit modal -->
    <Dialog
      v-model:visible="showModal"
      modal
      :draggable="false"
      :header="editTarget ? 'Edit Integration' : 'Add Integration'"
      :style="{ width: '520px' }"
      :pt="{ root: { style: 'max-width:92vw' } }"
      @hide="closeModal"
    >
      <form @submit.prevent="save">
        <div v-if="!editTarget && !typeIsPreset" class="form-group">
          <label>Type</label>
          <div class="type-selector">
            <button
              v-for="t in integrationTypes"
              :key="t.value"
              type="button"
              :class="['type-btn', { active: form.type === t.value }]"
              @click="form.type = t.value"
            >
              <span class="type-btn-icon"><component :is="t.icon" /></span>
              {{ t.label }}
            </button>
          </div>
        </div>

        <!-- Telegram subscribe flow (create only) -->
        <template v-if="form.type === 'TELEGRAM' && !editTarget">
          <div v-if="tgStep === 'loading'" class="tg-status">
            <i class="pi pi-spin pi-spinner tg-spinner" />
            <span>Generating link…</span>
          </div>

          <div v-else-if="tgStep === 'waiting'" class="tg-subscribe">
            <p class="tg-instruction">
              Open Telegram and send the message below to
              <strong>@{{ tgBotUsername }}</strong>.
              For a group, invite the bot first, then send it.
            </p>
            <div class="tg-code-box">
              <code class="tg-code">/start {{ tgCode }}</code>
              <button type="button" class="tg-copy-btn" @click="copyCode" :title="copied ? 'Copied!' : 'Copy'">
                <i :class="copied ? 'pi pi-check' : 'pi pi-copy'" />
              </button>
            </div>
            <p class="field-hint">Waiting for confirmation… This link expires in {{ tgExpiresMin }} minutes.</p>
          </div>

          <div v-else-if="tgStep === 'linked'" class="tg-status tg-linked">
            <i class="pi pi-check-circle tg-check" />
            <span>Telegram connected! Closing…</span>
          </div>

          <div v-else-if="tgStep === 'error'" class="tg-status tg-error">
            <i class="pi pi-exclamation-triangle" />
            <span>{{ tgError }}</span>
            <Button type="button" label="Retry" size="small" outlined @click="startTelegramLink" />
          </div>
        </template>

        <!-- Discord / Webhook / Slack / Teams fields -->
        <template v-if="['DISCORD', 'WEBHOOK', 'SLACK', 'TEAMS'].includes(form.type)">
          <div class="form-group">
            <label>Webhook URL</label>
            <InputText
              v-model="form.config.url"
              :placeholder="urlPlaceholder"
              class="w-full"
            />
            <p v-if="form.type === 'TEAMS'" class="field-hint">
              In Teams, go to a channel → Manage channel → Edit → Workflows → Post to a channel when a webhook request is received.
            </p>
            <p v-if="form.type === 'SLACK'" class="field-hint">
              In Slack, go to your workspace settings → Manage apps → Incoming Webhooks → Add to Slack.
            </p>
          </div>
        </template>

        <template v-if="form.type !== 'TELEGRAM' || editTarget">
          <div class="form-group">
            <label>Notify on</label>
            <div class="events-group">
              <label class="check-label">
                <input type="checkbox" value="DOWNTIME" v-model="form.events" class="check-input" />
                Down
              </label>
              <label class="check-label">
                <input type="checkbox" value="RECOVERY" v-model="form.events" class="check-input" />
                Recovery
              </label>
              <label class="check-label">
                <input type="checkbox" value="HIGH_LATENCY" v-model="form.events" class="check-input" />
                High latency
              </label>
            </div>
          </div>

          <div class="form-group">
            <label>Custom message <span class="label-optional">Optional</span></label>
            <InputText v-model="form.custom_text" placeholder="e.g. @oncall-team or #incident" class="w-full" />
          </div>

          <div class="form-actions">
            <Button type="button" label="Cancel" severity="secondary" outlined @click="closeModal" />
            <Button
              type="submit"
              :label="saving ? 'Saving…' : (editTarget ? 'Save changes' : 'Connect')"
              :loading="saving"
              :disabled="!canSave"
            />
          </div>
        </template>

        <template v-else>
          <div class="form-actions">
            <Button type="button" label="Cancel" severity="secondary" outlined @click="closeModal" />
          </div>
        </template>
      </form>
    </Dialog>

    <!-- Delete confirm -->
    <Dialog
      v-model:visible="showDeleteDialog"
      modal
      :draggable="false"
      header="Remove Integration"
      :style="{ width: '380px' }"
      :pt="{ root: { style: 'max-width:92vw' } }"
      @hide="deleteTarget = null"
    >
      <p class="delete-text">Remove <strong>{{ deleteTarget ? deleteTarget.type.charAt(0) + deleteTarget.type.slice(1).toLowerCase() : '' }}</strong>? This cannot be undone.</p>
      <template #footer>
        <Button label="Cancel" severity="secondary" outlined @click="showDeleteDialog = false; deleteTarget = null" />
        <Button label="Remove" severity="danger" :loading="deleting" @click="doDelete" />
      </template>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, defineComponent, h, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'primevue/usetoast'
import { integrationsApi, telegramApi } from '@/api/integrations'
import { usageApi } from '@/api/usage'
import type { Integration } from '@/types'
import Button from 'primevue/button'
import Dialog from 'primevue/dialog'
import InputText from 'primevue/inputtext'
import Menu from 'primevue/menu'

const toast = useToast()
const router = useRouter()

const FREE_TYPES = ['TELEGRAM', 'DISCORD']
const plan = ref<string>('free')

function isProOnly(type: string): boolean {
  return plan.value.toLowerCase() === 'free' && !FREE_TYPES.includes(type)
}

const integrations = ref<Integration[]>([])
const loading = ref(true)
const showModal = ref(false)
const saving = ref(false)
const editTarget = ref<Integration | null>(null)
const deleteTarget = ref<Integration | null>(null)
const showDeleteDialog = ref(false)
const deleting = ref(false)
const testingId = ref<number | null>(null)
const typeIsPreset = ref(false)
const menuRef = ref()
const activeIntegration = ref<Integration | null>(null)

// Telegram subscribe flow
type TgStep = 'loading' | 'waiting' | 'linked' | 'error'
const tgStep = ref<TgStep>('loading')
const tgCode = ref('')
const tgBotUsername = ref('')
const tgExpiresMin = ref(15)
const tgError = ref('')
const copied = ref(false)
let tgPollTimer: ReturnType<typeof setInterval> | null = null

function stopTgPoll() {
  if (tgPollTimer) { clearInterval(tgPollTimer); tgPollTimer = null }
}

async function startTelegramLink() {
  tgStep.value = 'loading'
  tgError.value = ''
  stopTgPoll()
  try {
    const data = await telegramApi.startLink()
    tgCode.value = data.code
    tgBotUsername.value = data.bot_username.replace(/^@/, '')
    tgExpiresMin.value = data.expires_in_minutes
    tgStep.value = 'waiting'
    tgPollTimer = setInterval(() => pollTelegramLink(data.code), 3000)
  } catch (err: any) {
    tgStep.value = 'error'
    const data = err?.response?.data
    const serverMsg = typeof data === 'object' && data !== null
      ? (data.error ?? data.message)
      : null
    tgError.value = serverMsg
      ?? (err?.response?.status === 503 ? 'Telegram is not configured on this server.' : null)
      ?? 'Could not reach the server.'
  }
}

async function pollTelegramLink(code: string) {
  try {
    const data = await telegramApi.linkStatus(code)
    if (data.linked) {
      stopTgPoll()
      tgStep.value = 'linked'
      await load()
      setTimeout(() => closeModal(), 1500)
    }
  } catch {
    stopTgPoll()
    tgStep.value = 'error'
    tgError.value = 'Link expired or an error occurred.'
  }
}

async function copyCode() {
  await navigator.clipboard.writeText(`/start ${tgCode.value}`)
  copied.value = true
  setTimeout(() => { copied.value = false }, 2000)
}

onUnmounted(stopTgPoll)

const ALL_EVENTS = ['DOWNTIME', 'RECOVERY', 'HIGH_LATENCY']

const form = reactive({
  type: 'TELEGRAM',
  config: {} as Record<string, string>,
  events: [...ALL_EVENTS] as string[],
  custom_text: '',
})

function resetForm() {
  form.type = 'TELEGRAM'
  form.config = {}
  form.events = [...ALL_EVENTS]
  form.custom_text = ''
}

const TelegramIcon = defineComponent({ render: () => h('svg', { viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.75', 'stroke-linecap': 'round', 'stroke-linejoin': 'round', width: 20, height: 20 }, [h('path', { d: 'M21 3L2 10l7 3 2 7 4-5 5 5L21 3z' })]) })
const DiscordIcon = defineComponent({ render: () => h('svg', { viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.75', 'stroke-linecap': 'round', 'stroke-linejoin': 'round', width: 20, height: 20 }, [h('path', { d: 'M20.317 4.37a19.791 19.791 0 00-4.885-1.515.074.074 0 00-.079.037c-.21.375-.444.864-.608 1.25a18.27 18.27 0 00-5.487 0 12.64 12.64 0 00-.617-1.25.077.077 0 00-.079-.037A19.736 19.736 0 003.677 4.37a.07.07 0 00-.032.027C.533 9.046-.32 13.58.099 18.057c.002.022.015.043.03.056a19.9 19.9 0 005.993 3.03.078.078 0 00.084-.028c.462-.63.874-1.295 1.226-1.994a.076.076 0 00-.041-.106 13.107 13.107 0 01-1.872-.892.077.077 0 01-.008-.128 10.2 10.2 0 00.372-.292.074.074 0 01.077-.01c3.928 1.793 8.18 1.793 12.062 0a.074.074 0 01.078.01c.12.098.246.198.373.292a.077.077 0 01-.006.127 12.299 12.299 0 01-1.873.892.077.077 0 00-.041.107c.36.698.772 1.362 1.225 1.993a.076.076 0 00.084.028 19.839 19.839 0 006.002-3.03.077.077 0 00.032-.054c.5-5.177-.838-9.674-3.549-13.66a.061.061 0 00-.031-.03z' })]) })
const SlackIcon = defineComponent({ render: () => h('svg', { viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.75', 'stroke-linecap': 'round', 'stroke-linejoin': 'round', width: 20, height: 20 }, [h('path', { d: 'M9 3L6 21' }), h('path', { d: 'M15 3L12 21' }), h('path', { d: 'M3 9h18' }), h('path', { d: 'M3 15h18' })]) })
const TeamsIcon = defineComponent({ render: () => h('svg', { viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.75', 'stroke-linecap': 'round', 'stroke-linejoin': 'round', width: 20, height: 20 }, [h('path', { d: 'M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2' }), h('circle', { cx: '9', cy: '7', r: '4' }), h('path', { d: 'M23 21v-2a4 4 0 00-3-3.87' }), h('path', { d: 'M16 3.13a4 4 0 010 7.75' })]) })
const WebhookIcon = defineComponent({ render: () => h('svg', { viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.75', 'stroke-linecap': 'round', 'stroke-linejoin': 'round', width: 20, height: 20 }, [h('path', { d: 'M10 13a5 5 0 007.54.54l3-3a5 5 0 00-7.07-7.07l-1.72 1.71' }), h('path', { d: 'M14 11a5 5 0 00-7.54-.54l-3 3a5 5 0 007.07 7.07l1.71-1.71' })]) })

const integrationTypes = [
  { value: 'TELEGRAM', label: 'Telegram', icon: TelegramIcon, description: 'Subscribe to the bot for instant Telegram alert notifications.' },
  { value: 'DISCORD', label: 'Discord', icon: DiscordIcon, description: 'Get important monitor status updates in your Discord messages.' },
  { value: 'SLACK', label: 'Slack', icon: SlackIcon, description: 'Post alerts to a Slack channel via an incoming webhook.' },
  { value: 'TEAMS', label: 'Microsoft Teams', icon: TeamsIcon, description: 'Send alerts to a Teams channel via a workflow webhook.' },
  { value: 'WEBHOOK', label: 'Webhook', icon: WebhookIcon, description: 'Send HTTP POST requests to any URL when alerts trigger.' },
]

const configuredByType = computed(() => {
  const map: Record<string, Integration> = {}
  for (const intg of integrations.value) map[intg.type] = intg
  return map
})

const resolvedTypes = computed(() =>
  integrationTypes.map(t => ({ ...t, integration: configuredByType.value[t.value] ?? null }))
)

const urlPlaceholder = computed(() => {
  if (form.type === 'DISCORD') return 'https://discord.com/api/webhooks/…'
  if (form.type === 'SLACK') return 'https://hooks.slack.com/services/…'
  if (form.type === 'TEAMS') return 'https://prod-xx.logic.azure.com:443/workflows/…'
  return 'https://example.com/webhook'
})

const canSave = computed(() => {
  const c = form.config
  if (form.type === 'TELEGRAM') return !!editTarget.value
  if (['DISCORD', 'WEBHOOK', 'SLACK', 'TEAMS'].includes(form.type)) return !!c.url
  return false
})

const menuItems = computed(() => [
  {
    label: 'Test',
    icon: 'pi pi-send',
    command: () => activeIntegration.value && testIntegration(activeIntegration.value),
  },
  {
    label: 'Edit',
    icon: 'pi pi-pencil',
    command: () => activeIntegration.value && openEdit(activeIntegration.value),
  },
  {
    label: 'Reconnect',
    icon: 'pi pi-refresh',
    visible: activeIntegration.value?.type === 'TELEGRAM',
    command: () => activeIntegration.value && openCreate(activeIntegration.value.type),
  },
  { separator: true },
  {
    label: 'Remove',
    icon: 'pi pi-trash',
    class: 'menu-item-danger',
    command: () => activeIntegration.value && confirmDelete(activeIntegration.value),
  },
])

function openMenu(event: Event, intg: Integration) {
  activeIntegration.value = intg
  menuRef.value.toggle(event)
}

async function load() {
  loading.value = true
  try {
    const [intgs, usage] = await Promise.all([integrationsApi.list(), usageApi.get()])
    integrations.value = intgs
    plan.value = usage.plan
  } finally {
    loading.value = false
  }
}

function openCreate(type?: string) {
  editTarget.value = null
  resetForm()
  if (type) {
    form.type = type
    typeIsPreset.value = true
  } else {
    typeIsPreset.value = false
  }
  showModal.value = true
  if (form.type === 'TELEGRAM') {
    startTelegramLink()
  }
}

function openEdit(intg: Integration) {
  editTarget.value = intg
  form.type = intg.type
  form.config = Object.fromEntries(
    Object.entries(intg.config).map(([k, v]) => [k, String(v)])
  )
  form.events = intg.config.events
    ? String(intg.config.events).split(',').filter(Boolean)
    : [...ALL_EVENTS]
  form.custom_text = String(intg.config.custom_text || '')
  typeIsPreset.value = false
  showModal.value = true
}

function closeModal() {
  showModal.value = false
  editTarget.value = null
  typeIsPreset.value = false
  stopTgPoll()
}

async function save() {
  if (!canSave.value) return
  saving.value = true
  const typeName = integrationTypes.find(t => t.value === form.type)?.label ?? form.type
  try {
    const config = {
      ...form.config,
      events: form.events.join(','),
      ...(form.custom_text ? { custom_text: form.custom_text } : {}),
    }
    if (editTarget.value) {
      await integrationsApi.update(editTarget.value.id, { name: typeName, config, enabled: true })
    } else {
      const existing = configuredByType.value[form.type]
      if (existing) {
        await integrationsApi.update(existing.id, { name: typeName, config, enabled: true })
      } else {
        await integrationsApi.create({ type: form.type, name: typeName, config, enabled: true })
      }
    }
    toast.add({ severity: 'success', summary: editTarget.value ? 'Integration updated' : 'Integration connected', detail: `${typeName} saved successfully`, life: 4000 })
    closeModal()
    await load()
  } catch (e: any) {
    const data = e?.response?.data
    const msg = (typeof data === 'object' && data !== null ? (data.error ?? data.message) : null) ?? (e instanceof Error ? e.message : 'Failed to save')
    const is402 = e?.response?.status === 402
    toast.add({ severity: is402 ? 'warn' : 'error', summary: is402 ? 'Plan limit reached' : 'Failed to save', detail: msg, life: 6000 })
  } finally {
    saving.value = false
  }
}

async function testIntegration(intg: Integration) {
  testingId.value = intg.id
  try {
    await integrationsApi.test(intg.id)
    toast.add({ severity: 'success', summary: 'Test passed', detail: 'Message sent successfully', life: 4000 })
  } catch (e: unknown) {
    const axiosDetail = (e as { response?: { data?: { detail?: string } } })?.response?.data?.detail
    const msg = axiosDetail || (e instanceof Error ? e.message : 'Test failed')
    toast.add({ severity: 'error', summary: 'Test failed', detail: msg, life: 6000 })
  } finally {
    testingId.value = null
  }
}

function confirmDelete(intg: Integration) {
  deleteTarget.value = intg
  showDeleteDialog.value = true
}

async function doDelete() {
  if (!deleteTarget.value) return
  deleting.value = true
  try {
    await integrationsApi.delete(deleteTarget.value.id)
    showDeleteDialog.value = false
    deleteTarget.value = null
    await load()
  } finally {
    deleting.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.integrations-view { width: 100%; }

.page-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 28px;
  flex-wrap: wrap;
}
.page-title { font-size: 30px; font-weight: 700; color: var(--text-primary); margin: 0 0 4px; }

/* List */
.intg-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.intg-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 20px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  transition: border-color 0.15s;
}
.intg-card:hover { border-color: var(--border-strong); }

.row-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  color: #fff;
}
.type-telegram { background: #229ED9; }
.type-discord  { background: #5865F2; }
.type-slack    { background: #4A154B; }
.type-teams    { background: #6264A7; }
.type-email    { background: #0ea5e9; }
.type-webhook  { background: #64748b; }

.row-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
  min-width: 0;
}
.row-name {
  font-size: 15px;
  font-weight: 700;
  color: var(--text-primary);
}
.row-desc {
  font-size: 13px;
  color: var(--text-muted);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.connected-badge {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  font-size: 11px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: var(--success);
  background: rgba(34, 197, 94, 0.1);
  padding: 3px 8px;
  border-radius: 20px;
  flex-shrink: 0;
}
.connected-dot {
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background: var(--success);
  flex-shrink: 0;
}

.menu-btn { flex-shrink: 0; }

.row-skel-btn {
  width: 76px;
  height: 30px;
  border-radius: var(--radius-sm);
  background: var(--bg-hover);
  animation: shimmer 1.4s ease-in-out infinite;
  flex-shrink: 0;
}
@keyframes shimmer {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.4; }
}

.w-full { width: 100%; }

/* Modal form */
.form-group { margin-bottom: 28px; }
.form-group label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
}
.label-optional {
  font-size: 12px;
  font-weight: 500;
  text-transform: none;
  letter-spacing: 0;
  color: var(--text-muted);
}
.field-hint { font-size: 13px; color: var(--text-muted); margin: 6px 0 0; line-height: 1.5; }

.type-selector { display: flex; gap: 8px; flex-wrap: wrap; }
.type-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 14px;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border);
  background: transparent;
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 600;
  font-family: var(--font-body);
  cursor: pointer;
  transition: border-color 0.15s, color 0.15s, background 0.15s;
}
.type-btn.active { border-color: var(--accent); color: var(--accent); background: rgba(212,175,55,0.08); }
.type-btn:hover:not(.active) { border-color: var(--border-strong); color: var(--text-primary); }
.type-btn-icon { display: flex; align-items: center; }

.events-group { display: flex; gap: 20px; flex-wrap: wrap; }
.check-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  color: var(--text-secondary);
  cursor: pointer;
  user-select: none;
}
.check-label:hover { color: var(--text-primary); }
.check-input {
  width: 15px;
  height: 15px;
  accent-color: var(--accent);
  cursor: pointer;
  flex-shrink: 0;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 32px;
}

.delete-text { font-size: 15px; color: var(--text-secondary); margin: 0; line-height: 1.5; }

/* Telegram subscribe flow */
.tg-status {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 32px 0;
  color: var(--text-muted);
  font-size: 15px;
}
.tg-spinner { font-size: 24px; color: var(--accent); }
.tg-linked { color: var(--success); }
.tg-linked .tg-check { font-size: 32px; }
.tg-error { color: var(--danger); }

.tg-subscribe { display: flex; flex-direction: column; gap: 16px; margin-bottom: 8px; }
.tg-instruction { font-size: 15px; color: var(--text-secondary); line-height: 1.6; margin: 0; }

.tg-code-box {
  display: flex;
  align-items: center;
  gap: 10px;
  background: var(--bg-hover);
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  padding: 12px 14px;
}
.tg-code {
  flex: 1;
  font-family: var(--font-mono, monospace);
  font-size: 15px;
  color: var(--text-primary);
  word-break: break-all;
  background: none;
}
.tg-copy-btn {
  flex-shrink: 0;
  background: none;
  border: none;
  cursor: pointer;
  color: var(--text-muted);
  padding: 4px;
  display: flex;
  align-items: center;
  transition: color 0.15s;
}
.tg-copy-btn:hover { color: var(--accent); }
</style>

<style>
.menu-item-danger .p-menuitem-link .p-menuitem-text,
.menu-item-danger .p-menuitem-link .p-menuitem-icon {
  color: var(--danger) !important;
}
</style>
