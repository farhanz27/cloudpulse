<template>
  <div class="sp-view">
    <div class="page-header">
      <div>
        <h1 class="page-title">Status Pages</h1>
        <p class="page-subtitle">Public status pages for your services</p>
      </div>
    </div>

    <div class="coming-soon">
      <div class="cs-icon">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" width="36" height="36">
          <circle cx="12" cy="12" r="10" stroke-linecap="round" stroke-linejoin="round" />
          <path d="M2 12h20M12 2a15.3 15.3 0 010 20M12 2a15.3 15.3 0 000 20" stroke-linecap="round" stroke-linejoin="round" />
        </svg>
      </div>
      <h2 class="cs-title">Coming Soon</h2>
      <p class="cs-desc">Status pages are on the way. You'll be able to create public status pages to keep your users informed.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.sp-view { width: 100%; }

.page-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}
.page-title { font-size: 28px; font-weight: 700; color: var(--text-primary); margin: 0 0 4px; }
.page-subtitle { font-size: 14px; color: var(--text-muted); margin: 0; }

.coming-soon {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  padding: 80px 20px;
  text-align: center;
}

.cs-icon {
  width: 72px;
  height: 72px;
  border-radius: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-muted);
}

.cs-title {
  font-size: 22px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.cs-desc {
  font-size: 14px;
  color: var(--text-muted);
  margin: 0;
  max-width: 380px;
  line-height: 1.6;
}
</style>
