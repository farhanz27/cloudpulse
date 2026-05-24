<template>
  <div class="step-slider">
    <Slider v-model="index" :min="0" :max="steps.length - 1" :step="1" class="w-full" />
    <div class="step-labels">
      <span
        v-for="(step, i) in steps"
        v-if="step.label"
        :key="i"
        class="step-label"
        :class="{ active: i === index }"
        :style="labelStyle(i)"
      >{{ step.label }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import Slider from 'primevue/slider'

const props = defineProps<{
  steps: Array<{ label: string; value: number }>
  modelValue: number
}>()

const emit = defineEmits<{ 'update:modelValue': [value: number] }>()

function closestIndex(val: number): number {
  let closest = 0
  let minDiff = Infinity
  props.steps.forEach((s, i) => {
    const diff = Math.abs(s.value - val)
    if (diff < minDiff) { minDiff = diff; closest = i }
  })
  return closest
}

const index = ref(closestIndex(props.modelValue))

function labelStyle(i: number): Record<string, string> {
  const left = (i / (props.steps.length - 1)) * 100
  if (i === 0) return { left: '0%' }
  if (i === props.steps.length - 1) return { right: '0%', left: 'auto' }
  return { left: `${left}%`, transform: 'translateX(-50%)' }
}

watch(index, (i) => {
  emit('update:modelValue', props.steps[i].value)
})

watch(() => props.modelValue, (val) => {
  const i = closestIndex(val)
  if (i !== index.value) index.value = i
})
</script>

<style scoped>
.w-full { width: 100%; }

.step-slider {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.step-labels {
  position: relative;
  height: 16px;
}

.step-label {
  position: absolute;
  top: 0;
  font-size: 11px;
  color: var(--text-muted);
  font-family: var(--font-mono);
  transition: color 0.15s;
  white-space: nowrap;
  line-height: 1;
}

.step-label.active {
  color: var(--accent);
  font-weight: 700;
}
</style>
