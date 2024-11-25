<script setup>
import { useCartStore } from "@/stores/cart";
import { onMounted } from "vue";

const props = defineProps(["attraction"]);

const cartStore = useCartStore();

const dropItem = () => {
  const value = cartStore.attraction.filter(
    (item) => item.idx !== props.attraction.idx
  );
  console.log(value);
  cartStore.setAttraction(value);
};

defineEmits(["onDragStart", "onDrop"]);
</script>

<template>
  <div
    draggable="true"
    @dragstart="$emit('onDragStart', attraction.idx)"
    @dragover.prevent
    @drop="$emit('onDrop', attraction.idx)"
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

    <svg
      xmlns="http://www.w3.org/2000/svg"
      fill="none"
      viewBox="0 0 24 24"
      stroke-width="1.5"
      stroke="currentColor"
      class="drop-svg"
      @click="dropItem"
    >
      <path
        stroke-linecap="round"
        stroke-linejoin="round"
        d="M6 18 18 6M6 6l12 12"
      />
    </svg>
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
  cursor: grab;
}

.drag-item > .drop-svg {
  width: 2rem;
  cursor: pointer;
}

.drag-item > .title {
  min-width: 15rem;
}
.drag-item > .addr1 {
  width: 100%;
}
</style>
