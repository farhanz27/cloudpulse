<script setup lang="ts">
import CloudPulseLogo from '@/components/CloudPulseLogo.vue'
</script>

<template>
  <span class="cp-wordmark">
    <CloudPulseLogo />
    CloudPulse
  </span>
</template>

<style scoped>
.cp-wordmark {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  font-family: 'Marcellus', Georgia, serif;
  font-size: 18px;
  letter-spacing: 0.03em;
  color: #F2F0E4;
  line-height: 1;
}
</style>
