<script setup>
const props = defineProps(["attraction", "idx"]);

defineEmits(["onDragStart", "onDrop"]);
</script>

<template>
  <div
    draggable="true"
    @dragstart="$emit('onDragStart', idx)"
    @dragover.prevent
    @drop="$emit('onDrop', idx)"
    class="drag-item"
  >
    <svg
      xmlns="http://www.w3.org/2000/svg"
      fill="none"
      viewBox="0 0 24 24"
      stroke-width="1.5"
      stroke="currentColor"
      class="drag-bar"
    >
      <path
        stroke-linecap="round"
        stroke-linejoin="round"
        d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"
      />
    </svg>

    <div class="title">{{ attraction.title }}</div>
    <div class="addr1">{{ attraction.addr1 }}</div>
  </div>
</template>

<style scoped>
.drag-item {
  display: flex;
  gap: 1.25rem;
  border-bottom: 1px solid rgba(209, 213, 219, 0.418);
  align-items: center;
  margin: 0.5rem;
  padding: 1rem;
}
.drag-item > .drag-bar {
  width: 2rem;
}

.drag-item > .title {
  min-width: 20rem;
}
.drag-item > .addr1 {
  width: 100%;
}
</style>
