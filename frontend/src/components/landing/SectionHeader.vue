<template>
  <div
    class="section-header"
    v-motion
    :initial="{ opacity: 0, x: -40 }"
    :visible="{ opacity: 1, x: 0, transition: { duration: 600, ease: 'easeOut' } }"
  >
    <div class="sh-line" aria-hidden="true"></div>
    <div class="sh-text">
      <span class="sh-label">{{ label }}</span>
    </div>
    <div class="sh-line" aria-hidden="true"></div>
  </div>
</template>

<script setup lang="ts">
defineProps<{ label: string }>()
</script>

<style scoped>
.section-header {
  display: flex;
  align-items: center;
  gap: 24px;
  margin-bottom: 20px;
}
.sh-line {
  flex: 1;
  height: 1px;
  background: rgba(212,175,55,0.2);
}
.sh-text { flex-shrink: 0; }
.sh-label {
  font-size: 0.75rem;
  font-weight: 600;
  letter-spacing: 0.08em;
  color: #D4AF37;
}
</style>
