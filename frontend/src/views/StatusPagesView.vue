<template>
  <div class="sp-view">
    <div class="page-header">
      <div>
        <h1 class="page-title">Status Pages</h1>
      </div>
      <Button label="Create status page" icon="pi pi-plus" @click="openCreate" />
    </div>

    <div class="table-wrap">
      <table class="sp-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>URL</th>
            <th>Access</th>
            <th>Status</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <!-- Skeleton -->
          <template v-if="loading">
            <tr v-for="n in 3" :key="n" class="skel-row">
              <td><div class="skel" style="width:140px;height:14px" /></td>
              <td><div class="skel" style="width:220px;height:14px" /></td>
              <td><div class="skel" style="width:52px;height:20px;border-radius:20px" /></td>
              <td><div class="skel" style="width:52px;height:20px;border-radius:20px" /></td>
              <td></td>
            </tr>
          </template>

          <!-- Error -->
          <tr v-else-if="loadError">
            <td colspan="5" class="empty-cell">
              <div class="empty-state">Failed to load status pages — refresh to try again.</div>
            </td>
          </tr>

          <!-- Empty -->
          <tr v-else-if="pages.length === 0">
            <td colspan="5" class="empty-cell">
              <div class="empty-state">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" width="36" height="36" class="empty-icon">
                  <circle cx="12" cy="12" r="10" stroke-linecap="round" stroke-linejoin="round" />
                  <path d="M2 12h20M12 2a15.3 15.3 0 010 20M12 2a15.3 15.3 0 000 20" stroke-linecap="round" stroke-linejoin="round" />
                </svg>
                <p class="empty-title">No status pages yet</p>
                <p class="empty-desc">Create one to share a public status URL with your users.</p>
                <Button label="Create status page" @click="openCreate" />
              </div>
            </td>
          </tr>

          <!-- Rows -->
          <tr v-else v-for="page in pages" :key="page.id">
            <td>
              <span class="sp-name">{{ page.name }}</span>
              <span v-if="page.description" class="sp-desc">{{ page.description }}</span>
            </td>
            <td>
              <div class="sp-url-wrap">
                <span class="sp-url">{{ origin }}/status/{{ page.slug }}</span>
                <button class="copy-btn" :title="copiedSlug === page.slug ? 'Copied!' : 'Copy link'" @click="copyLink(page.slug)">
                  <i :class="copiedSlug === page.slug ? 'pi pi-check' : 'pi pi-copy'" />
                </button>
              </div>
            </td>
            <td><span class="badge badge-public">Public</span></td>
            <td><span class="badge badge-active">Active</span></td>
            <td class="actions-cell">
              <Button
                icon="pi pi-ellipsis-v"
                text
                rounded
                size="small"
                @click="openMenu($event, page)"
              />
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <Menu ref="menuRef" :model="menuItems" popup />

    <!-- Create / Edit dialog -->
    <Dialog
      v-model:visible="showModal"
      modal
      :draggable="false"
      :header="editTarget ? 'Edit Status Page' : 'New Status Page'"
      :style="{ width: '520px' }"
      :pt="{ root: { style: 'max-width:92vw' } }"
      @hide="closeModal"
    >
      <form @submit.prevent="save">
        <div class="form-group">
          <label>Name</label>
          <InputText v-model="form.name" placeholder="My App Status" class="w-full" @input="onNameInput" />
        </div>

        <div class="form-group">
          <label>Slug <span class="label-hint">used in the public URL</span></label>
          <div class="slug-wrap">
            <span class="slug-prefix">/status/</span>
            <input v-model="form.slug" placeholder="my-app" class="slug-input" @input="form.slugTouched = true" />
          </div>
        </div>

        <div class="form-group">
          <label>Description <span class="label-hint">optional</span></label>
          <textarea
            v-model="form.description"
            placeholder="Current status of our services"
            class="desc-textarea"
            rows="2"
          />
        </div>

        <div class="form-group">
          <label>Monitors <span class="label-hint">shown on the page</span></label>
          <div v-if="monitorsStore.services.length === 0" class="no-monitors">
            No monitors found. Add a monitor first.
          </div>
          <div v-else class="monitors-list">
            <label
              v-for="svc in monitorsStore.services"
              :key="svc.id"
              class="monitor-item"
            >
              <input
                type="checkbox"
                :value="svc.id"
                v-model="form.serviceIds"
                class="check-input"
              />
              <span class="monitor-name">{{ svc.name }}</span>
              <span class="monitor-url">{{ svc.url }}</span>
            </label>
          </div>
        </div>

        <div class="form-actions">
          <Button type="button" label="Cancel" severity="secondary" outlined @click="closeModal" />
          <Button
            type="submit"
            :label="saving ? 'Saving…' : (editTarget ? 'Save changes' : 'Create')"
            :loading="saving"
            :disabled="!canSave"
          />
        </div>
      </form>
    </Dialog>

    <!-- Delete confirm -->
    <Dialog
      v-model:visible="showDeleteDialog"
      modal
      :draggable="false"
      header="Delete Status Page"
      :style="{ width: '380px' }"
      :pt="{ root: { style: 'max-width:92vw' } }"
      @hide="deleteTarget = null"
    >
      <p class="delete-text">
        Delete <strong>{{ deleteTarget?.name }}</strong>? The public URL will stop working immediately.
        This cannot be undone.
      </p>
      <template #footer>
        <Button label="Cancel" severity="secondary" outlined @click="showDeleteDialog = false; deleteTarget = null" />
        <Button label="Delete" severity="danger" :loading="deleting" @click="doDelete" />
      </template>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useToast } from 'primevue/usetoast'
