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

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap");

* {
  font-family: "Noto Sans KR", sans-serif;
  font-optical-sizing: auto;
  font-weight: 500;
  font-style: normal;
}
</style>
