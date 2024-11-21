<script setup>
import { ref } from "vue";

const emit = defineEmits(["handleType"]);

const selectType = ref({
  id: 0,
  name: "",
  svg: "",
});

const setSelectType = (type) => {
  if (type.id === selectType.value.id) {
    selectType.value = { id: 0, name: "", svg: "" };
  } else {
    selectType.value = type;
  }

  emit("handleType", selectType.value.id);
  console.log(selectType);
};

const types = [
  {
    id: 39,
    name: "음식점",
    svg: `<i style="width: 20px; height: 20px; color: rgb(249 115 22);" class="fa-solid fa-utensils"></i>`,
  },
  {
    id: 38,
    name: "쇼핑",
    svg: `<i style="width: 20px; height: 20px; color: rgb(245 158 11);" class="fa-solid fa-basket-shopping"></i>`,
  },
  {
    id: 12,
    name: "관광지",
    svg: `<i style="font-size:20px; color:rgb(16 185 129);"  class="fa-solid fa-campground"></i>`,
  },
  {
    id: 14,
    name: "문화시설",
    svg: `<i style="font-size:20px; color:rgb(139 92 246);" class="fa-solid fa-building"></i>`,
  },
  {
    id: 15,
    name: "행사/공연/축제",
    svg: `<i style="font-size:20px;  color:rgb(139 92 246);" class="fa-solid fa-guitar"></i>`,
  },
  {
    id: 25,
    name: "여행코스",
    svg: `<i style="font-size:20px; color:rgb(59 130 246);" class="fa-solid fa-plane"></i>`,
  },
  {
    id: 28,
    name: "레포츠",
    svg: `<i style="font-size:20px; color: rgb(16 185 129);" class="fa-solid fa-person-skiing-nordic"></i>`,
  },
  {
    id: 32,
    name: "숙박",
    svg: `<i  style="font-size:20px; color: rgb(236 72 153);"  class="fa-solid fa-hotel"></i>`,
  },
];
</script>

<template>
  <div class="type-container">
    <div class="type-group" v-for="type in types" @click="setSelectType(type)">
      <div v-html="type.svg"></div>

      <div class="name" :class="{ 'name-color': selectType.id == type.id }">
        {{ type.name }}
      </div>
    </div>
  </div>
</template>

<style scoped>
.type-container {
  display: flex;
  position: absolute;
  top: 10px;
  left: 10px;
  gap: 0.5rem;
  z-index: 1;
}
.type-group {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.5rem 0.75rem;
  height: 38px;
  width: fit-content;
  background-color: white;
  border: 1px solid rgb(212 212 212);
  border-radius: 1.5rem;
  box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
  user-select: none;
}
.type-group > .name {
  min-width: fit-content;
  font-weight: 600;
}

.name-color {
  color: rgb(59 130 246);
}
</style>
