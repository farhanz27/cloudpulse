<template>
  <template v-if="routerReady">
  <!-- Public pages — no chrome -->
  <router-view v-if="isPublicPage" />

  <div v-else class="app-layout">
    <!-- Mobile header -->
    <header class="mobile-header">
      <button class="hamburger" :class="{ open: sidebarOpen }" @click="sidebarOpen = !sidebarOpen" aria-label="Toggle menu">
        <span></span><span></span><span></span>
      </button>
      <div class="mobile-brand">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="20" height="20" aria-hidden="true" style="color:#D4AF37">
          <polyline points="1,12 6,12 8,6 10,18 12,8 14,15 16,12 23,12" />
        </svg>
        <span>CloudPulse</span>
      </div>
      <router-link v-if="openIncidentCount > 0" to="/incidents" class="mobile-incident-dot">
        {{ openIncidentCount }}
      </router-link>
    </header>

    <!-- Sidebar overlay on mobile -->
    <div v-if="sidebarOpen" class="sidebar-overlay" @click="sidebarOpen = false"></div>

    <!-- Sidebar -->
    <aside :class="['sidebar', { 'sidebar--open': sidebarOpen }]">
      <div class="sidebar-brand">
        <svg class="brand-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
          <polyline points="1,12 6,12 8,6 10,18 12,8 14,15 16,12 23,12" />
        </svg>
        <span class="brand-name">CLOUDPULSE</span>
      </div>

      <nav class="sidebar-nav">
        <router-link to="/dashboard" class="nav-item" exact-active-class="nav-item--active" @click="sidebarOpen = false">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="16" height="16" aria-hidden="true">
            <path d="M2 12h2M20 12h2M12 2v2M12 20v2" /><circle cx="12" cy="12" r="4" />
            <path d="M4.93 4.93l1.41 1.41M17.66 17.66l1.41 1.41M4.93 19.07l1.41-1.41M17.66 6.34l1.41-1.41" />
          </svg>
          <span>Monitoring</span>
        </router-link>

        <router-link to="/incidents" class="nav-item" active-class="nav-item--active" @click="sidebarOpen = false">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="16" height="16" aria-hidden="true">
            <path d="M12 9v4M12 17h.01" />
            <path d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z" />
          </svg>
          <span>Incidents</span>
          <span v-if="openIncidentCount > 0" class="nav-badge">{{ openIncidentCount > 9 ? '9+' : openIncidentCount }}</span>
        </router-link>

        <router-link to="/status-pages" class="nav-item" active-class="nav-item--active" @click="sidebarOpen = false">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="16" height="16" aria-hidden="true">
            <circle cx="12" cy="12" r="10" />
            <path d="M2 12h20M12 2a15.3 15.3 0 010 20M12 2a15.3 15.3 0 000 20" />
          </svg>
          <span>Status Pages</span>
        </router-link>

        <router-link to="/app/integrations" class="nav-item" active-class="nav-item--active" @click="sidebarOpen = false">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="16" height="16" aria-hidden="true">
            <path d="M10 13a5 5 0 007.54.54l3-3a5 5 0 00-7.07-7.07l-1.72 1.71" />
            <path d="M14 11a5 5 0 00-7.54-.54l-3 3a5 5 0 007.07 7.07l1.71-1.71" />
          </svg>
          <span>Integrations</span>
        </router-link>

        <router-link to="/alerts" class="nav-item" active-class="nav-item--active" @click="sidebarOpen = false">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="16" height="16" aria-hidden="true">
            <path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9M13.73 21a2 2 0 01-3.46 0" />
          </svg>
          <span>Event Log</span>
        </router-link>
      </nav>

      <!-- User account -->
      <div class="sidebar-account">
        <button
          class="account-row"
          :class="{ 'account-row--open': profileMenuOpen }"
          @click="profileMenuOpen = !profileMenuOpen"
          @keydown.escape="profileMenuOpen = false"
          aria-haspopup="true"
          :aria-expanded="profileMenuOpen"
        >
          <div class="account-avatar" aria-hidden="true">{{ auth.initials }}</div>
          <div class="account-info">
            <div class="account-name">{{ auth.user?.name }}</div>
            <div class="account-email">{{ auth.user?.email }}</div>
          </div>
          <svg class="account-chevron" :class="{ 'account-chevron--up': profileMenuOpen }" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="14" height="14" aria-hidden="true">
            <path d="M6 9l6 6 6-6" />
          </svg>
        </button>

        <div v-if="profileMenuOpen" class="profile-menu">
          <router-link to="/account" class="profile-menu-item" @click="profileMenuOpen = false; sidebarOpen = false">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="14" height="14" aria-hidden="true">
              <circle cx="12" cy="8" r="4" />
              <path d="M4 20c0-4 3.6-7 8-7s8 3 8 7" />
            </svg>
            <span>Account</span>
          </router-link>
          <button class="profile-menu-item profile-menu-item--signout" @click="handleSignOut">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="14" height="14" aria-hidden="true">
              <path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4M16 17l5-5-5-5M21 12H9" />
            </svg>
            <span>Sign out</span>
          </button>
        </div>
      </div>
    </aside>

    <!-- Main content -->
    <main class="app-main">
      <router-view />
    </main>
  </div>

  <Toast position="bottom-right" />
  </template>
