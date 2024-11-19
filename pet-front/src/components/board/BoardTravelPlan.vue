<script setup>
import { ref, onMounted, defineProps, computed } from "vue";
import attractionApi from "@/api/attractionApi.js";

const plans = ref([]);
// 부모로부터 전달받을 prop 정의
// props에 id에 해당하는 travelPlanitems의 첫 번째 관광지의 img 를 추가로 가지고 있게 만들기
const props = defineProps({
  travelplans: {
    type: Array,
    required: true,
  },
});

// 이미지 데이터를 받아오는 함수
const getImage = async () => {
  const{data} = await attractionApi.get("/image", {
    params:{
    }
  });
  return data.imageUrl; // 예시로 imageUrl 반환한다고 가정하기
}

// travelplans를 처리하고 이미지 url 추가하는 함수
const loadPlans = async () => {
  const updatePlans = await Promise.all(
    props.travelplans.map(async (plan) => {
      const imageUrl = await getImage(plan.id);
      return {...plan, img: imageUrl}
    })
  );
  plans.value = updatePlans;
}

// 페이지가 마운트 될 때 실행
onMounted(() => {
  loadPlans();
})

// 카드뷰가 6의 배수가 아닐 경우 - 더미 데이터 추가하기
const paddedPlans = computed(() => {
  const padded = [...plans.value];
  while (padded.length < 6) {
    padded.push({ img: '', title: '', description: '', view_cnt: 0, favorite_cnt: 0, liked: false });
  }
  return padded;
});

// 상세보기 메서드
const TravelDetail = (id) => {
  console.log("상세보기 클릭 : ", id);
}
// 좋아요 누르는 메서드
const travelPlanLike = (id) => {
  console.log("좋아요 클릭함! : ", id)
}
// travelplans 콘솔 출력
console.log(props.travelplans);
</script>

<template>
  <div>
<!--    <h2>Travel Plans:</h2>-->
    <v-row>
      <!-- plans 배열을 2행 3열로 카드뷰 형태로 출력 -->
      <v-col v-for="(plan, index) in paddedPlans" :key="index" cols="12" sm="4">
        <v-card :style="{ backgroundImage: `url(${plan.img})`, backgroundSize: 'cover', height: '300px' }">
          <v-card-actions>
            <v-btn @click="TravelDetail(plan.id)">상세보기</v-btn>
            <!-- if 문으로 하트 누르기 전 후-->
            <i class="fa-regular fa-heart"></i>
            <i class="fa-solid fa-heart"></i>
          </v-card-actions>
          <v-card-title>{{ plan.title }}</v-card-title>
          <v-card-text>{{ plan.description }}</v-card-text>
          <v-card-subtitle>
            <i class="fa fa-eye"></i> {{ plan.view_cnt }}
            <i class="fa fa-heart"></i> {{ plan.favorite_cnt }}
          </v-card-subtitle>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<style scoped>
.v-card {
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
}

.v-card-title {
  font-weight: bold;
  font-size: 18px;
}

.v-card-subtitle {
  display: flex;
  justify-content: space-between;
}

.v-card-actions {
  position: absolute;
  bottom: 0;
  width: 100%;
  background: rgba(0, 0, 0, 0.5); /* 투명 배경 */
  padding: 10px;
  display: flex;
  justify-content: space-between;
}
</style>
