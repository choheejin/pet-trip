<script setup>
import TheHeadingNavbar from "./components/layout/TheHeadingNavbar.vue";
import { RouterView } from "vue-router";
</script>

<template>
  <div>
    <TheHeadingNavbar></TheHeadingNavbar>
    <RouterView />
  </div>
</template>

<style scoped></style>
