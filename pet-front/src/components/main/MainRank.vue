<script setup>
import { ref } from "vue";
import attractionApi from "@/api/attractionApi.js";
import router from "@/router/index.js";
import { useMainSelectStore } from "@/stores/mainselect.js";

// 검색 결과
const mainSelectStore = useMainSelectStore();
const hotplaces = ref([]);
const hotplans = ref([]);
const selectedAttraction = ref("");
const selectedTravelPlan = ref("");

// 검색 하기
const getHotPlaces = async () => {
  try {
    const { data } = await attractionApi.get("/hotplace-ranking", {});
    hotplaces.value = data || [];
  } catch (error) {
    console.error("Hotplaces API 호출 에러:", error);
  }
};
const getHotPlans = async () => {
  try {
    const { data } = await attractionApi.get("/plan-ranking", {});
    hotplans.value = data || [];
    // console.log("여행계획 출력!!! : ", hotplans.value);
  } catch (error) {
    console.error("Hotplans API 호출 에러:", error);
  }
};
getHotPlaces();
getHotPlans();

// 순위에서 하나 클릭하면 - 페이지 이동
function selectAttraction(content_id) {
  console.log("선택된 핫플레이스 : ", content_id);
  selectedAttraction.value = content_id;
  mainSelectStore.setAttraction(content_id);
  // 이걸로 BaseMap 에 전송??
  // router.push({name:"BaseMap"});
}
function selectTravelPlan(id) {
  // console.log("선택된 여행 계획 : ", id);
  selectedTravelPlan.value = id;
  mainSelectStore.setTravelPlan(id);
  // 이걸로 여행 세부 계획 전송??
  // router.push({});
}
</script>
<template>
  <div>
    <h5>인기 관광지</h5>
    <ul>
      <li
        v-for="(place, index) in hotplaces.slice(0, 5)"
        :key="index"
        @click="selectAttraction(place.content_id)"
        class="hotPlaceItem"
      >
        <div class="rankNum">{{ index + 1 }}</div>
        <div class="hotPlaceContent">
          <div class="hotPlaceTitle">{{ place.title }}</div>
          <div class="hotPlaceAddr">{{ place.addr1 }}</div>
        </div>
        <div class="hotPlaceFavoriteCnt">
          <i class="fa fa-heart"></i> {{ place.favorite_cnt }}
        </div>
      </li>
    </ul>

    <h5>인기 여행 계획</h5>
    <ul>
      <li
        v-for="(plan, index) in hotplans.slice(0, 5)"
        :key="index"
        @click="selectTravelPlan(plan.id)"
        class="hotPlanItem"
      >
        <div class="rankNum">{{ index + 1 }}</div>
        <div class="hotPlanContent">
          <div class="hotPlanTitle">{{ plan.title }}</div>
          <div class="hotPlanDesc">{{ plan.description }}</div>
        </div>

        <div class="hotPlanStatus">
          <span> <i class="fa fa-heart"></i> {{ plan.favorite_cnt }} </span>
          <span> <i class="fa fa-eye"></i> {{ plan.view_cnt }} </span>
        </div>
      </li>
    </ul>
  </div>
</template>

<style scoped>
ul {
  list-style: none;
  padding: 0;
}
li {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 10px;
  border-bottom: 1px solid #ccd5ae;
  cursor: pointer;
  transition: background 0.3s ease;
}
li:hover {
  background: #fefae0;
  border-radius: 15px;
}
.rankNum {
  padding-right: 10px;
  font-weight: bold;
}
.hotPlaceItem .hotPlaceTitle,
.hotPlanItem .hotPlanTitle {
  font-size: 15px;
  font-weight: bold;
}
.hotPlaceItem .hotPlaceAddr,
.hotPlanItem .hotPlanDesc {
  font-size: 10px;
  color: #888;
}

.hotPlanContent,
.hotPlaceContent {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
}

.hotPlanDesc {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 150px;
}
.hotPlaceItem .hotPlaceFavoriteCnt,
.hotPlanItem .hotPlanStatus {
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.hotPlaceItem .fa-heart,
.hotPlanItem .fa-heart {
  margin-right: 3px;
  color: #ff5a5f;
}
.hotPlanItem .fa-eye {
  margin-right: 3px;
  color: #5995ff;
}
</style>
