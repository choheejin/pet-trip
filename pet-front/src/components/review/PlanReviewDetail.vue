<script setup>
import { useRoute } from 'vue-router';
import { ref } from 'vue';

// useRoute()를 이용하여 라우트 객체를 가져오고, plan_id를 추출합니다.
const route = useRoute();
const planId = ref(route.params.plan_id);  // 동적 파라미터 plan_id를 받아옵니다.

// travel_reviews 데이터 (샘플 데이터로 가정)
const review = ref({
  review_id: 1,
  title: '환상적인 여행지!',
  content: '이곳은 정말 멋진 곳입니다. 자연 경관과 함께 즐길 수 있는 활동들이 많아서 좋았습니다.',
  rating: 5,
  created_at: '2024-11-24',
});

// review_images 데이터 (샘플 이미지 데이터로 가정)
const reviewImages = ref([
  { image_url: 'https://via.placeholder.com/600x400', description: '여행지 전경' },
  { image_url: 'https://via.placeholder.com/600x400', description: '맛있는 음식' },
]);
</script>

<template>
  <div style="display: flex; align-items: center; flex-direction: column;">
    <div class="write-box">
      <h1>여행 계획 ID: {{ planId }}</h1>
      <h2>{{ review.title }}</h2>
      <p><strong>작성일:</strong> {{ review.created_at }}</p>
      <p><strong>평점:</strong> {{ review.rating }} / 5</p>
      <p>{{ review.content }}</p>

      <!-- 이미지 갤러리 -->
      <div class="image-gallery">
        <h3>이미지 갤러리</h3>
        <div class="images">
          <div v-for="(image, index) in reviewImages" :key="index" class="image-item">
            <img :src="image.image_url" :alt="image.description" />
            <p>{{ image.description }}</p>
          </div>
        </div>
      </div>
    </div>
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

.image-gallery {
  width: 100%;
  margin-top: 20px;
}

.images {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.image-item {
  width: 300px;
  text-align: center;
}

.image-item img {
  width: 100%;
  border-radius: 8px;
}

.image-item p {
  margin-top: 10px;
  color: #777;
}
</style>
