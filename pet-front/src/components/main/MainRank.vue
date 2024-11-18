<script setup>
import { useRankStore } from "@/stores/rankStore";
import { onMounted } from "vue";

const rankStore = useRankStore();

onMounted(() => {
  rankStore.fetchHotPlaces();
  rankStore.fetchTravelPlans();
});

console.log("여기여기여기");

const { sortedHotPlaces, sortedTravelPlans } = rankStore;
</script>
<template>
  <div class="main-rank">
    <section class="hot-place">
      <h2>핫플레이스 순위</h2>
      <ul>
        <li v-for="(place, index) in sortedHotPlaces" :key="place.id">
          {{ index + 1 }}. {{ place.name }}
        </li>
      </ul>
    </section>

    <section class="travel-plan">
      <h2>여행 계획 순위</h2>
      <ul>
        <li v-for="(plan, index) in sortedTravelPlans" :key="plan.id">
          {{ index + 1 }}. {{ plan.name }}
        </li>
      </ul>
    </section>
  </div>
</template>

<style scoped>
.main-rank {
  padding: 20px;
}

section {
  margin-bottom: 40px;
}

h2 {
  font-size: 1.5rem;
  margin-bottom: 10px;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  padding: 10px 0;
  font-size: 1.2rem;
}
</style>
