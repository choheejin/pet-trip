<script setup>
import { useRoute } from "vue-router";
import { onMounted, ref } from "vue";
import reviewApi from "@/api/reviewApi.js";
import { useAuthStore } from "@/stores/user.js";

const defaultImg = "http://localhost:8080/pet/reviews/default.jpg";

// useRoute()를 이용하여 라우트 객체를 가져오고, plan_id를 추출합니다.
const route = useRoute();
const id = ref(route.query.id);
const review = ref(null);
const reviewImages = ref([]);
const users = useAuthStore();
const isLiked = ref(false); // 좋아요 상태 초기화
const loading = ref(false); // 로딩 중 상태
const hasMore = ref(true); // 추가 데이터가 있는지 여부

// 좋아요 확인하기
const getLiked = async () => {
  // 로그인 되어있을 때 좋아요 표시 여부 확인
  const { like } = await reviewApi.get(`/like/user`, {
    params: {
      review_id: review.value.id,
    },
  });
  isLiked.value = like;
};

// 리뷰 데이터 가져오기
const getReview = async () => {
  const { data } = await reviewApi.get(`/detail/${id.value}`);
  review.value = data;

  console.log(
    "리뷰 데이터 : ",
    review.value,
    " | 좋아요 여부 : ",
    isLiked.value
  );
};

// 리뷰 이미지 가져오기
const getReviewImages = async () => {
  const { data } = await reviewApi.get(`/detail/${id.value}/images`);
  reviewImages.value = data;
  console.log("리뷰 이미지들 : ", reviewImages.value);
};

// 좋아요 추가하기
const addLike = async (reviewId) => {
  try {
    const { data } = await reviewApi.post(
      `/like/${reviewId}`,
      {},
      {
        headers: {
          accessToken: users.token, // 사용자 토큰을 헤더에 포함
        },
      }
    );
    console.log("좋아요 추가 성공:", data);

    // 좋아요 추가 후 상태 반영
    isLiked.value = true;
    review.value.favorite_cnt += 1; // 좋아요 수 증가
  } catch (error) {
    console.error(
      "좋아요 추가 실패:",
      error.response ? error.response.data : error.message
    );
  }
};

// 스크롤을 감지하여 데이터 로드하는 함수
const loadMore = () => {
  // 이미 로딩 중이거나 데이터가 더 이상 없으면 아무것도 하지 않음
  if (loading.value || !hasMore.value) return;
  getReviewImages();
};

// 좋아요 취소하기
const deleteLike = async (reviewId) => {
  try {
    const { data } = await reviewApi.delete(`/like/${reviewId}`, {
      headers: {
        accessToken: users.token, // 사용자 토큰을 헤더에 포함
      },
    });
    console.log("좋아요 취소 성공:", data);

    // 좋아요 취소 후 상태 반영
    isLiked.value = false;
    review.value.favorite_cnt -= 1; // 좋아요 수 감소
  } catch (error) {
    console.error(
      "좋아요 취소 실패:",
      error.response ? error.response.data : error.message
    );
  }
};

// 컴포넌트 마운트 후 데이터 가져오기
onMounted(async () => {
  await getReview();
  if (users.token != null) {
    await getLiked();
  }
  await getReviewImages();
});
</script>

<template>
  <div v-if="review">
    <div class="write-box">
      <div class="head">
        <div class="left">
          <h1 class="title">{{ review.title }}</h1>
          <p class="date">작성일 : {{ review.created_at }}</p>

          <!-- 반려견 크기 뱃지 -->
          <div class="dog-badge">
            <button v-if="review.dog_size === 1" class="badge small">
              소형견
            </button>
            <button v-else-if="review.dog_size === 2" class="badge medium">
              중형견
            </button>
            <button v-else-if="review.dog_size === 3" class="badge large">
              대형견
            </button>
          </div>
        </div>
        <div class="right">
          <!-- 좋아요 및 조회수 -->
          <div class="stats">
            <div class="stats-item">
              <i class="fa fa-eye eye-icon"></i> {{ review.view_cnt }}
            </div>
            <div class="stats-item">
              <i class="fa fa-heart heart-icon"></i> {{ review.favorite_cnt }}
            </div>
          </div>
        </div>
      </div>

      <div class="body">
        <h2 class="content">{{ review.content }}</h2>
        <!-- 이미지 갤러리 -->
        <div
          class="image-gallery"
          v-infinite-scroll="loadMore"
          :scroll-distance="100"
          style="overflow-y: auto; height: 300px"
        >
          <div class="images" v-if="reviewImages.length > 0">
            <div
              v-for="(image, index) in reviewImages"
              :key="index"
              class="image-item"
            >
              <img
                :src="`http://localhost:8080/pet/reviews/${review.id}/${image}`"
                :alt="'Image ' + (index + 1)"
              />
            </div>
          </div>
        </div>
        <!-- 좋아요 버튼 -->
        <div class="like-container">
          <button
            class="like-button"
            :disabled="!users.token"
            @click="isLiked ? deleteLike(review.id) : addLike(review.id)"
          >
            <i
              :class="isLiked ? 'fa-solid' : 'fa-regular'"
              class="fa-heart"
              style="color: red; font-size: 24px"
            ></i>
            좋아요
          </button>
        </div>
      </div>
    </div>
  </div>
  <div v-else>
    <p>로딩 중...</p>
  </div>
</template>

<style scoped>
.write-box {
  border: 1px solid rgb(228, 228, 228);
  border-radius: 15px;
  height: calc(100vh - 82px);
  padding: 20px 10px;
  margin: 10px auto;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 986px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #fff;
}

.head {
  width: 80%;
  margin-bottom: 20px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.date {
  font-size: 14px;
  color: #888;
}

.dog-badge {
  display: flex;
  margin-top: 10px;
  gap: 10px;
}

.badge {
  padding: 5px 10px;
  font-size: 14px;
  border-radius: 20px;
  color: white;
  background-color: #4caf50;
  border: none;
  cursor: pointer;
}

.small {
  background-color: #f44336;
}

.medium {
  background-color: #ff9800;
}

.large {
  background-color: #2196f3;
}

.stats {
  display: flex;
  align-items: center;
  margin-top: 10px;
}
.stats-item {
  margin-right: 10px;
}

.like-container {
  margin-top: 20px;
  border: 1px solid #ff6f65;
  border-radius: 10px;
  padding: 3px 5px;
  display: flex;
  justify-content: center;
  width: 20%;
  margin: 20px auto;
}

.like-button {
  border: none;
  cursor: pointer;
  color: #f44336;
  font-size: 24px;
}

.body {
  width: 80%;
}

.content {
  font-size: 16px;
  line-height: 1.6;
  color: #333;
  border: 1px solid rgb(216, 216, 216);
  border-radius: 10px;
  padding: 10px;
  height: 200px;
}

.image-gallery {
  margin-top: 20px;
}

.images {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.image-item {
  position: relative;
  width: calc(33.33% - 10px);
  max-width: 300px;
}

.image-item img {
  width: 100%;
  height: auto;
  border-radius: 10px;
  object-fit: cover;
}

.eye-icon {
  color: rgb(135, 135, 253);
}

.heart-icon {
  color: rgb(255, 143, 143);
}
@media (max-width: 768px) {
  .image-item {
    width: calc(50% - 10px);
  }
}

@media (max-width: 480px) {
  .image-item {
    width: 100%;
  }
}
</style>
