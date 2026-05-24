import { ref, onMounted, onUnmounted } from 'vue'

export function usePolling(fn: () => Promise<void>, intervalMs = 30000) {
  const isPolling = ref(true)
  let timer: ReturnType<typeof setInterval> | null = null

  function schedule() {
    timer = setInterval(() => {
      if (isPolling.value && !document.hidden) fn()
    }, intervalMs)
  }

  function stop() {
    if (timer) {
      clearInterval(timer)
      timer = null
    }
  }

  function onVisibilityChange() {
    if (!document.hidden) {
      fn()
      stop()
      schedule()
    }
  }

  onMounted(() => {
    fn()
    schedule()
    document.addEventListener('visibilitychange', onVisibilityChange)
  })

  onUnmounted(() => {
    stop()
    document.removeEventListener('visibilitychange', onVisibilityChange)
  })

  return { isPolling, stop }
}
