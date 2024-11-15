<script setup>
import { defineProps } from "vue";

defineProps(["attraction"]);
</script>

<template>
  <div class="container">
    <div>{{ attraction.title }}</div>
    <div>{{ attraction.addr1 }}</div>
  </div>
</template>

<style scoped>
.container {
  cursor: pointer;
  margin: 10px 0px;
  border-bottom: 1px solid;
  padding-bottom: 10px;
}
.container:nth-last-child(1) {
  border-bottom: none;
}
</style>
