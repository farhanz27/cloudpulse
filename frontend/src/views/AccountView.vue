<template>
  <div class="account-page">
    <div class="page-header">
      <h1 class="page-title">Account</h1>
      <p class="page-sub">Manage your profile and preferences</p>
    </div>

    <div class="sections">
      <!-- Profile -->
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
            <span v-if="profileSaved" class="save-confirm">Changes saved</span>
            <Button type="submit" label="Save changes" />
          </div>
        </form>
      </section>

      <!-- Change password -->
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
            <span v-if="pwSaved" class="save-confirm">Password updated</span>
            <Button type="submit" label="Update password" />
          </div>
        </form>
      </section>

      <!-- Danger zone -->
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
  </div>

  <!-- Delete account confirmation dialog -->
  <Dialog v-model:visible="deleteDialogOpen" modal header="Delete Account" :style="{ width: '420px' }" :closable="!deleting">
    <p class="confirm-text">This will permanently delete your account. Type <strong>DELETE</strong> to confirm.</p>
    <InputText v-model="deleteConfirmInput" placeholder="Type DELETE" class="w-full confirm-input" :disabled="deleting" @keydown.enter="confirmDelete" />
    <span v-if="deleteError" class="field-error">{{ deleteError }}</span>
    <template #footer>
      <Button label="Cancel" text @click="closeDeleteDialog" :disabled="deleting" />
      <Button label="Delete account" severity="danger" :loading="deleting" :disabled="deleteConfirmInput !== 'DELETE'" @click="confirmDelete" />
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { authApi } from '@/api/auth'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import Dialog from 'primevue/dialog'

const router = useRouter()
const auth = useAuthStore()

const profile = reactive({
  name: auth.user?.name ?? '',
  email: auth.user?.email ?? '',
})
const pErrors = reactive({ name: '', email: '' })
const profileSaved = ref(false)

async function saveProfile() {
  pErrors.name = ''
  pErrors.email = ''
  let ok = true
  if (!profile.name.trim()) { pErrors.name = 'Name is required'; ok = false }
  if (!profile.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(profile.email)) { pErrors.email = 'Valid email required'; ok = false }
  if (!ok) return
  try {
    await auth.updateProfile(profile.name.trim(), profile.email)
    profileSaved.value = true
    setTimeout(() => { profileSaved.value = false }, 3000)
  } catch (err: any) {
    const msg = err?.response?.data?.detail
    pErrors.email = typeof msg === 'string' ? msg : 'Failed to save changes'
  }
}

const pw = reactive({ current: '', next: '', confirm: '' })
const pwErrors = reactive({ current: '', next: '', confirm: '' })
const pwSaved = ref(false)

function savePassword() {
  pwErrors.current = ''
  pwErrors.next = ''
  pwErrors.confirm = ''
  let ok = true
  if (!pw.current) { pwErrors.current = 'Required'; ok = false }
  if (!pw.next || pw.next.length < 8) { pwErrors.next = 'Min. 8 characters'; ok = false }
  if (pw.next !== pw.confirm) { pwErrors.confirm = 'Passwords do not match'; ok = false }
  if (!ok) return
  pw.current = ''
  pw.next = ''
  pw.confirm = ''
  pwSaved.value = true
  setTimeout(() => { pwSaved.value = false }, 3000)
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
</script>

<style scoped>
.w-full { width: 100%; }

.account-page {
  max-width: 760px;
}

.page-header {
  margin-bottom: 32px;
}
.page-title {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 4px;
}
.page-sub {
  font-size: 14px;
  color: var(--text-muted);
}

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
}

.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.field-error {
  font-size: 12px;
  color: var(--danger);
}

.form-footer {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 12px;
}

.save-confirm {
  font-size: 13px;
  color: var(--success);
  font-weight: 500;
}

/* Danger zone */
.danger-card {
  border-color: rgba(239, 68, 68, 0.2);
}
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
  margin-bottom: 16px;
  line-height: 1.5;
}
.confirm-text strong { color: var(--text-primary); }
.confirm-input { margin-bottom: 8px; }

@media (max-width: 600px) {
  .field-row { grid-template-columns: 1fr; }
  .danger-row { flex-direction: column; align-items: flex-start; }
}
</style>
