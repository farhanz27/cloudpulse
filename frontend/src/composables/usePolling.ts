import { ref, onMounted, onUnmounted } from 'vue'

export function usePolling(fn: () => Promise<void>, intervalMs = 30000) {
  const isPolling = ref(true)
  let timer: ReturnType<typeof setInterval> | null = null

  function start() {
    fn()
    timer = setInterval(() => {
      if (isPolling.value) fn()
    }, intervalMs)
  }

  function stop() {
    if (timer) {
      clearInterval(timer)
      timer = null
    }
  }

  onMounted(start)
  onUnmounted(stop)

  return { isPolling, stop }
}
