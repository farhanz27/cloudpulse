<template>
  <div class="settings-page">
    <div class="page-header">
      <h1 class="page-title">Settings</h1>
    </div>

    <div class="settings-tabs">
      <button
        v-for="tab in tabs"
        :key="tab.id"
        class="tab-btn"
        :class="{ 'tab-btn--active': activeTab === tab.id }"
        @click="activeTab = tab.id"
      >{{ tab.label }}</button>
    </div>

    <!-- Profile -->
    <div v-if="activeTab === 'profile'" class="sections">
      <section class="section card">
        <h2 class="section-title">Profile</h2>

        <form class="profile-form" @submit.prevent="saveProfile" novalidate>
          <div class="field-row">
            <div class="field">
              <label for="pname" class="field-label">Full Name</label>
              <InputText id="pname" v-model="profile.name" type="text" autocomplete="name" class="w-full" />
              <span v-if="pErrors.name" class="field-error">{{ pErrors.name }}</span>
            </div>
            <div class="field">
              <label for="pemail" class="field-label">Email Address</label>
              <InputText id="pemail" v-model="profile.email" type="email" autocomplete="email" class="w-full" />
              <span v-if="pErrors.email" class="field-error">{{ pErrors.email }}</span>
            </div>
          </div>
          <div class="form-footer">
            <Button type="submit" label="Save changes" />
          </div>
        </form>
      </section>

      <section class="section card">
        <h2 class="section-title">Change Password</h2>
        <p class="section-desc">Password updates will take effect on next sign-in.</p>

        <form class="profile-form" @submit.prevent="savePassword" novalidate>
          <div class="field">
            <label for="cur-pw" class="field-label">Current Password</label>
            <InputText id="cur-pw" v-model="pw.current" type="password" autocomplete="current-password" class="w-full" />
            <span v-if="pwErrors.current" class="field-error">{{ pwErrors.current }}</span>
          </div>
          <div class="field-row">
            <div class="field">
              <label for="new-pw" class="field-label">New Password</label>
              <InputText id="new-pw" v-model="pw.next" type="password" autocomplete="new-password" class="w-full" />
              <span v-if="pwErrors.next" class="field-error">{{ pwErrors.next }}</span>
            </div>
            <div class="field">
              <label for="conf-pw" class="field-label">Confirm Password</label>
              <InputText id="conf-pw" v-model="pw.confirm" type="password" autocomplete="new-password" class="w-full" />
              <span v-if="pwErrors.confirm" class="field-error">{{ pwErrors.confirm }}</span>
            </div>
          </div>
          <div class="form-footer">
            <Button type="submit" label="Update password" />
          </div>
        </form>
      </section>

      <section class="section card danger-card">
        <h2 class="section-title danger-title">Danger Zone</h2>
        <div class="danger-row">
          <div>
            <div class="danger-label">Delete account</div>
            <div class="danger-hint">Permanently delete your account. This cannot be undone.</div>
          </div>
          <Button label="Delete account" severity="danger" @click="deleteDialogOpen = true" />
        </div>
      </section>
    </div>

    <!-- Notifications -->
    <div v-else-if="activeTab === 'notifications'" class="sections">
      <section class="section card">
        <h2 class="section-title">Notifications</h2>
        <p class="section-desc">Global notification preferences.</p>

        <div class="toggle-row">
          <div>
            <div class="toggle-label">Email alerts</div>
            <div class="toggle-hint">Receive email when a monitor goes down or recovers.</div>
          </div>
          <ToggleSwitch v-model="notifs.emailAlerts" />
        </div>

        <div class="toggle-row">
          <div>
            <div class="toggle-label">Recovery notifications</div>
            <div class="toggle-hint">Also notify when a monitor comes back up.</div>
          </div>
          <ToggleSwitch v-model="notifs.recoveryNotifications" />
        </div>

        <div class="form-footer">
          <Button label="Save preferences" @click="saveNotifications" />
        </div>
      </section>
    </div>

    <!-- Plan -->
    <div v-else-if="activeTab === 'subscription'" class="sections">
      <div class="plan-cards">
        <div class="plan-card plan-card--active">
          <div class="plan-card-header">
            <span class="plan-name">Free</span>
            <span class="plan-current-badge">Current plan</span>
          </div>
          <p class="plan-price">RM0 <span class="plan-price-period">/ month</span></p>
          <p class="plan-desc">Everything you need to get started with uptime monitoring.</p>
          <ul class="plan-features">
            <li>Up to 5 monitors</li>
            <li>1 status page</li>
            <li>2 integrations</li>
            <li>5-minute checks</li>
            <li>30-day history</li>
          </ul>
          <button class="plan-btn plan-btn--current" disabled>Current plan</button>
        </div>

        <div class="plan-card plan-card--pro">
          <div class="plan-card-header">
            <span class="plan-name plan-name--pro">Pro</span>
            <span class="plan-soon-badge">Coming soon</span>
          </div>
          <p class="plan-price plan-price--pro">RM20 <span class="plan-price-period">/ month</span></p>
          <p class="plan-desc">For teams that need more power and fewer limits.</p>
          <ul class="plan-features">
            <li>Up to 50 monitors</li>
            <li>Up to 10 status pages</li>
            <li>All notification channels</li>
            <li>1-minute check intervals</li>
            <li>12-month history</li>
            <li>SSL certificate monitoring</li>
            <li>Priority support</li>
          </ul>
          <button class="plan-btn plan-btn--upgrade" disabled>Upgrade to Pro</button>
        </div>
      </div>

      <section class="section card">
        <h2 class="section-title">Usage</h2>

        <div v-if="usageLoading" class="usage-loading">Loading usage…</div>
        <div v-else-if="usageError" class="usage-loading">Failed to load usage — refresh to try again.</div>

        <div v-else class="usage-list">
          <div class="usage-item">
            <div class="usage-header">
              <span class="usage-label">Monitors</span>
              <span class="usage-count">
                {{ usage.monitors.used }} / {{ usage.monitors.limit }}
              </span>
            </div>
            <div class="usage-bar">
              <div class="usage-fill" :style="{ width: pct(usage.monitors) + '%' }"></div>
            </div>
          </div>

          <div class="usage-item">
            <div class="usage-header">
              <span class="usage-label">Status Pages</span>
              <span class="usage-count">
                {{ usage.status_pages.used }} / {{ usage.status_pages.limit }}
              </span>
            </div>
            <div class="usage-bar">
              <div class="usage-fill" :style="{ width: pct(usage.status_pages) + '%' }"></div>
            </div>
          </div>

          <div class="usage-item">
            <div class="usage-header">
              <span class="usage-label">Integrations</span>
              <span class="usage-count">
                {{ usage.integrations.used }} / {{ usage.integrations.limit }}
              </span>
            </div>
            <div class="usage-bar">
              <div class="usage-fill" :style="{ width: pct(usage.integrations) + '%' }"></div>
            </div>
          </div>
        </div>
      </section>
    </div>

    <!-- App -->
    <div v-else-if="activeTab === 'preferences'" class="sections">
      <section class="section card">
        <h2 class="section-title">Monitoring Defaults</h2>
        <p class="section-desc">Applied when adding a new monitor.</p>

        <div class="field-row">
          <div class="field">
            <label class="field-label">Default Check Interval</label>
            <Select v-model="appSettings.defaultInterval" :options="intervalOptions" option-label="label" option-value="value" class="w-full" />
          </div>
          <div class="field">
            <label class="field-label">Default Timeout</label>
            <Select v-model="appSettings.defaultTimeout" :options="timeoutOptions" option-label="label" option-value="value" class="w-full" />
          </div>
        </div>

        <div class="field">
          <label class="field-label">Default Latency Threshold</label>
          <div class="input-suffix-wrap">
            <InputText v-model="latencyInput" type="number" min="100" class="w-full" />
            <span class="input-suffix">ms</span>
          </div>
        </div>

        <div class="form-footer">
          <Button label="Save defaults" @click="saveDefaults" />
        </div>
      </section>

      <section class="section card danger-card">
        <h2 class="section-title danger-title">Data</h2>
        <div class="danger-row">
          <div>
            <div class="danger-label">Clear all health logs</div>
            <div class="danger-hint">Permanently delete all historical check data. Monitors are not affected.</div>
          </div>
          <Button label="Clear logs" severity="danger" outlined @click="clearLogsDialogOpen = true" />
        </div>
      </section>
    </div>
  </div>

  <Dialog v-model:visible="deleteDialogOpen" modal header="Delete Account" :style="{ width: '420px' }" :closable="!deleting">
    <p class="confirm-text">This will permanently delete your account. Type <strong>DELETE</strong> to confirm.</p>
    <InputText v-model="deleteConfirmInput" placeholder="Type DELETE" class="w-full confirm-input" :disabled="deleting" @keydown.enter="confirmDelete" />
    <span v-if="deleteError" class="field-error">{{ deleteError }}</span>
    <template #footer>
      <Button label="Cancel" text @click="closeDeleteDialog" :disabled="deleting" />
      <Button label="Delete account" severity="danger" :loading="deleting" :disabled="deleteConfirmInput !== 'DELETE'" @click="confirmDelete" />
    </template>
  </Dialog>

  <Dialog v-model:visible="clearLogsDialogOpen" modal header="Clear Health Logs" :style="{ width: '400px' }">
    <p class="confirm-text">This will permanently delete all health log history. This cannot be undone.</p>
    <template #footer>
      <Button label="Cancel" text @click="clearLogsDialogOpen = false" />
      <Button label="Clear all logs" severity="danger" @click="confirmClearLogs" />
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { authApi } from '@/api/auth'
import { monitorsApi } from '@/api/monitors'
import { usageApi, type UsageResponse, type QuotaItem } from '@/api/usage'
import { useToast } from 'primevue/usetoast'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import Select from 'primevue/select'
import ToggleSwitch from 'primevue/toggleswitch'
import Dialog from 'primevue/dialog'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()
const toast = useToast()

