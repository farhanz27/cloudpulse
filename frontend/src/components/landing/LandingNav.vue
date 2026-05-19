<template>
  <nav class="lnav" :class="{ 'lnav--scrolled': scrolled }">
    <div class="lnav-inner">
      <router-link to="/" class="lnav-brand">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.75"
          stroke-linecap="round" stroke-linejoin="round" width="22" height="22" aria-hidden="true"
          style="color: #D4AF37;">
          <polyline points="1,12 6,12 8,6 10,18 12,8 14,15 16,12 23,12" />
        </svg>
        <span class="lnav-name">CLOUDPULSE</span>
      </router-link>

      <div class="lnav-center">
        <router-link class="lnav-link" to="/features">FEATURES</router-link>
        <router-link class="lnav-link" to="/integrations">INTEGRATIONS</router-link>
        <router-link class="lnav-link" to="/pricing">PRICING</router-link>
        <router-link class="lnav-link" to="/contact">CONTACT</router-link>
      </div>

      <div class="lnav-auth">
        <router-link to="/sign-in" class="lnav-link">SIGN IN</router-link>
        <router-link to="/sign-up" class="lnav-cta">GET STARTED</router-link>
      </div>

      <button
        class="lnav-hamburger"
        :class="{ 'lnav-hamburger--open': menuOpen }"
        @click="menuOpen = !menuOpen"
        :aria-expanded="menuOpen"
        aria-label="Toggle navigation menu"
      >
        <span></span>
        <span></span>
        <span></span>
      </button>
    </div>

    <!-- Mobile menu overlay -->
    <Transition name="menu">
      <div class="lnav-mobile" v-if="menuOpen" role="dialog" aria-label="Navigation menu">
        <div class="lnav-mobile-links">
          <router-link class="lnav-mobile-link" to="/features" @click="menuOpen = false">FEATURES</router-link>
          <router-link class="lnav-mobile-link" to="/integrations" @click="menuOpen = false">INTEGRATIONS</router-link>
          <router-link class="lnav-mobile-link" to="/pricing" @click="menuOpen = false">PRICING</router-link>
          <router-link class="lnav-mobile-link" to="/contact" @click="menuOpen = false">CONTACT</router-link>
        </div>
        <div class="lnav-mobile-ctas">
          <router-link to="/sign-up" class="lnav-mobile-cta-primary" @click="menuOpen = false">GET STARTED</router-link>
          <router-link to="/sign-in" class="lnav-mobile-cta-outline" @click="menuOpen = false">SIGN IN</router-link>
        </div>
      </div>
    </Transition>
  </nav>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

const menuOpen = ref(false)
const scrolled = ref(false)

function onScroll() {
  scrolled.value = window.scrollY > 20
}

onMounted(() => window.addEventListener('scroll', onScroll, { passive: true }))
onUnmounted(() => window.removeEventListener('scroll', onScroll))
</script>

<style scoped>
.lnav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  background: transparent;
  backdrop-filter: none;
  border-bottom: 1px solid transparent;
  transition: background 0.4s ease, backdrop-filter 0.4s ease, border-color 0.4s ease;
}

.lnav--scrolled {
  background: rgba(10,10,10,0.95);
  backdrop-filter: blur(8px);
  border-bottom-color: rgba(212,175,55,0.2);
}

.lnav-inner {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 32px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: relative;
}

.lnav-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  flex-shrink: 0;
  z-index: 1;
}

.lnav-name {
  font-family: 'Marcellus', Georgia, serif;
  font-size: 18px;
  letter-spacing: 0.15em;
  color: #F2F0E4;
}

.lnav-center {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  gap: 32px;
}

.lnav-auth {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-shrink: 0;
  z-index: 1;
}

.lnav-link {
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.18em;
  color: #888;
  text-decoration: none;
  cursor: pointer;
  transition: color 0.3s;
}
.lnav-link:hover,
.lnav-link.router-link-active { color: #D4AF37; }

.lnav-cta {
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.18em;
  color: #D4AF37;
  text-decoration: none;
  border: 1px solid #D4AF37;
  padding: 9px 20px;
  transition: background 0.3s, color 0.3s, box-shadow 0.3s;
}
.lnav-cta:hover {
  background: #D4AF37;
  color: #0A0A0A;
  box-shadow: 0 0 20px rgba(212,175,55,0.3);
}

/* Hamburger button */
.lnav-hamburger {
  display: none;
  flex-direction: column;
  justify-content: center;
  gap: 5px;
  width: 36px;
  height: 36px;
  background: transparent;
  border: 1px solid rgba(212,175,55,0.2);
  cursor: pointer;
  padding: 8px;
  z-index: 1;
  transition: border-color 0.3s;
}
.lnav-hamburger:hover { border-color: rgba(212,175,55,0.5); }

.lnav-hamburger span {
  display: block;
  width: 100%;
  height: 1px;
  background: #D4AF37;
  transition: transform 0.3s, opacity 0.3s;
  transform-origin: center;
}

.lnav-hamburger--open span:nth-child(1) { transform: translateY(6px) rotate(45deg); }
.lnav-hamburger--open span:nth-child(2) { opacity: 0; }
.lnav-hamburger--open span:nth-child(3) { transform: translateY(-6px) rotate(-45deg); }

/* Mobile overlay */
.lnav-mobile {
  position: fixed;
  top: 64px;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(10,10,10,0.98);
  backdrop-filter: blur(12px);
  border-top: 1px solid rgba(212,175,55,0.15);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 48px;
  padding: 48px 32px;
}

.lnav-mobile-links {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
}

.lnav-mobile-link {
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 0.25em;
  color: #666;
  text-decoration: none;
  transition: color 0.3s;
  font-family: 'Josefin Sans', 'Inter', sans-serif;
}
.lnav-mobile-link:hover,
.lnav-mobile-link.router-link-active { color: #D4AF37; }

.lnav-mobile-ctas {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  width: 100%;
  max-width: 280px;
}

.lnav-mobile-cta-primary {
  display: block;
  width: 100%;
  text-align: center;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.2em;
  color: #0A0A0A;
  background: #D4AF37;
  text-decoration: none;
  padding: 15px;
  transition: background 0.3s;
}
.lnav-mobile-cta-primary:hover { background: #F2E8C4; }

.lnav-mobile-cta-outline {
  display: block;
  width: 100%;
  text-align: center;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.2em;
  color: #D4AF37;
  border: 1px solid rgba(212,175,55,0.4);
  text-decoration: none;
  padding: 15px;
  transition: background 0.3s, border-color 0.3s;
}
.lnav-mobile-cta-outline:hover {
  background: rgba(212,175,55,0.06);
  border-color: #D4AF37;
}

/* Transition */
.menu-enter-active,
.menu-leave-active { transition: opacity 0.25s, transform 0.25s; }
.menu-enter-from,
.menu-leave-to { opacity: 0; transform: translateY(-8px); }

@media (max-width: 768px) {
  .lnav-center { display: none; }
  .lnav-auth { display: none; }
  .lnav-hamburger { display: flex; }
  .lnav-inner { padding: 0 20px; }
}
</style>