</template>


<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { incidentsApi } from '@/api/incidents'
import { useAuthStore } from '@/stores/auth'
import Toast from 'primevue/toast'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const isPublicPage = computed(() => !!route.meta.public)
const routerReady = ref(false)
const sidebarOpen = ref(false)
const profileMenuOpen = ref(false)
const openIncidentCount = ref(0)

async function fetchOpenIncidentCount() {
  try {
    const data = await incidentsApi.list({ status: 'open', limit: 1 })
    openIncidentCount.value = data.total
  } catch {
    // silently ignore
  }
}

async function handleSignOut() {
  await auth.signOut()
  router.push({ name: 'sign-in' })
}

let pollInterval: ReturnType<typeof setInterval> | null = null

function onDocClick(e: MouseEvent) {
  if (!(e.target as Element).closest('.sidebar-account')) {
    profileMenuOpen.value = false
  }
}

router.isReady().then(() => {
  routerReady.value = true
})

onMounted(() => {
  fetchOpenIncidentCount()
  pollInterval = setInterval(fetchOpenIncidentCount, 30_000)
  document.addEventListener('click', onDocClick, true)
})

onUnmounted(() => {
  if (pollInterval) clearInterval(pollInterval)
  document.removeEventListener('click', onDocClick, true)
})
</script>

<style scoped>
.app-layout {
  display: flex;
  min-height: 100vh;
}

/* ── Mobile header ── */
.mobile-header {
  display: none;
  align-items: center;
  gap: 12px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 56px;
  padding: 0 16px;
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border);
  z-index: 200;
}

.mobile-brand {
  display: flex;
  align-items: center;
  gap: 8px;
  font-family: var(--font-display);
  font-size: 14px;
  letter-spacing: 0.12em;
  color: var(--text-primary);
  flex: 1;
}

.mobile-incident-dot {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 22px;
  height: 22px;
  padding: 0 6px;
  border-radius: 999px;
  background: var(--danger);
  color: #fff;
  font-size: 11px;
  font-weight: 700;
  text-decoration: none;
}

.hamburger {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 5px;
  width: 32px;
  height: 32px;
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 4px;
  flex-shrink: 0;
}
.hamburger span {
  display: block;
  height: 2px;
  background: var(--text-secondary);
  border-radius: 2px;
  transition: transform 0.2s, opacity 0.2s;
}
.hamburger.open span:nth-child(1) { transform: translateY(7px) rotate(45deg); }
.hamburger.open span:nth-child(2) { opacity: 0; }
.hamburger.open span:nth-child(3) { transform: translateY(-7px) rotate(-45deg); }

