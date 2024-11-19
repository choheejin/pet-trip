<script setup>
import { defineProps } from "vue";

// 부모로부터 전달받을 prop 정의
// props에 id에 해당하는 travelPlanitems의 첫 번째 관광지의 img 를 추가로 가지고 있게 만들기
const props = defineProps({
  travelplans: {
    type: Array,
    required: true,
  },
});

// travelplans 콘솔 출력
console.log(props.travelplans);
</script>

<template>
  <div>
    <h2>Travel Plans:</h2>
    <ul>
      <!-- travelplans 배열을 출력 -->
      <li v-for="(plan, index) in props.travelplans" :key="index">
        {{ plan }}
        <!-- plan의 내용을 출력 (여기서는 예시로 그대로 출력) -->
      </li>
    </ul>
  </div>
</template>

<style scoped></style>