import { statusPagesApi } from '@/api/statusPages'
import { useMonitorsStore } from '@/stores/monitors'
import type { StatusPage } from '@/types'
import Button from 'primevue/button'
import Dialog from 'primevue/dialog'
import InputText from 'primevue/inputtext'
import Menu from 'primevue/menu'

const toast = useToast()
const monitorsStore = useMonitorsStore()

const pages = ref<StatusPage[]>([])
const loading = ref(true)
const loadError = ref(false)
const showModal = ref(false)
const saving = ref(false)
const editTarget = ref<StatusPage | null>(null)
const showDeleteDialog = ref(false)
const deleteTarget = ref<StatusPage | null>(null)
const deleting = ref(false)
const copiedSlug = ref<string | null>(null)
const origin = window.location.origin
const menuRef = ref()
const activePage = ref<StatusPage | null>(null)

const form = reactive({
  name: '',
  slug: '',
  description: '',
  serviceIds: [] as string[],
  slugTouched: false,
})

function slugify(val: string) {
  return val.toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/^-|-$/g, '')
}

function onNameInput() {
  if (!form.slugTouched) {
    form.slug = slugify(form.name)
  }
}

function resetForm() {
  form.name = ''
  form.slug = ''
  form.description = ''
  form.serviceIds = []
  form.slugTouched = false
}

const canSave = computed(() => form.name.trim().length > 0 && form.slug.trim().length > 0)

async function load() {
  loading.value = true
  loadError.value = false
  try {
    pages.value = await statusPagesApi.list()
  } catch {
    loadError.value = true
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editTarget.value = null
  resetForm()
  showModal.value = true
}

async function openEdit(page: StatusPage) {
  editTarget.value = page
  form.name = page.name
  form.slug = page.slug
  form.description = page.description ?? ''
  form.serviceIds = []
  form.slugTouched = true
  showModal.value = true
  const detail = await statusPagesApi.get(page.id)
  form.serviceIds = detail.service_ids
}

function closeModal() {
  showModal.value = false
  editTarget.value = null
}

async function save() {
  if (!canSave.value) return
  saving.value = true
  try {
    if (editTarget.value) {
      await statusPagesApi.update(editTarget.value.id, {
        name: form.name,
        slug: form.slug,
        description: form.description || undefined,
        service_ids: form.serviceIds,
      })
      toast.add({ severity: 'success', summary: 'Status page updated', life: 4000 })
    } else {
      await statusPagesApi.create({
        name: form.name,
        slug: form.slug,
        description: form.description || undefined,
        service_ids: form.serviceIds,
      })
      toast.add({ severity: 'success', summary: 'Status page created', life: 4000 })
    }
    closeModal()
    await load()
  } catch (e: any) {
    const data = e?.response?.data
    const msg = (typeof data === 'object' && data !== null ? (data.error ?? data.message) : null) ?? 'Failed to save'
    const is402 = e?.response?.status === 402
    toast.add({ severity: is402 ? 'warn' : 'error', summary: is402 ? 'Plan limit reached' : 'Failed', detail: msg, life: 6000 })
  } finally {
    saving.value = false
  }
}

function confirmDelete(page: StatusPage) {
  deleteTarget.value = page
  showDeleteDialog.value = true
}

async function doDelete() {
  if (!deleteTarget.value) return
  deleting.value = true
  try {
    await statusPagesApi.delete(deleteTarget.value.id)
    showDeleteDialog.value = false
    deleteTarget.value = null
    await load()
  } finally {
    deleting.value = false
  }
}

async function copyLink(slug: string) {
  await navigator.clipboard.writeText(`${window.location.origin}/status/${slug}`)
  copiedSlug.value = slug
  setTimeout(() => { copiedSlug.value = null }, 2000)
}

function formatDate(iso: string) {
  return new Date(iso).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
}

const menuItems = computed(() => [
  {
    label: 'Edit',
    icon: 'pi pi-pencil',
    command: () => activePage.value && openEdit(activePage.value),
  },
  { separator: true },
  {
    label: 'Delete',
    icon: 'pi pi-trash',
    class: 'menu-item-danger',
    command: () => activePage.value && confirmDelete(activePage.value),
  },
])

function openMenu(event: Event, page: StatusPage) {
  activePage.value = page
  menuRef.value.toggle(event)
}

onMounted(async () => {
  await Promise.all([load(), monitorsStore.fetchAll()])
})
</script>

<style scoped>
.sp-view { width: 100%; }

.page-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}
.page-title { font-size: 30px; font-weight: 700; color: var(--text-primary); margin: 0 0 4px; }

