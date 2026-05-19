<template>
  <div class="table-container">
    <table>
      <thead>
        <tr>
          <th v-for="col in columns" :key="col.key">{{ col.label }}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(row, idx) in rows" :key="idx">
          <td v-for="col in columns" :key="col.key">
            <slot :name="col.key" :row="row" :value="row[col.key]">
              {{ row[col.key] ?? '—' }}
            </slot>
          </td>
        </tr>
        <tr v-if="!rows.length">
          <td :colspan="columns.length" class="empty">No data</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  columns: Array<{ key: string; label: string }>
  rows: Array<Record<string, any>>
}>()
</script>

<style scoped>
.table-container {
  overflow-x: auto;
}
.empty {
  text-align: center;
  color: var(--text-muted);
  padding: 32px 16px;
}
</style>
