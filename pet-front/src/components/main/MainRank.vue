<script setup>
import {ref} from "vue"
import attractionApi from "@/api/attractionApi.js";
import router from "@/router/index.js"
import {useMainSelectStore} from "@/stores/mainselect.js";

// 검색 결과
const mainSelectStore = useMainSelectStore();
const hotplaces = ref([]);
const hotplans = ref([]);
const selectedAttraction = ref('');
const selectedTravelPlan = ref('');

// 검색 하기
const getHotPlaces = async () => {
  const {data} = await attractionApi.get("/hotplace-ranking",{
    params:{
      page:1
    },
  });
  hotplaces.value = data;
  console.log(hotplaces.value)
}
const getHotPlans = async () => {
  const {data} = await attractionApi.get("/plan-ranking",{
    params:{
      page:1
    },
  });
  hotplans.value = data;
  console.log(hotplans.value)
}
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
  console.log("선택된 여행 계획 : ", id);
  selectedTravelPlan.value = id;
  mainSelectStore.setTravelPlan(id);
  // 이걸로 여행 세부 계획 전송??
  // router.push({});
}
</script>
<template>
  <div>
<!--    <h5>  핫 플레이스</h5>-->
    <ul>
      <li v-for="(place, index) in hotplaces.slice(0, 5)" :key="index"
          @click="selectAttraction(place.content_id)"
          class="hotPlaceItem">
        <div>
        <div class="hotPlaceTitle">{{ place.title }}</div>
        <div class="hotPlaceAddr">{{ place.addr1 }}</div>
        </div>
        <div class="hotPlaceFavoriteCnt">
          <i class="fa fa-heart"></i> {{ place.favorite_cnt }}
        </div>
      </li>
    </ul>

<!--    <h5>Hot Plans (Top 5)</h5>-->
    <ul>
      <li
        v-for="(plan, index) in hotplans.slice(0, 5)"
        :key="index"
        @click="selectTravelPlan(plan.id)"
        class="hotPlanItem"
      >
        <div><div class="hotPlanTitle">{{ plan.title }}</div>
        <div class="hotPlanDesc">{{plan.description}}</div></div>

        <div class="hotPlanStatus">
          <span>
            <i class="fa fa-heart"></i> {{ plan.favorite_cnt }}
          </span>
          <span>
            <i class="fa fa-eye"></i> {{ plan.view_cnt }}
          </span>
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
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #ccd5ae;
  cursor: pointer;
  transition: background 0.3s ease;
}
li:hover {
  background: #fefae0;
  border-radius: 15px;
}
.hotPlaceItem .hotPlaceTitle,
.hotPlanItem .hotPlanTitle {
  font-size: 15px;
  font-weight: bold;
}
.hotPlaceItem .hotPlaceAddr,
.hotPlanItem .hotPlanDesc{
  font-size: 10px;
  color: #888;
}
.hotPlanDesc {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 180px; /* 예시로 설정한 너비, 필요에 따라 조정 */
}
.hotPlaceItem .hotPlaceFavoriteCnt,
.hotPlanItem .hotPlanStatus {
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px
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
