import apiClient from './client'

export interface UserPayload {
  id: string
  name: string
  email: string
  created_at: string
}

export interface PreferencesPayload {
  emailAlerts: boolean
  recoveryNotifications: boolean
  defaultIntervalSeconds: number
  defaultTimeoutSeconds: number
  defaultLatencyThresholdMs: number
}

export const authApi = {
  async register(name: string, email: string, password: string): Promise<UserPayload> {
    const { data } = await apiClient.post<UserPayload>('/auth/register', { name, email, password })
    return data
  },

  async login(email: string, password: string): Promise<UserPayload> {
    const { data } = await apiClient.post<UserPayload>('/auth/login', { email, password })
    return data
  },

  async logout(): Promise<void> {
    await apiClient.post('/auth/logout')
  },

  async me(): Promise<UserPayload> {
    const { data } = await apiClient.get<UserPayload>('/auth/me')
    return data
  },

  async updateMe(name?: string, email?: string): Promise<UserPayload> {
    const { data } = await apiClient.patch<UserPayload>('/auth/me', { name, email })
    return data
  },

  async changePassword(currentPassword: string, newPassword: string): Promise<void> {
    await apiClient.patch('/auth/me/password', { currentPassword, newPassword })
  },

  async getPreferences(): Promise<PreferencesPayload> {
    const { data } = await apiClient.get<PreferencesPayload>('/auth/me/preferences')
    return data
  },

  async updatePreferences(prefs: Partial<PreferencesPayload>): Promise<PreferencesPayload> {
    const { data } = await apiClient.patch<PreferencesPayload>('/auth/me/preferences', prefs)
    return data
  },

  async deleteAccount(): Promise<void> {
    await apiClient.delete('/auth/me')
  },
}
