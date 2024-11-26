<script setup>
import { onMounted, ref } from "vue";
import reviewApi from "@/api/reviewApi";
import PlanReviewSide from "@/components/review/PlanReviewSide.vue";
import router from "@/router/index.js";

// 검색 결과
const travelreviews = ref([]);
const loading = ref(false); // 로딩 중 상태
const hasMore = ref(true); // 추가 데이터가 있는지 여부
const page = ref(1); // 현재 페이지 번호
// 기본 이미지
const defaultImg =
  "https://st3.depositphotos.com/8687452/13536/i/450/depositphotos_135362258-stock-photo-seoul-south-korea-nov-1.jpg";
const baseImgUrl = ref("");
baseImgUrl.value = `http://localhost:8080/pet/reviews/`;
// http://localhost:8080/pet/reviews/5/1_caption_20241125222540.jpg
// 정렬 조건과 강아지 크기
const order_by = ref("oldest"); // 정렬 조건
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
      travelreviews.value = data; // 기존 데이터에 새 데이터를 추가
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

// 상세조회
const reviewDetail = (id) => {
  // console.log("선택된 여행 후기 : ", review)
  router.push({ name: "ReviewDetail", query: { id: id } });
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
        <div
          class="card-container"
          v-infinite-scroll="loadMore"
          :scroll-distance="100"
          style="overflow-y: auto; height: 100%"
        >
          <div
            v-for="review in travelreviews"
            :key="review.id"
            class="card"
            @click="reviewDetail(review.id)"
          >
            <div
              class="card-img"
              :style="{
                backgroundImage: `url(${
                  review.stored_name
                    ? 'http://localhost:8080/pet/reviews/' +
                      review.id +
                      '/' +
                      review.stored_name
                    : defaultImg
                })`,
              }"
            ></div>
            <div class="stats">
              <i class="fa fa-eye eye-icon"></i> {{ review.view_cnt }}
              <i class="fa fa-heart heart-icon"></i> {{ review.favorite_cnt }}
            </div>
            <div class="card-title">
              <div>{{ review.title }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
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
  justify-content: center; /* 위에서부터 순서대로 배치 */
}

.card-container {
  padding: 50px 30px;
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 1rem;
  grid-row-gap: 3rem;
  align-items: center;
}

.card {
  height: 300px;
  /*border: 1px solid #ccd5aeca;*/
  border-radius: 10px;
  /* box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3); */
  cursor: pointer;
}
.card:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.card:hover .card-title {
  color: #fff;
  transform: translateY(-10px);
}
.stats {
  position: absolute;
  right: 10px;
  top: 5px;
  text-align: right;
  font-size: 15px;
  padding-top: 10px;
  color: white;
}

.eye-icon {
  color: rgb(135, 135, 253);
}

.heart-icon {
  color: rgb(255, 143, 143);
}
.card-img {
  position: relative;
  border-radius: 10px;
  overflow: hidden;
  height: 100%;
  background-size: cover;
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
</style>
