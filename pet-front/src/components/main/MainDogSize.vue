<script setup>
import { ref } from "vue";
import router from "@/router/index";
import { useMainSelectStore } from "@/stores/mainselect";
const selectedSize = ref(""); // 선택된 강아지 크기를 추적
const mainSelectStore = useMainSelectStore();

const sizes = [
  { label: "소형견", iconSrc: "/src/assets/small.png", color: "white" },
  { label: "중형견", iconSrc: "/src/assets/medium.png", color: "white" },
  { label: "대형견", iconSrc: "/src/assets/large.png", color: "white" },
];
function selectSize(size) {
  console.log("선택된 강아지 : ", size);
  selectedSize.value = size;
  mainSelectStore.setDogSize(size);
  router.push({ name: "BaseMap" });
}
</script>

<template>
  <div>
    <div class="buttons">
      <button
        v-for="size in sizes"
        :key="size.label"
        :class="['size-btn', selectedSize === size.label ? 'selected' : '']"
        @click="selectSize(size.label)"
      >
        <img :src="size.iconSrc" alt="아이콘" class="size-icon" />
        {{ size.label }}
      </button>
    </div>
  </div>
</template>

<style scoped>
.buttons {
  height: 100%;
  display: flex;
  gap: 15px;
  justify-content: center;
}

.size-btn {
  border: solid 2px #ccd5ae;
  border-radius: 15px;
  font-weight: bold;
  font-size: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 198px;
  padding: 5px 20px;
  background-color: white;
  cursor: pointer;
  transition: background-color 0.3s ease, opacity 0.3s ease;
}

.size-btn.selected {
  background-color: #ccd5ae;
  opacity: 1;
}

.size-btn:hover {
  background-color: #ccd5ae;
  opacity: 0.8;
}

.size-icon {
  margin-right: 15px;
  width: 35px;
  height: 35px;
}
</style>
