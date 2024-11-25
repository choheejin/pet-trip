<script setup>
import { useRoute } from 'vue-router';
import { onMounted, ref } from 'vue';
import reviewApi from "@/api/reviewApi.js";
import axios from "axios";
import {useAuthStore} from "@/stores/user.js";

// 기본 이미지 경로 설정
const defaultImg = 'http://localhost:8080/pet/default.jpg'; // 기본 이미지 경로

// useRoute()를 이용하여 라우트 객체를 가져오고, plan_id를 추출합니다.
const route = useRoute();
const id = ref(route.query.id);
const review = ref(null);
const reviewImages = ref([]);
const isReady = ref(false);
const users = useAuthStore();

// 리뷰 데이터 가져오기
const getReview = async () => {
  const {data} = await reviewApi.get(`/detail/${id.value}`);
  review.value = data;
};

// 리뷰 이미지 가져오기
const getReviewImages = async () => {
  const {data} = await reviewApi.get(`/detail/${id.value}/images`);
  reviewImages.value = data;
};

// 리뷰 좋아요 누르기
const addLike = async (reviewId) => {
  try {
    const { data } = await reviewApi.post(
      `/like/${reviewId}`,
      {}, // POST 요청 body는 빈 객체로 전달
      {
        headers: {
          accessToken: users.token, // 사용자 토큰을 헤더에 포함
        },
      }
    );
    console.log('좋아요 추가 성공:', data);
  } catch (error) {
    console.error('좋아요 추가 실패:', error.response ? error.response.data : error.message);
  }
};

// 리뷰 좋아요 취소하기
const deleteLike = async (reviewId) => {
  try {
    const { data } = await reviewApi.delete(`/like/${reviewId}`, {
      headers: {
        accessToken: users.token, // 사용자 토큰을 헤더에 포함
      },
    });
    console.log('좋아요 취소 성공:', data);
  } catch (error) {
    console.error('좋아요 취소 실패:', error.response ? error.response.data : error.message);
  }
};

// 컴포넌트 마운트 후 데이터 가져오기
onMounted(async () => {
  await getReview();
  await getReviewImages();
});
</script>

<template>
  <div v-if="review">
    <div class="write-box">
      <h1>{{ review.id }}</h1>
      <h2>{{ review.title }}</h2>
      <p>{{ review.content }}</p>
    </div>

    <!-- 이미지 갤러리 -->
    <div class="image-gallery">
      <div class="images">
        <div v-for="(image, index) in reviewImages" :key="index" class="image-item">
          <!-- 이미지 경로 설정 -->
          <img
            :src="`url(${
              review.stored_name
                ? 'http://localhost:8080/pet/reviews/' + review.id + '/' + review.stored_name
                : defaultImg
            })`"
            :alt="'Image ' + (index + 1)"
          />
          <p>{{ image.description || 'No description' }}</p>
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
  width: 986px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #fff;
}

h1, h2 {
  color: #333;
}

h3 {
  margin-top: 20px;
  color: #555;
}

/* 이미지 갤러리 스타일 */
.image-gallery {
  width: 100%;
  margin-top: 20px;
}

.images {
  display: flex;
  gap: 20px;
  overflow-x: auto; /* 가로로 스크롤 가능하도록 */
  padding-bottom: 10px;
}

.image-item {
  flex-shrink: 0; /* 크기가 고정되도록 */
  width: 300px;
  text-align: center;
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 10px;
  background-color: #f9f9f9;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.image-item img {
  width: 100%;
  height: auto;
  border-radius: 8px;
}

.image-item p {
  margin-top: 10px;
  color: #777;
  font-size: 0.9rem;
}

/* 스크롤 바 스타일 */
.images::-webkit-scrollbar {
  height: 8px;
}

.images::-webkit-scrollbar-thumb {
  background-color: #aaa;
  border-radius: 4px;
}

.images::-webkit-scrollbar-track {
  background-color: #f1f1f1;
}
</style>