const tabs = [
  { id: 'profile', label: 'Profile' },
  { id: 'notifications', label: 'Notifications' },
  { id: 'preferences', label: 'Preferences' },
  { id: 'subscription', label: 'Subscription' },
]

const activeTab = ref((route.query.tab as string) || 'profile')

watch(activeTab, (tab) => {
  router.replace({ query: { ...route.query, tab } })
})

watch(() => route.query.tab, (tab) => {
  activeTab.value = (tab as string) || 'profile'
})

// ── Profile ──
const profile = reactive({
  name: auth.user?.name ?? '',
  email: auth.user?.email ?? '',
})
const pErrors = reactive({ name: '', email: '' })

async function saveProfile() {
  pErrors.name = ''
  pErrors.email = ''
  let ok = true
  if (!profile.name.trim()) { pErrors.name = 'Name is required'; ok = false }
  if (!profile.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(profile.email)) { pErrors.email = 'Valid email required'; ok = false }
  if (!ok) return
  try {
    await auth.updateProfile(profile.name.trim(), profile.email)
    toast.add({ severity: 'success', summary: 'Profile updated', detail: 'Changes saved successfully', life: 3000 })
  } catch (err: any) {
    const msg = err?.response?.data?.message
    toast.add({ severity: 'error', summary: 'Save failed', detail: typeof msg === 'string' ? msg : 'Failed to save changes', life: 6000 })
  }
}

