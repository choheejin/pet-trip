<script setup>
import { ref } from "vue";

// sortOptions 배열에서 아이콘을 FontAwesome 아이콘 객체로 바꿈
const sortOptions = [
  { id: 1, label: "오래된 순", value: "oldest", icon: "fa fa-clock" },
  { id: 2, label: "최신 순", value: "newest", icon: "fa fa-fire" },
  { id: 3, label: "조회 순", value: "views", icon: "fa fa-eye" },
  { id: 4, label: "좋아요 순", value: "likes", icon: "fa fa-heart" },
];

// emit 정의
const emit = defineEmits(["update-sort"]);

// 선택된 정렬 옵션
const selectedSort = ref("");

// 클릭 시 동작
const handleClick = (option) => {
  if (selectedSort.value === option.value) {
    selectedSort.value = "";
    emit("update-sort", ""); // 선택 해제 시
  } else {
    selectedSort.value = option.value;
    emit("update-sort", option.value); // 선택 시
  }
};
</script>

<template>
  <div class="sidebar">
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

.icon {
  margin-right: 8px;
}
</style>
