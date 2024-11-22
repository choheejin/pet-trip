<script setup>
import { ref, onMounted } from "vue";
import myPageApi from "@/api/myPageApi.js";

const plans = ref([]);
const page = ref(1); // 현재 페이지 번호
const loading = ref(false); // 로딩 중 상태
const hasMore = ref(true); // 추가 데이터가 있는지 여부

// 기본 이미지
const defaultImg =
  "https://st3.depositphotos.com/8687452/13536/i/450/depositphotos_135362258-stock-photo-seoul-south-korea-nov-1.jpg";

// 데이터를 가져오는 함수
const getPlans = async () => {
  if (loading.value || !hasMore.value) return; // 이미 로딩 중이거나 데이터가 더 이상 없으면 리턴
  loading.value = true; // 로딩 시작

  try {
    const { data } = await myPageApi.get("/plan/user-plan", {
      params: { page: page.value, limit: 6 }, // 페이지네이션 매개변수 추가
    });
    if (data.length > 0) {
      plans.value = [...plans.value, ...data]; // 기존 데이터에 새 데이터를 추가
      page.value += 1; // 다음 페이지로 이동
    } else {
      hasMore.value = false; // 데이터가 더 이상 없으면 플래그 설정
    }
  } catch (error) {
    console.error("데이터 조회 실패:", error);
  } finally {
    loading.value = false; // 로딩 종료
  }
};

// 스크롤을 감지하여 데이터 로드하는 함수
const loadMore = () => {
  // 이미 로딩 중이거나 데이터가 더 이상 없으면 아무것도 하지 않음
  if (loading.value || !hasMore.value) return;
  getPlans();
};

// 컴포넌트가 마운트될 때 초기 데이터 로드
onMounted(() => {
  getPlans(); // 초기 데이터 불러오기
});
</script>

<template>
  <div
    class="card-container"
    v-infinite-scroll="loadMore"
    :scroll-distance="100"
    style="overflow-y: auto; height: 100%"
  >
    <div v-for="plan in plans" :key="plan.id" class="card">
      <div class="card-top">
        <div
          class="card-img"
          :style="{
            backgroundImage: `url(${plan.image ? plan.image : defaultImg})`,
          }"
        ></div>
        <div class="card-title">
          <div>{{ plan.title }}</div>
        </div>
      </div>
      <div class="card-bottom">
        <div class="content">
          <v-btn class="detail-btn" @click="TravelDetail(plan.id)">
            상세보기
          </v-btn>
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
    <v-progress-circular
      v-if="loading"
      indeterminate
      color="primary"
      class="loading-indicator"
    />
    <div v-if="!hasMore" class="no-more-data">
      <span>더 이상 데이터가 없습니다.</span>
    </div>
  </div>
</template>

<style scoped>
.card-container {
  padding: 20px;
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 1rem;
}

.card {
  height: 240px;
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

.loading-indicator {
  text-align: center;
  margin-top: 20px;
}

.no-more-data {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #888;
}
</style>
