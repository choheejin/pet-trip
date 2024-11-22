<script setup>
import { defineProps, ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/user";
import BoardTravelPlanDetail from "@/components/board/BoardTravelPlanDetail.vue";
import myPageApi from "@/api/myPageApi";
import mapApi from "@/api/mapApi";

const authStore = useAuthStore();
const router = useRouter();
// console.log("로그인 ??? : ", authStore.value);

const userInfo = ref([]);

// 사용자 정보 조회 /info
const getUserInfo = async () => {
  const { data } = await myPageApi.get("/user/info", {});
  userInfo.value = data;
  console.log("사용자 정보 출력하기 : ", userInfo.value);
};

// 하트 데이터
const heart = ref({
  plan_id: "",
  user_id: null,
});

// 부모로부터 전달받을 prop 정의
// props에 id에 해당하는 travelPlanitems의 첫 번째 관광지의 img 를 추가로 가지고 있게 만들기
const props = defineProps({
  travelplans: {
    type: Array,
    required: true,
  },
  favorites: {
    type: Array,
    required: true,
  },
});

// 이벤트 정의
const emit = defineEmits(["like", "dislike"]);

// 상세보기 메서드
const TravelDetail = (id) => {
  console.log("상세보기 클릭 : ", id);
  router.push({ name: "PlanDetail", query: { id } });
};

// 좋아요 요청
const travelPlanLike = (id) => {
  console.log("좋아요 누름! : ", id);
  emit("like", { plan_id: id, user_id: userInfo.value.pk_id }); // 상위로 이벤트 전달
};

// 좋아요 취소 요청
const travelPlanDisLike = (id) => {
  console.log("좋아요 취소! : ", id);
  emit("dislike", { plan_id: id, user_id: userInfo.value.pk_id }); // 상위로 이벤트 전달
};

// 기본 이미지
const defaultImg =
  "https://st3.depositphotos.com/8687452/13536/i/450/depositphotos_135362258-stock-photo-seoul-south-korea-nov-1.jpg";

getUserInfo();
</script>

<template>
  <div class="card-container">
    <div v-for="(plan, index) in travelplans" :key="plan.id" class="card">
      <div class="card-top">
        <div
          class="card-img"
          :style="{
            backgroundImage: `url(${plan.image ? plan.image : defaultImg})`,
          }"
        ></div>
        <div class="heart">
          <!-- 로그인 안된 상태 -->
          <i class="fa-regular fa-heart" v-if="!authStore.token"></i>
          <!-- 좋아요 아이콘 - 로그인 된 상태 & 좋아요가 false -->
          <i
            class="fa-regular fa-heart"
            v-else-if="authStore.token && !favorites[index]"
            @click="travelPlanLike(plan.id)"
          ></i>
          <!-- 좋아요 아이콘 - 로그인 된 상태 & 좋아요가 true -->
          <i
            class="fa-solid fa-heart"
            v-else
            @click="travelPlanDisLike(plan.id)"
          ></i>
        </div>
        <div class="card-title">
          <div>{{ plan.title }}</div>
        </div>
      </div>
      <div class="card-bottom">
        <div class="content">
          <v-btn class="detail-btn" @click="TravelDetail(plan.id)"
            >상세보기</v-btn
          >
          <div class="stats">
            <div>
              <i class="fa fa-eye eye-icon"></i> {{ plan.view_cnt }}
              <i class="fa fa-heart heart-icon"></i> {{ plan.favorite_cnt }}
            </div>
          </div>
        </div>
        <div class="description">{{ plan.description }}</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.card-container {
  width: 100%;
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 1rem;
}

.card {
  border: 1px solid #ccd5aeca;
  border-radius: 15px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

.card-top {
  position: relative;
  border-top-left-radius: 15px;
  border-top-right-radius: 15px;
  overflow: hidden;
}

.card-img {
  background-size: cover;
  height: 182px;
  position: relative;
  filter: brightness(50%);
}

.heart {
  position: absolute;
  top: 5px;
  right: 10px;
  color: rgb(247, 67, 97);
  font-size: 30px;
  cursor: pointer;
}

.card-title {
  position: absolute;
  bottom: 10px;
  left: 10px;
  color: white;
  font-weight: bold;
  font-size: 18px;
}

.card-bottom {
  padding: 10px;
  display: flex;
  justify-content: space-between;
  flex-direction: column;
  align-items: center;
}

.content {
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.stats {
  text-align: right;
  font-size: 10px;
  padding-top: 10px;
}

.detail-btn {
  font-size: 12px;
  background-color: #ccd5aeca;
  color: white;
  padding: 5px 10px;
  font-weight: 600;
  border-radius: 5px;
}

.description {
  font-size: 12px;
  color: #555;
  margin-top: 5px;
}

.eye-icon {
  color: rgb(135, 135, 253);
}

.heart-icon {
  color: rgb(255, 143, 143);
}
</style>