.sidebar-overlay {
  display: none;
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.6);
  z-index: 149;
}

/* ── Sidebar ── */
.sidebar {
  width: 220px;
  flex-shrink: 0;
  background: var(--bg-secondary);
  border-right: 1px solid var(--border);
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  display: flex;
  flex-direction: column;
  z-index: 150;
  transition: transform 0.25s ease;
}

.sidebar-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 0 20px;
  height: 64px;
  border-bottom: 1px solid var(--border);
  flex-shrink: 0;
}

.brand-icon {
  width: 22px;
  height: 22px;
  color: var(--accent);
  flex-shrink: 0;
}

.brand-name {
  font-family: var(--font-display);
  font-size: 15px;
  letter-spacing: 0.14em;
  color: var(--text-primary);
}

/* Nav */
.sidebar-nav {
  display: flex;
  flex-direction: column;
  gap: 1px;
  padding: 12px 8px;
  flex: 1;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 9px 12px;
  border-radius: var(--radius-sm);
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--text-muted);
  text-decoration: none;
  transition: color 0.15s, background 0.15s;
  position: relative;
}
.nav-item:hover {
  color: var(--text-primary);
  background: var(--bg-hover);
}
.nav-item--active {
  color: var(--accent);
  background: rgba(212, 175, 55, 0.08);
}
.nav-item--active svg {
  color: var(--accent);
}

.nav-badge {
  margin-left: auto;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 18px;
  height: 18px;
  padding: 0 5px;
  border-radius: 999px;
  background: var(--danger);
  color: #fff;
  font-size: 10px;
  font-weight: 700;
}

/* ── User account ── */
.sidebar-account {
  border-top: 1px solid var(--border);
  padding: 8px;
  flex-shrink: 0;
  position: relative;
}

.account-row {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px;
  border-radius: var(--radius-sm);
  border: none;
  background: transparent;
  width: 100%;
  cursor: pointer;
  text-align: left;
  font-family: var(--font-body);
  transition: background 0.15s;
}
.account-row:hover,
.account-row--open {
  background: var(--bg-hover);
}

.account-avatar {
  width: 30px;
  height: 30px;
  border-radius: var(--radius-sm);
  background: rgba(212, 175, 55, 0.15);
  border: 1px solid rgba(212, 175, 55, 0.3);
  color: var(--accent);
  font-size: 11px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  letter-spacing: 0.04em;
  font-family: var(--font-display);
}

.account-info {
  flex: 1;
  min-width: 0;
}

.account-name {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.account-email {
  font-size: 11px;
  color: var(--text-muted);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.account-chevron {
  flex-shrink: 0;
  color: var(--text-muted);
  transition: transform 0.2s;
}
.account-chevron--up {
  transform: rotate(-90deg);
}

.profile-menu {
  position: absolute;
  bottom: 8px;
  left: calc(100% + 8px);
  min-width: 160px;
  background: var(--bg-secondary);
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.5);
  overflow: hidden;
  z-index: 200;
}

.profile-menu-item {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
  padding: 9px 12px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.06em;
  font-family: var(--font-body);
  color: var(--text-muted);
  background: transparent;
  border: none;
  text-decoration: none;
  cursor: pointer;
  transition: color 0.15s, background 0.15s;
}
.profile-menu-item:hover {
  color: var(--text-primary);
  background: var(--bg-hover);
}
.profile-menu-item--signout:hover {
  color: var(--danger);
}

/* ── Main content ── */
.app-main {
  margin-left: 220px;
  flex: 1;
  min-height: 100vh;
  padding: 32px;
}

/* ── Mobile breakpoint ── */
@media (max-width: 768px) {
  .mobile-header { display: flex; }
  .sidebar-overlay { display: block; }
  .sidebar { transform: translateX(-100%); top: 0; }
  .sidebar--open { transform: translateX(0); }
  .app-main { margin-left: 0; padding: 80px 16px 24px; }
}
</style>
