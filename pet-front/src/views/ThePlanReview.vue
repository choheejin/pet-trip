<script setup>
import { onMounted, ref } from "vue";
import reviewApi from "@/api/reviewApi";
import PlanReviewSide from "@/components/review/PlanReviewSide.vue";

// 검색 결과
const travelreviews = ref([]);
const loading = ref(false); // 로딩 중 상태
const hasMore = ref(true); // 추가 데이터가 있는지 여부
const page = ref(1); // 현재 페이지 번호

// 정렬 조건과 강아지 크기
const order_by = ref(""); // 정렬 조건
const dog_size = ref(0); // 강아지 크기

// 검색하기 - 정렬 조건 및 강아지 크기 필터링
const getTravelReviewsBySorting = async () => {
  console.log("조건 : ", {
    order_by: order_by.value,
    dog_size: dog_size.value,
  });
  try {
    const { data } = await reviewApi.get("", {
      params: {
        orderBy: order_by.value,
        dog_size: dog_size.value,
      },
    });
    console.log("조회 결과 : ", data);
    if (data.length > 0) {
      travelreviews.value = [...travelreviews.value, ...data]; // 기존 데이터에 새 데이터를 추가
      page.value += 1; // 다음 페이지로 이동
    } else {
      hasMore.value = false; // 데이터가 더 이상 없으면 플래그 설정
    }
  } catch (error) {
    console.error("Error fetching travel reviews:", error);
  } finally {
    loading.value = false; // 로딩 종료
  }
};

// update-sort 이벤트 처리
const updateSort = (newSort) => {
  order_by.value = newSort; // 정렬 조건 업데이트
  getTravelReviewsBySorting(); // 검색 메서드 실행
};

// update-size 이벤트 처리
const updateSize = (newSize) => {
  dog_size.value = newSize; // 강아지 크기 업데이트
  getTravelReviewsBySorting(); // 검색 메서드 실행
};

// 스크롤을 감지하여 데이터 로드하는 함수
const loadMore = () => {
  // 이미 로딩 중이거나 데이터가 더 이상 없으면 아무것도 하지 않음
  if (loading.value || !hasMore.value) return;
  getTravelReviewsBySorting();
};

// 처음 데이터 불러오기
onMounted(async () => {
  await getTravelReviewsBySorting();
});
</script>

<template>
  <div style="height: calc(100vh - 66px); display: flex; align-items: center">
    <div style="width: 1000px; margin: 0 auto; height: 100%; display: flex">
      <div class="left" style="width: 30%">
        <PlanReviewSide
          class="selectOption"
          @update-sort="updateSort"
          @update-size="updateSize"
        />
      </div>
      <div class="right" style="width: 70%">
        <div style="height: 100%; display: flex; flex-direction: column">
          <h1>여기 후기 게시판으로 할거임</h1>
          <div
            class="CardTravelPlan"
            v-infinite-scroll="loadMore"
            :scroll-distance="100"
            style="overflow-y: auto; height: 100%"
          >
            <div
              v-for="review in travelreviews"
              :key="review.id"
              class="card"
            ></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.button {
  display: flex;
  justify-content: end;
  margin-top: 81px;
  padding: 0px 8px 8px 50px;
}
.left {
  border: 1px solid whitesmoke;
  height: calc(100vh - 82px);
  padding: 20px 10px;
  margin: 10px 5px;
}
.selectOption {
  margin-top: 50px;
}
.right {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 66px); /* 부모 컨테이너의 높이 */
  justify-content: flex-start; /* 위에서부터 순서대로 배치 */
}

.CardTravelPlan {
  height: fit-content;
  margin-top: 10px;
  display: flex;
  justify-content: center; /* 가로 중심 */
  align-items: center; /* 세로 중심 */
}

.pagination-group {
  height: fit-content;
  margin-top: 25px; /* 아래로 고정 */
  padding-bottom: 20px; /* 하단에 여백 */
  display: flex;
  justify-content: center;
}
</style>
