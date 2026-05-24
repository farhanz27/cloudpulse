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
        <CloudPulseWordmark />
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
        <CloudPulseWordmark />
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


        <router-link to="/settings" class="nav-item" active-class="nav-item--active" @click="sidebarOpen = false">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="16" height="16" aria-hidden="true">
            <circle cx="12" cy="12" r="3" />
            <path d="M19.4 15a1.65 1.65 0 00.33 1.82l.06.06a2 2 0 010 2.83 2 2 0 01-2.83 0l-.06-.06a1.65 1.65 0 00-1.82-.33 1.65 1.65 0 00-1 1.51V21a2 2 0 01-4 0v-.09A1.65 1.65 0 009 19.4a1.65 1.65 0 00-1.82.33l-.06.06a2 2 0 01-2.83-2.83l.06-.06A1.65 1.65 0 004.68 15a1.65 1.65 0 00-1.51-1H3a2 2 0 010-4h.09A1.65 1.65 0 004.6 9a1.65 1.65 0 00-.33-1.82l-.06-.06a2 2 0 012.83-2.83l.06.06A1.65 1.65 0 009 4.68a1.65 1.65 0 001-1.51V3a2 2 0 014 0v.09a1.65 1.65 0 001 1.51 1.65 1.65 0 001.82-.33l.06-.06a2 2 0 012.83 2.83l-.06.06A1.65 1.65 0 0019.4 9a1.65 1.65 0 001.51 1H21a2 2 0 010 4h-.09a1.65 1.65 0 00-1.51 1z" />
          </svg>
          <span>Settings</span>
        </router-link>
      </nav>

      <!-- Sidebar bottom -->
      <div class="sidebar-bottom">
        <div class="sidebar-profile">
          <router-link to="/settings?tab=profile" class="profile-link" @click="sidebarOpen = false">
            <div class="account-avatar" aria-hidden="true">{{ auth.initials }}</div>
            <div class="account-info">
              <div class="account-name">{{ auth.user?.name }}</div>
              <div class="account-email">{{ auth.user?.email }}</div>
            </div>
          </router-link>
          <button class="logout-btn" @click="handleSignOut" aria-label="Sign out">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" width="16" height="16" aria-hidden="true">
              <path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4M16 17l5-5-5-5M21 12H9" />
            </svg>
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
import CloudPulseWordmark from '@/components/CloudPulseWordmark.vue'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const isPublicPage = computed(() => !!route.meta.public)
const routerReady = ref(false)
const sidebarOpen = ref(false)
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

router.isReady().then(() => {
  routerReady.value = true
})

onMounted(() => {
  fetchOpenIncidentCount()
  pollInterval = setInterval(fetchOpenIncidentCount, 30_000)
})

onUnmounted(() => {
  if (pollInterval) clearInterval(pollInterval)
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
  width: 276px;
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
  justify-content: center;
  gap: 10px;
  padding: 0 20px;
  height: 64px;
  border-bottom: 1px solid var(--border);
  flex-shrink: 0;
}


/* Nav */
.sidebar-nav {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 16px 10px;
  flex: 1;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 14px;
  border-radius: var(--radius-sm);
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 0.02em;
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

/* ── Sidebar bottom ── */
.sidebar-bottom {
  border-top: 1px solid var(--border);
  padding: 10px 10px 12px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.sidebar-profile {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 2px 0;
}

.profile-link {
  display: flex;
  align-items: center;
  gap: 10px;
  flex: 1;
  min-width: 0;
  padding: 8px 10px;
  border-radius: var(--radius-sm);
  text-decoration: none;
  transition: background 0.15s;
}
.profile-link:hover {
  background: var(--bg-hover);
}

.account-avatar {
  width: 36px;
  height: 36px;
  border-radius: var(--radius-sm);
  background: rgba(212, 175, 55, 0.15);
  border: 1px solid rgba(212, 175, 55, 0.3);
  color: var(--accent);
  font-size: 13px;
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

.logout-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  flex-shrink: 0;
  border: none;
  background: transparent;
  border-radius: var(--radius-sm);
  color: var(--text-muted);
  cursor: pointer;
  transition: color 0.15s, background 0.15s;
}
.logout-btn:hover {
  color: var(--danger);
  background: var(--bg-hover);
}

/* ── Main content ── */
.app-main {
  margin-left: 276px;
  flex: 1;
  height: 100vh;
  overflow-y: auto;
  padding: 32px;
}

/* ── Mobile breakpoint ── */
@media (max-width: 768px) {
  .mobile-header { display: flex; }
  .sidebar-overlay { display: block; }
  .sidebar { transform: translateX(-100%); top: 0; }
  .sidebar--open { transform: translateX(0); }
  .app-main { margin-left: 0; height: 100dvh; padding: 80px 16px 24px; }
}
</style>