/* Table */
.table-wrap {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  overflow-x: auto;
}

.sp-table {
  width: 100%;
  border-collapse: collapse;
}

.sp-table th {
  padding: 11px 16px;
  font-size: 11px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: var(--text-muted);
  border-bottom: 1px solid var(--border);
  text-align: left;
  white-space: nowrap;
}

.sp-table td {
  padding: 14px 16px;
  font-size: 14px;
  color: var(--text-secondary);
  border-bottom: 1px solid var(--border);
  vertical-align: middle;
}

.sp-table tbody tr:hover td { background: var(--bg-hover); }
.sp-table tbody tr:has(.empty-cell):hover td { background: transparent; }

.sp-name {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
}
.sp-desc {
  display: block;
  font-size: 12px;
  color: var(--text-muted);
  margin-top: 2px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 220px;
}

.sp-url-wrap {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: var(--bg-hover);
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  padding: 4px 10px;
  max-width: 100%;
}
.sp-url {
  font-size: 12px;
  color: var(--text-secondary);
  font-family: var(--font-mono, monospace);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.copy-btn {
  background: none;
  border: none;
  cursor: pointer;
  color: var(--text-muted);
  padding: 0;
  display: flex;
  align-items: center;
  font-size: 12px;
  flex-shrink: 0;
  transition: color 0.15s;
}
.copy-btn:hover { color: var(--accent); }

.badge {
  display: inline-block;
  padding: 3px 10px;
  border-radius: 20px;
  font-size: 11px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  white-space: nowrap;
}
.badge-public { background: rgba(148,163,184,0.1); color: var(--text-muted); }
.badge-active { background: rgba(34,197,94,0.1); color: var(--success); }

.actions-cell { width: 48px; text-align: right; }

/* Skeleton */
.skel {
  border-radius: var(--radius-sm);
  background: var(--bg-hover);
  animation: shimmer 1.4s ease-in-out infinite;
  display: inline-block;
}
@keyframes shimmer { 0%, 100% { opacity: 1; } 50% { opacity: 0.4; } }

/* Empty state */
.empty-cell { padding: 0 !important; border: none !important; vertical-align: middle; }
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 24px;
  text-align: center;
}
.empty-icon { color: var(--text-muted); opacity: 0.4; }
.empty-title { font-size: 16px; font-weight: 700; color: var(--text-primary); margin: 0; }
.empty-desc { font-size: 14px; color: var(--text-muted); margin: 0; max-width: 300px; line-height: 1.6; }

/* Form */
.w-full { width: 100%; }

.form-group { margin-bottom: 20px; }
.form-group label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-secondary);
}
.label-hint { font-size: 12px; font-weight: 400; color: var(--text-muted); }

.slug-wrap { display: flex; align-items: center; border: 1px solid var(--border); border-radius: var(--radius-sm); overflow: hidden; }
.slug-prefix {
  padding: 0 10px;
  height: 40px;
  display: flex;
  align-items: center;
  font-size: 13px;
  color: var(--text-muted);
  background: var(--bg-hover);
  border-right: 1px solid var(--border);
  white-space: nowrap;
  flex-shrink: 0;
}
.slug-input {
  flex: 1;
  background: none;
  border: none;
  outline: none;
  padding: 0 12px;
  height: 40px;
  font-size: 14px;
  font-family: var(--font-body);
  color: var(--text-primary);
}

.desc-textarea {
  width: 100%;
  resize: vertical;
  background: var(--bg-input, var(--bg-hover));
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  padding: 10px 12px;
  font-size: 14px;
  font-family: var(--font-body);
  color: var(--text-primary);
  outline: none;
  transition: border-color 0.15s;
  box-sizing: border-box;
}
.desc-textarea:focus { border-color: var(--accent); }
.desc-textarea::placeholder { color: var(--text-muted); }

.no-monitors { font-size: 14px; color: var(--text-muted); padding: 8px 0; }

.monitors-list {
  display: flex;
  flex-direction: column;
  gap: 2px;
  max-height: 200px;
  overflow-y: auto;
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  padding: 6px;
}
.monitor-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 10px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.1s;
}
.monitor-item:hover { background: var(--bg-hover); }
.check-input {
  width: 15px;
  height: 15px;
  accent-color: var(--accent);
  cursor: pointer;
  flex-shrink: 0;
}
.monitor-name { font-size: 14px; font-weight: 600; color: var(--text-primary); flex-shrink: 0; }
.monitor-url {
  font-size: 12px;
  color: var(--text-muted);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 28px;
}

.delete-text { font-size: 15px; color: var(--text-secondary); margin: 0; line-height: 1.5; }
</style>

<style>
.menu-item-danger .p-menuitem-link .p-menuitem-text,
.menu-item-danger .p-menuitem-link .p-menuitem-icon {
  color: var(--danger) !important;
}
</style>