const pw = reactive({ current: '', next: '', confirm: '' })
const pwErrors = reactive({ current: '', next: '', confirm: '' })

async function savePassword() {
  pwErrors.current = ''
  pwErrors.next = ''
  pwErrors.confirm = ''
  let ok = true
  if (!pw.current) { pwErrors.current = 'Required'; ok = false }
  if (!pw.next || pw.next.length < 8) { pwErrors.next = 'Min. 8 characters'; ok = false }
  if (pw.next !== pw.confirm) { pwErrors.confirm = 'Passwords do not match'; ok = false }
  if (!ok) return
  try {
    await authApi.changePassword(pw.current, pw.next)
    pw.current = ''
    pw.next = ''
    pw.confirm = ''
    toast.add({ severity: 'success', summary: 'Password updated', life: 3000 })
  } catch (err: any) {
    const msg = err?.response?.data?.message
    if (typeof msg === 'string' && msg.toLowerCase().includes('incorrect')) {
      pwErrors.current = 'Current password is incorrect'
    } else {
      toast.add({ severity: 'error', summary: 'Update failed', detail: typeof msg === 'string' ? msg : 'Failed to update password', life: 6000 })
    }
  }
}

const deleteDialogOpen = ref(false)
const deleteConfirmInput = ref('')
const deleteError = ref('')
const deleting = ref(false)

function closeDeleteDialog() {
  deleteDialogOpen.value = false
  deleteConfirmInput.value = ''
  deleteError.value = ''
}

async function confirmDelete() {
  if (deleteConfirmInput.value !== 'DELETE') return
  deleting.value = true
  deleteError.value = ''
  try {
    await authApi.deleteAccount()
    auth.signOut()
    router.push({ name: 'sign-in' })
  } catch {
    deleteError.value = 'Failed to delete account. Please try again.'
    deleting.value = false
  }
}

// ── Notifications ──
const notifs = reactive({
  emailAlerts: true,
  recoveryNotifications: true,
})

async function saveNotifications() {
  try {
    await authApi.updatePreferences({
      emailAlerts: notifs.emailAlerts,
      recoveryNotifications: notifs.recoveryNotifications,
    })
    toast.add({ severity: 'success', summary: 'Preferences saved', life: 3000 })
  } catch {
    toast.add({ severity: 'error', summary: 'Save failed', detail: 'Failed to save notification preferences', life: 6000 })
  }
}

