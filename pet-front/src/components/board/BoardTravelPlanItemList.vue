<script setup>
defineProps(["item", "idx"]);
</script>
<template>
  <div class="plan-item-container">
    <div>{{ idx }}</div>
    <div>{{ item.title }}</div>
    <div>{{ item.addr1 }}</div>
  </div>
</template>
<style scoped>
.plan-item-container {
  display: flex;
  gap: 20px;
  width: 100%;
  border-bottom: 1px solid rgb(209 213 219);
  padding: 1rem 0rem;
}

.plan-item-container:nth-last-child(1) {
  border-bottom: none;
}
</style>
