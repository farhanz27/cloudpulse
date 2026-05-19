import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'

export interface User {
  id: string
  name: string
  email: string
}

export const useAuthStore = defineStore('auth', () => {
  const user = ref<User | null>(null)
  const loading = ref(false)
  // single promise so concurrent route guards don't fire multiple /me requests
  let _initPromise: Promise<void> | null = null

  const isAuthenticated = computed(() => !!user.value)

  const initials = computed(() => {
    if (!user.value) return ''
    const parts = user.value.name.trim().split(' ')
    if (parts.length >= 2) return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase()
    return parts[0].slice(0, 2).toUpperCase()
  })

  async function _restore(): Promise<void> {
    loading.value = true
    try {
      const data = await authApi.me()
      user.value = { id: data.id, name: data.name, email: data.email }
    } catch {
      user.value = null
    } finally {
      loading.value = false
    }
  }

  function ensureInitialized(): Promise<void> {
    if (!_initPromise) _initPromise = _restore()
    return _initPromise
  }

  async function signIn(email: string, password: string): Promise<void> {
    const data = await authApi.login(email, password)
    user.value = { id: data.id, name: data.name, email: data.email }
  }

  async function signUp(name: string, email: string, password: string): Promise<void> {
    const data = await authApi.register(name, email, password)
    user.value = { id: data.id, name: data.name, email: data.email }
  }

  async function updateProfile(name: string, email: string): Promise<void> {
    const data = await authApi.updateMe(name, email)
    if (user.value) {
      user.value = { ...user.value, name: data.name, email: data.email }
    }
  }

  async function signOut(): Promise<void> {
    try {
      await authApi.logout()
    } finally {
      user.value = null
      _initPromise = null
    }
  }

  return { user, loading, isAuthenticated, initials, ensureInitialized, signIn, signUp, updateProfile, signOut }
})
