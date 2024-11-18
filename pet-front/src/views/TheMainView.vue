<script setup>
import MainRank from "@/components/main/MainRank.vue";
import MainSidoCard from "@/components/main/MainSidoCard.vue";
import MainDogSize from "@/components/main/MainDogSize.vue";
</script>

<template>
  <div style="height: calc(100vh - 66px); width: 100%">
    <div style="height: 100%; display: flex">
      <div style="width: 35%">
        <MainRank />
      </div>
      <div style="display: flex; width: 65%; flex-direction: column">
        <div style="height: 70%">
          <MainSidoCard />
        </div>
        <div style="height: 30%">
          <MainDogSize />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
