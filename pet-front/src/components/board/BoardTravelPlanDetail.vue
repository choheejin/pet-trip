<script setup>
import {ref} from "vue";
import {useRoute} from "vue-router";
import travelplanApi from "@/api/travelplanApi.js";

// 상세조회할 id
const route = useRoute();
const id = route.query.id;

const plan = ref([]);
const items = ref([]);

const getDetail = async () => {
  try {
    // id가 쿼리 파라미터로 전달되었으므로 해당 id를 사용해 API 호출
    const { data } = await travelplanApi.get(`/${id}`);

    // console.log("조회할 여행 계획 : ", data);

    // 응답 받은 데이터를 상태에 할당
    plan.value = data.plan;
    items.value = data.items;
    console.log("확인 : ", plan.value);
    console.log("여행지들 : ", items.value);

  } catch (error) {
    console.error("API 요청 중 오류 발생: ", error);
  }
};


getDetail();
</script>

<template>
  <div></div>
</template>

<style scoped>

</style>
