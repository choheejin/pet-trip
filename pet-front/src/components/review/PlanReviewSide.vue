<script setup>
import { ref } from "vue";
import { useAttrs } from "vue";

// sortOptions 배열에서 아이콘을 FontAwesome 아이콘 객체로 바꿈
const sortOptions = [
  { id: 1, label: "오래된 순", value: "oldest", icon: "fa fa-clock" },
  { id: 2, label: "최신 순", value: "date", icon: "fa fa-fire" },
  { id: 3, label: "조회 순", value: "view", icon: "fa fa-eye" },
  { id: 4, label: "좋아요 순", value: "favorite", icon: "fa fa-heart" },
];

const pickDogSizes = [
  { id: 1, label: "소형견", value: 1, icon: "fa-solid fa-dog" },
  { id: 2, label: "중형견", value: 2, icon: "fa-solid fa-paw" },
  { id: 3, label: "대형견", value: 3, icon: "fa-solid fa-bone" },
];

// emit 정의
const emit = defineEmits(["update-sort", "update-size"]);
const attrs = useAttrs();

// 선택된 정렬 옵션
const selectedSort = ref("oldest");
const selectedSize = ref(0);

// 클릭 시 동작 - 정렬 조건
const handleClick = (option) => {
  if (selectedSort.value === option.value) {
    selectedSort.value = "";
    emit("update-sort", ""); // 선택 해제 시
  } else {
    selectedSort.value = option.value;
    emit("update-sort", option.value); // 선택 시
  }
};

// 반려견 크기 선택
const sizeClick = (size) => {
  if (selectedSize.value === size.value) {
    selectedSize.value = "";
    emit("update-size", 0);
  } else {
    selectedSize.value = size.value;
    emit("update-size", size.value);
  }
};
</script>

<template>
  <div class="sidebar" v-bind="attrs">
    <div
      v-for="option in sortOptions"
      :key="option.id"
      :class="{ selected: selectedSort === option.value }"
      @click="handleClick(option)"
      class="sort-option"
    >
      <i :class="option.icon" class="icon"></i>
      {{ option.label }}
    </div>
  </div>
  <div class="sidebar">
    <div
      v-for="dogsize in pickDogSizes"
      :key="dogsize.id"
      :class="{ selected: selectedSize === dogsize.value }"
      @click="sizeClick(dogsize)"
      class="sort-option-size"
    >
      <i :class="dogsize.icon" class="icon"></i>
      {{ dogsize.label }}
    </div>
  </div>
</template>

<style scoped>
.sort-option {
  display: flex;
  align-items: center;
  padding: 10px;
  cursor: pointer;
}
.sort-option:hover {
  background-color: rgba(226, 240, 182, 0.5);
  border-radius: 15px;
}

.sort-option.selected {
  background-color: #e2f0b6;
  border-radius: 15px;
  color: #737e50;
  font-weight: 900;
}

.sort-option-size {
  display: flex;
  align-items: center;
  padding: 10px;
  cursor: pointer;
}
.sort-option-size:hover {
  background-color: #f4f8ba80;
  border-radius: 15px;
}

.sort-option-size.selected {
  background-color: #f7ff8680;
  border-radius: 15px;
  color: #2f302c80;
  font-weight: 900;
}
.icon {
  margin-right: 8px;
}
</style>
