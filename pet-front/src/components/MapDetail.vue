<script setup>
import { defineProps } from "vue";

defineProps(["attraction"]);
</script>

<template>
  <div>
    <button @click="$emit('setShowDetail', false)">닫기</button>
    <div>{{ attraction.title }}</div>
    <div>{{ attraction.addr1 }}</div>
  </div>
</template>

<style scoped></style>
