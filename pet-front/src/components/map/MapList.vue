<script setup>
defineProps(["attraction"]);
</script>

<template>
  <div class="container">
    <div class="title">{{ attraction.title }}</div>
    <div>{{ attraction.addr1 }}</div>
  </div>
</template>

<style scoped>
.container {
  cursor: pointer;
  padding: 1.5rem 3rem;
  border-bottom: 1px solid rgb(209 213 219);
}

.container:nth-last-child(1) {
  border-bottom: none;
}

.container:hover {
  background-color: rgb(209 213 219);
}

.container > .title {
  color: rgb(29 78 216);
  font-weight: 600;
  font-size: large;
}
</style>
