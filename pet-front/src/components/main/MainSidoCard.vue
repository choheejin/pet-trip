<script setup>
import { ref, computed } from "vue";
import { useMainSelectStore } from "@/stores/mainselect.js";

import router from "@/router/index.js";

const images = [
  { sido_name: "서울", sido_code: 1 },
  { sido_name: "인천", sido_code: 2 },
  { sido_name: "대전", sido_code: 3 },
  { sido_name: "대구", sido_code: 4 },
  { sido_name: "광주", sido_code: 5 },
  { sido_name: "부산", sido_code: 6 },
  { sido_name: "울산", sido_code: 7 },
  { sido_name: "세종특별자치시", sido_code: 8 },
  { sido_name: "경기도", sido_code: 31 },
  { sido_name: "강원특별자치도", sido_code: 32 },
  { sido_name: "충청북도", sido_code: 33 },
  { sido_name: "충청남도", sido_code: 34 },
  { sido_name: "경상북도", sido_code: 35 },
  { sido_name: "경상남도", sido_code: 36 },
  { sido_name: "전북특별자치도", sido_code: 37 },
  { sido_name: "전라남도", sido_code: 38 },
  { sido_name: "제주도", sido_code: 39 },
];

const mainSelectStore = useMainSelectStore();
const currentIndex = ref(0);
const itemsPerPage = 3;

const currentGroup = computed(() => {
  const start = currentIndex.value;
  const end = start + itemsPerPage;
  const group = images.slice(start, end);

  // 빈 카드 추가
  while (group.length < itemsPerPage) {
    group.push({ sido_name: "", sido_code: 0 }); // 빈 객체로 채움
  }

  return group;
});

function getImagePath(image) {
  return `/src/assets/sido/${image.sido_code}.jpg`;
}

function prevGroup() {
  if (currentIndex.value - itemsPerPage >= 0) {
    currentIndex.value -= itemsPerPage;
  }
}

function nextGroup() {
  if (currentIndex.value + itemsPerPage < images.length) {
    currentIndex.value += itemsPerPage;
  }
}

function sendFileName(image) {
  mainSelectStore.setSidoCode(image.sido_code);
  router.push({ name: "BaseMap" });
}
</script>

<template>
  <div class="main-sido-card">
    <div class="arrow left">
      <button @click="prevGroup" :disabled="currentIndex.value === 0">
        &#9664;
      </button>
    </div>

    <div class="card-container">
      <div>
        <h1 class="title">지역별 관광지</h1>
      </div>
      <transition-group name="slide" tag="div" class="cards">
        <div
          v-for="(image, index) in currentGroup"
          :key="image.sido_code"
          class="card"
          @click="sendFileName(image)"
          :class="{ 'empty-card': image.sido_name === '' }"
        >
          <img v-if="image.sido_name" :src="getImagePath(image)" :alt="image.sido_name" />
          <div v-if="image.sido_name" class="sido-name">{{ image.sido_name }}</div>
    </div>
    </transition-group>

  </div>

    <div class="arrow right">
      <button
        @click="nextGroup"
        :disabled="currentIndex.value + itemsPerPage >= images.length"
      >
        &#9654;
      </button>
    </div>
  </div>
</template>

<style scoped>
.main-sido-card {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.arrow {
  background-color: transparent;
  border: none;
  font-size: 24px;
  cursor: pointer;
  text-align: center;
  user-select: none;
  transition: transform 0.2s ease;
}

.arrow:hover {
  color: #ccd5ae;
}

.arrow.left {
  margin-top: 50px;
}

.arrow.right {
  margin-top: 50px;
}

.card-container {
  overflow: hidden;
  width: 880px; /* Adjust width based on itemsPerPage and card width */
  padding: 0 40px; /* 화살표와 간격을 만들기 위한 여백 추가 */
}

.cards {
  display: flex;
  gap: 16px;
  transition: transform 0.5s ease-in-out;
}

.card {
  position: relative;
  width: 200px;
  height: 300px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  cursor: pointer;
  visibility: visible;
}

.card.empty-card {
  visibility: hidden;
  pointer-events: none;
}

.card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(0, 0, 0, 0.1), transparent);
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 10px rgba(0, 0, 0, 0.15);
}

.card .sido-name {
  position: absolute;
  top: 10px;
  left: 10px;
  color: white;
  font-size: 20px;
  font-weight: bold;
  text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.8);
}

.card img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.slide-enter-active,
.slide-leave-active {
  transition: transform 0.02s ease;
}

.slide-enter-from {
  transform: translateX(100%);
}

.slide-leave-to {
  transform: translateX(-100%);
}

.title {
  font-family: "Franklin Gothic Medium", "Arial Narrow", Arial, sans-serif;
  font-weight: bold;
}
</style>
