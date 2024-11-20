<script setup>
import MainRank from "@/components/main/MainRank.vue";
import MainSidoCard from "@/components/main/MainSidoCard.vue";
import MainDogSize from "@/components/main/MainDogSize.vue";
</script>

<template>
  <div style="height: calc(100vh - 66px); display: flex; align-items: center">
    <div style="width: 1000px; margin: 0 auto; height: 100%; display: flex">
      <div style="display: flex; width: 70%; flex-direction: column">
        <div style="height: 70%">
          <MainSidoCard />
        </div>
        <div style="height: 30%">
          <MainDogSize />
        </div>
      </div>
      <div class="rank" style="width: 30%">
        <MainRank />
      </div>
    </div>
  </div>
</template>

<style scoped>
.rank {
  display: flex;
  align-items: center;
}
</style>
