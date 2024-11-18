<script setup>
import MainHotPlaceItem from "./MainHotPlaceItem.vue";

const hotplaces = [
  { content_id: "125266", name: "국립 청태산자연휴양림", cnt: "15" },
  { content_id: "125405", name: "토함산자연휴양림", cnt: "14" },
  { content_id: "125406", name: "비슬산자연휴양림", cnt: "13" },
  { content_id: "125407", name: "불정자연휴양림", cnt: "12" },
  {
    content_id: "125408",
    name: "청송 자연휴양림 퇴적암층 (청송 국가지질공원)",
    cnt: "11",
  },
];

const hottravelplans = [
  { travel_plan_id: 5, favorite_cnt: 7, title: "여름 해수욕장 투어" },
  { travel_plan_id: 2, favorite_cnt: 6, title: "가을 단풍 명소" },
  { travel_plan_id: 1, favorite_cnt: 5, title: "수학여행 추천" },
  { travel_plan_id: 3, favorite_cnt: 4, title: "겨울 스키 여행" },
  { travel_plan_id: 4, favorite_cnt: 4, title: "봄꽃 나들이" },
];
</script>

<template>
  <div>
    <h2>핫플레이스 순위</h2>
    <div v-for="hotplace in hotplaces" :key="hotplace.content_id">
      <MainHotPlaceItem :item="hotplace" type="place" />
    </div>

    <h2>핫트래블플랜 순위</h2>
    <div
      v-for="hottravelplan in hottravelplans"
      :key="hottravelplan.travel_plan_id"
    >
      <MainHotPlaceItem :item="hottravelplan" type="travel" />
    </div>
  </div>
</template>

<style scoped>
h2 {
  margin-top: 20px;
}
</style>
