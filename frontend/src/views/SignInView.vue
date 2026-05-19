<template>
  <div class="auth-page">
    <div class="auth-bg" aria-hidden="true"></div>
    <div class="auth-sunburst" aria-hidden="true"></div>

    <div class="auth-card">
      <!-- Corner decorations -->
      <div class="card-corner card-corner--tl" aria-hidden="true"></div>
      <div class="card-corner card-corner--br" aria-hidden="true"></div>

      <div class="auth-header">
        <router-link to="/" class="auth-logo">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75"
            stroke-linecap="round" stroke-linejoin="round" width="20" height="20" aria-hidden="true">
            <polyline points="1,12 6,12 8,6 10,18 12,8 14,15 16,12 23,12" />
          </svg>
          <span>CLOUDPULSE</span>
        </router-link>

        <div class="auth-divider" aria-hidden="true">
          <div class="divider-line"></div>
          <div class="divider-diamond"></div>
          <div class="divider-line"></div>
        </div>

        <h1 class="auth-title">SIGN IN</h1>
        <p class="auth-sub">Access your monitoring dashboard</p>
      </div>

      <form class="auth-form" @submit.prevent="handleSubmit" novalidate>
        <div class="field">
          <label for="email" class="field-label">EMAIL ADDRESS</label>
          <input
            id="email"
            v-model="form.email"
            type="email"
            autocomplete="email"
            placeholder="you@example.com"
            class="field-input"
            :class="{ 'field-input--error': errors.email }"
          />
          <span v-if="errors.email" class="field-error">{{ errors.email }}</span>
        </div>

        <div class="field">
          <label for="password" class="field-label">PASSWORD</label>
          <input
            id="password"
            v-model="form.password"
            type="password"
            autocomplete="current-password"
            placeholder="••••••••"
            class="field-input"
            :class="{ 'field-input--error': errors.password }"
          />
          <span v-if="errors.password" class="field-error">{{ errors.password }}</span>
        </div>

        <span v-if="serverError" class="server-error">{{ serverError }}</span>

        <button type="submit" class="auth-submit" :disabled="loading">
          <span v-if="loading" class="loading-dots">
            <span></span><span></span><span></span>
          </span>
          <span v-else>SIGN IN</span>
        </button>
      </form>

      <p class="auth-footer-text">
        New to CloudPulse?
        <router-link to="/sign-up" class="auth-link">Create an account</router-link>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const auth = useAuthStore()

const form = reactive({ email: '', password: '' })
const errors = reactive({ email: '', password: '' })
const serverError = ref('')
const loading = ref(false)

function validate(): boolean {
  errors.email = ''
  errors.password = ''
  let ok = true
  if (!form.email) { errors.email = 'Email is required'; ok = false }
  else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) { errors.email = 'Enter a valid email'; ok = false }
  if (!form.password) { errors.password = 'Password is required'; ok = false }
  return ok
}

