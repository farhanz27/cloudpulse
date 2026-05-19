import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(),
  scrollBehavior() {
    return { top: 0 }
  },
  routes: [
    {
      path: '/',
      name: 'landing',
      component: () => import('@/views/LandingView.vue'),
      meta: { public: true, guestOnly: true },
    },
    {
      path: '/sign-in',
      name: 'sign-in',
      component: () => import('@/views/SignInView.vue'),
      meta: { public: true, guestOnly: true },
    },
    {
      path: '/sign-up',
      name: 'sign-up',
      component: () => import('@/views/SignUpView.vue'),
      meta: { public: true, guestOnly: true },
    },
    {
      path: '/dashboard',
      name: 'monitoring',
      component: () => import('@/views/DashboardView.vue'),
    },
    {
      path: '/services/:id',
      name: 'service-detail',
      component: () => import('@/views/ServiceDetailView.vue'),
      props: true,
    },
    {
      path: '/incidents',
      name: 'incidents',
      component: () => import('@/views/IncidentsView.vue'),
    },
    {
      path: '/status-pages',
      name: 'status-pages',
      component: () => import('@/views/StatusPagesView.vue'),
    },
    {
      path: '/status/:slug',
      name: 'status-page-public',
      component: () => import('@/views/StatusPagePublicView.vue'),
      props: true,
      meta: { public: true },
    },
    {
      path: '/integrations',
      name: 'integrations-public',
      component: () => import('@/views/IntegrationsPublicView.vue'),
      meta: { public: true },
    },
    {
      path: '/app/integrations',
      name: 'integrations',
      component: () => import('@/views/IntegrationsView.vue'),
    },
    {
      path: '/alerts',
      name: 'alerts',
      component: () => import('@/views/AlertsView.vue'),
    },
    {
      path: '/account',
      name: 'account',
      component: () => import('@/views/AccountView.vue'),
    },
    {
      path: '/contact',
      name: 'contact',
      component: () => import('@/views/ContactView.vue'),
      meta: { public: true },
    },
    {
      path: '/terms',
      name: 'terms',
      component: () => import('@/views/TermsView.vue'),
      meta: { public: true },
    },
    {
      path: '/privacy',
      name: 'privacy',
      component: () => import('@/views/PrivacyView.vue'),
      meta: { public: true },
    },
    {
      path: '/features',
      name: 'features',
      component: () => import('@/views/FeaturesView.vue'),
      meta: { public: true },
    },
    {
      path: '/pricing',
      name: 'pricing',
      component: () => import('@/views/PricingView.vue'),
      meta: { public: true },
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('@/views/AboutView.vue'),
      meta: { public: true },
    },
    {
      path: '/docs',
      name: 'docs',
      component: () => import('@/views/DocsView.vue'),
      meta: { public: true },
    },
    {
      path: '/support',
      name: 'support',
      component: () => import('@/views/SupportView.vue'),
      meta: { public: true },
    },
    {
      path: '/updates',
      name: 'updates',
      component: () => import('@/views/UpdatesView.vue'),
      meta: { public: true },
    },
    {
      path: '/cookies',
      name: 'cookies',
      component: () => import('@/views/CookiesView.vue'),
      meta: { public: true },
    },
  ],
})

router.beforeEach(async (to) => {
  const auth = useAuthStore()

  // Hydrate session from cookie on first navigation
  await auth.ensureInitialized()

  if (!to.meta.public && !auth.isAuthenticated) {
    return { name: 'sign-in' }
  }
  if (to.meta.guestOnly && auth.isAuthenticated) {
    return { name: 'monitoring' }
  }
})

export default router