// ── Plan / Usage ──
const usageLoading = ref(false)
const usageError = ref(false)
const usageLoaded = ref(false)
const usage = ref<UsageResponse>({
  plan: 'free',
  monitors: { used: 0, limit: 5 },
  status_pages: { used: 0, limit: 1 },
  integrations: { used: 0, limit: 2 },
  min_check_interval_seconds: 60,
})

function pct(q: QuotaItem): number {
  return Math.min(100, Math.round((q.used / q.limit) * 100))
}

watch(activeTab, async (tab) => {
  if (tab === 'subscription' && !usageLoaded.value && !usageLoading.value) {
    await loadUsage()
  }
})

async function loadUsage() {
  usageLoading.value = true
  usageError.value = false
  try {
    usage.value = await usageApi.get()
    usageLoaded.value = true
  } catch {
    usageError.value = true
  } finally {
    usageLoading.value = false
  }
}

// ── App settings ──
const appSettings = reactive({
  defaultInterval: 60,
  defaultTimeout: 10,
  defaultLatencyThreshold: 5000,
})

const intervalOptions = [
  { label: '1 minute',  value: 60  },
  { label: '2 minutes', value: 120 },
  { label: '5 minutes', value: 300 },
  { label: '10 minutes', value: 600 },
]

const timeoutOptions = [
  { label: '5 seconds',  value: 5  },
  { label: '10 seconds', value: 10 },
  { label: '20 seconds', value: 20 },
  { label: '30 seconds', value: 30 },
]

const latencyInput = computed({
  get: () => String(appSettings.defaultLatencyThreshold),
  set: (v: string) => { appSettings.defaultLatencyThreshold = parseInt(v) || 5000 },
})

const clearLogsDialogOpen = ref(false)

async function saveDefaults() {
  try {
    await authApi.updatePreferences({
      defaultIntervalSeconds: appSettings.defaultInterval,
      defaultTimeoutSeconds: appSettings.defaultTimeout,
      defaultLatencyThresholdMs: appSettings.defaultLatencyThreshold,
    })
    toast.add({ severity: 'success', summary: 'Defaults saved', life: 3000 })
  } catch {
    toast.add({ severity: 'error', summary: 'Save failed', detail: 'Failed to save monitoring defaults', life: 6000 })
  }
}

async function confirmClearLogs() {
  clearLogsDialogOpen.value = false
  try {
    await monitorsApi.clearAllHealthLogs()
    toast.add({ severity: 'success', summary: 'Logs cleared', detail: 'All health log history has been deleted', life: 4000 })
  } catch {
    toast.add({ severity: 'error', summary: 'Clear failed', detail: 'Failed to clear health logs', life: 6000 })
  }
}

async function loadPreferences() {
  try {
    const p = await authApi.getPreferences()
    notifs.emailAlerts = p.emailAlerts
    notifs.recoveryNotifications = p.recoveryNotifications
    appSettings.defaultInterval = p.defaultIntervalSeconds
    appSettings.defaultTimeout = p.defaultTimeoutSeconds
    appSettings.defaultLatencyThreshold = p.defaultLatencyThresholdMs
  } catch {
    // leave defaults in place
  }
}

onMounted(() => {
  loadPreferences()
  if (activeTab.value === 'subscription') loadUsage()
})
</script>

<style scoped>
.w-full { width: 100%; }

.settings-page { width: 100%; }

.page-header { margin-bottom: 24px; }
.page-title {
  font-size: 30px;
  font-weight: 700;
  letter-spacing: -0.02em;
  color: var(--text-primary);
}

/* ── Tabs ── */
.settings-tabs {
  display: flex;
  gap: 2px;
  border-bottom: 1px solid var(--border);
  margin-bottom: 28px;
}

.tab-btn {
  padding: 9px 16px;
  font-size: 13px;
  font-weight: 600;
  font-family: var(--font-body);
  color: var(--text-muted);
  background: transparent;
  border: none;
  border-bottom: 2px solid transparent;
  margin-bottom: -1px;
  cursor: pointer;
  transition: color 0.15s, border-color 0.15s;
}
.tab-btn:hover { color: var(--text-primary); }
.tab-btn--active {
  color: var(--accent);
  border-bottom-color: var(--accent);
}

/* ── Sections ── */
.sections {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.section-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 20px;
}
.section-desc {
  font-size: 13px;
  color: var(--text-muted);
  margin-bottom: 20px;
  margin-top: -12px;
}