async function handleSubmit() {
  if (!validate()) return
  serverError.value = ''
  loading.value = true
  try {
    await auth.signIn(form.email, form.password)
    router.push({ name: 'monitoring' })
  } catch (err: any) {
    const msg = err?.response?.data?.detail
    serverError.value = typeof msg === 'string' ? msg : 'Invalid email or password. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  background: #0A0A0A;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px 16px;
  position: relative;
  overflow: hidden;
  font-family: 'Josefin Sans', 'Inter', sans-serif;
}

.auth-bg {
  position: absolute;
  inset: 0;
  background-image:
    repeating-linear-gradient(45deg, rgba(212,175,55,0.03) 0px, rgba(212,175,55,0.03) 1px, transparent 1px, transparent 12px),
    repeating-linear-gradient(-45deg, rgba(212,175,55,0.03) 0px, rgba(212,175,55,0.03) 1px, transparent 1px, transparent 12px);
  pointer-events: none;
}

.auth-sunburst {
  position: absolute;
  inset: 0;
  background: radial-gradient(ellipse at 50% 40%, rgba(212,175,55,0.07) 0%, transparent 65%);
  pointer-events: none;
}

.auth-card {
  position: relative;
  width: 100%;
  max-width: 420px;
  background: #141414;
  border: 1px solid rgba(212,175,55,0.25);
  padding: 48px 40px 40px;
}

.card-corner {
  position: absolute;
  width: 16px;
  height: 16px;
  pointer-events: none;
}
.card-corner--tl { top: 8px; left: 8px; border-top: 2px solid #D4AF37; border-left: 2px solid #D4AF37; }
.card-corner--br { bottom: 8px; right: 8px; border-bottom: 2px solid #D4AF37; border-right: 2px solid #D4AF37; }

.auth-header {
  text-align: center;
  margin-bottom: 36px;
}

.auth-logo {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  font-family: 'Marcellus', Georgia, serif;
  font-size: 15px;
  letter-spacing: 0.15em;
  color: #F2F0E4;
  margin-bottom: 24px;
}
.auth-logo svg { color: #D4AF37; }

.auth-divider {
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 0 auto 20px;
  max-width: 200px;
}
.divider-line { flex: 1; height: 1px; background: rgba(212,175,55,0.3); }
.divider-diamond { width: 6px; height: 6px; border: 1px solid #D4AF37; transform: rotate(45deg); flex-shrink: 0; }

.auth-title {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 28px;
  font-weight: 400;
  letter-spacing: 0.2em;
  color: #F2F0E4;
  margin-bottom: 8px;
}

.auth-sub {
  font-size: 12px;
  color: #888;
  letter-spacing: 0.05em;
  font-weight: 300;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
  margin-bottom: 24px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.field-label {
  font-size: 10px;
  font-weight: 600;
  letter-spacing: 0.2em;
  color: #D4AF37;
  margin: 0;
}

.field-input {
  background: transparent;
  border: none;
  border-bottom: 2px solid rgba(212,175,55,0.4);
  border-radius: 0;
  padding: 10px 4px;
  color: #F2F0E4;
  font-size: 14px;
  font-family: inherit;
  font-weight: 300;
  outline: none;
  transition: border-color 0.3s, box-shadow 0.3s;
  width: 100%;
}
.field-input::placeholder { color: #555; }
.field-input:focus {
  border-bottom-color: #D4AF37;
  box-shadow: 0 4px 10px rgba(212,175,55,0.15);
}
.field-input--error { border-bottom-color: #ef4444; }
.field-input--error:focus { border-bottom-color: #ef4444; box-shadow: none; }

.field-error {
  font-size: 11px;
  color: #ef4444;
  letter-spacing: 0.05em;
}

.server-error {
  font-size: 12px;
  color: #ef4444;
  text-align: center;
  background: rgba(239,68,68,0.08);
  border: 1px solid rgba(239,68,68,0.2);
  padding: 10px 16px;
}

.auth-submit {
  margin-top: 8px;
  height: 48px;
  background: #D4AF37;
  color: #0A0A0A;
  border: none;
  font-family: inherit;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.2em;
  text-transform: uppercase;
  cursor: pointer;
  transition: background 0.3s, box-shadow 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}
.auth-submit:hover:not(:disabled) {
  background: #F2E8C4;
  box-shadow: 0 0 20px rgba(212,175,55,0.35);
}
.auth-submit:disabled { opacity: 0.6; cursor: not-allowed; }

.loading-dots {
  display: flex;
  gap: 5px;
  align-items: center;
}
.loading-dots span {
  width: 6px;
  height: 6px;
  background: #0A0A0A;
  border-radius: 50%;
  animation: bounce 1.2s ease-in-out infinite;
}
.loading-dots span:nth-child(2) { animation-delay: 0.2s; }
.loading-dots span:nth-child(3) { animation-delay: 0.4s; }
@keyframes bounce {
  0%, 80%, 100% { transform: scale(0.8); opacity: 0.5; }
  40% { transform: scale(1.1); opacity: 1; }
}

.auth-footer-text {
  text-align: center;
  font-size: 13px;
  color: #888;
  font-weight: 300;
}

.auth-link {
  color: #D4AF37;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.2s;
}
.auth-link:hover { color: #F2E8C4; }
</style>