.profile-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.field-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-bottom: 20px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-bottom: 20px;
}
.field:last-of-type { margin-bottom: 0; }

.field-label {
  font-size: 13px;
  font-weight: 500;
  color: var(--text-secondary);
}

.field-error {
  font-size: 12px;
  color: var(--danger);
}

.form-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 24px;
}

/* Toggles */
.toggle-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  padding: 14px 0;
  border-bottom: 1px solid var(--border);
}
.toggle-row:last-of-type { border-bottom: none; }

.toggle-label {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: 3px;
}
.toggle-hint {
  font-size: 13px;
  color: var(--text-muted);
}

/* Danger zone */
.danger-card { border-color: rgba(239, 68, 68, 0.2); }
.danger-title { color: var(--danger); }

.danger-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}
.danger-label {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: 4px;
}
.danger-hint {
  font-size: 13px;
  color: var(--text-muted);
}

.confirm-text {
  font-size: 14px;
  color: var(--text-secondary);
  line-height: 1.5;
  margin-bottom: 16px;
}
.confirm-text strong { color: var(--text-primary); }
.confirm-input { margin-bottom: 8px; }

/* App tab */
.input-suffix-wrap {
  position: relative;
  display: flex;
  align-items: center;
}
.input-suffix-wrap .w-full { padding-right: 40px; }
.input-suffix {
  position: absolute;
  right: 12px;
  font-size: 12px;
  color: var(--text-muted);
  pointer-events: none;
}

/* ── Plan cards ── */
.plan-cards {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.plan-card {
  background: var(--bg-secondary);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 28px;
  display: flex;
  flex-direction: column;
}

.plan-card--pro {
  border-color: rgba(212, 175, 55, 0.35);
  background: linear-gradient(160deg, rgba(212, 175, 55, 0.04) 0%, var(--bg-secondary) 60%);
}

.plan-card-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
}

.plan-name {
  font-size: 18px;
  font-weight: 700;
  color: var(--text-primary);
}
.plan-name--pro { color: var(--accent); }

.plan-current-badge {
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.07em;
  text-transform: uppercase;
  color: var(--text-muted);
  background: var(--bg-hover);
  border: 1px solid var(--border);
  padding: 2px 8px;
  border-radius: 999px;
}

.plan-soon-badge {
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.07em;
  text-transform: uppercase;
  color: var(--accent);
  background: rgba(212, 175, 55, 0.1);
  border: 1px solid rgba(212, 175, 55, 0.25);
  padding: 2px 8px;
  border-radius: 999px;
}

.plan-price {
  font-size: 32px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 10px;
  line-height: 1;
}
.plan-price--pro { color: var(--accent); }
.plan-price-period {
  font-size: 14px;
  font-weight: 400;
  color: var(--text-muted);
}

.plan-desc {
  font-size: 13px;
  color: var(--text-muted);
  line-height: 1.5;
  margin-bottom: 20px;
}

.plan-features {
  list-style: none;
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 28px;
  flex: 1;
}

.plan-features li {
  font-size: 13px;
  color: var(--text-secondary);
  padding-left: 18px;
  position: relative;
}
.plan-features li::before {
  content: '–';
  position: absolute;
  left: 0;
  color: var(--text-muted);
}
.plan-card--pro .plan-features li::before {
  content: '✓';
  color: var(--accent);
  font-size: 11px;
  top: 1px;
}

.plan-btn {
  width: 100%;
  padding: 10px;
  border-radius: var(--radius-sm);
  font-size: 13px;
  font-weight: 600;
  border: none;
  cursor: default;
}
.plan-btn--current {
  background: var(--bg-hover);
  color: var(--text-muted);
  border: 1px solid var(--border);
}
.plan-btn--upgrade {
  background: var(--accent);
  color: #000;
  opacity: 0.45;
}

/* ── Usage ── */
.usage-loading {
  font-size: 13px;
  color: var(--text-muted);
}

.usage-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.usage-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 8px;
}

.usage-label {
  font-size: 13px;
  font-weight: 500;
  color: var(--text-secondary);
}

.usage-count {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-muted);
}

.usage-bar {
  height: 6px;
  background: var(--bg-hover);
  border-radius: 999px;
  overflow: hidden;
}

.usage-fill {
  height: 100%;
  background: var(--accent);
  border-radius: 999px;
  transition: width 0.4s ease;
}

@media (max-width: 640px) {
  .plan-cards { grid-template-columns: 1fr; }
  .field-row { grid-template-columns: 1fr; }
  .danger-row { flex-direction: column; align-items: flex-start; }
}
</style>
