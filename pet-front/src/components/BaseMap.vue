<script setup>
import axios from "axios";
import { ref } from "vue";
import MapList from "@/components/MapList.vue";
import MapDetail from "@/components/MapDetail.vue";

// 검색관련
const sido_code = ref("");
const gugun_code = ref("");
const content_type_id = ref("");
const title = ref("");

const attractions = ref([]); // 리스트의 값
const selectAttraction = ref(null); // 상세 보기의 값

// 검색하기
const getAttractions = async () => {
  console.log("?");
  console.log(title.value);
  const { data } = await axios.get(
    `http://localhost:8080/pet/attraction/search?sidoCode=${sido_code.value}&gugunCode=${gugun_code.value}&contentTypeId=${content_type_id.value}&title=${title.value}`
  );
  console.log(data);
  attractions.value = data;
};

const showDetail = ref(false);

const setShowDetail = (isShowed) => {
  showDetail.value = isShowed;
};

const selected = (attraction) => {
  console.log("체크!");
  console.log(attraction);
  setShowDetail(true);
  selectAttraction.value = attraction;
};
</script>

<template>
  <div style="display: flex">
    <div class="left-side">
      <!-- 입력  -->
      <div>
        <select name="" id="" v-model="sido_code">
          <option value="">전체</option>
        </select>
        <select name="" id="" v-model="gugun_code">
          <option value="">전체</option>
        </select>
        <input type="text" v-model="title" />
        <button @click="getAttractions">검색</button>
      </div>

      <!-- 리스트 -->
      <div v-if="attractions.length <= 0">검색 결과가 존재하지 않습니다.</div>
      <div v-else>
        <MapList
          v-for="attraction in attractions"
          :attraction="attraction"
          :id="attraction.id"
          @click="selected(attraction)"
        />
      </div>
    </div>

    <!-- 상세조회 -->
    <div class="detail-side" v-if="showDetail">
      <MapDetail
        :attraction="selectAttraction"
        @set-show-detail="setShowDetail"
      />
    </div>

    <!-- 지도 -->
    <div class="right-side">지도 화면</div>
  </div>
</template>

<style scoped>
.left-side {
  width: 30%;
  height: 100%;
  padding: 0px 10px;
  background-color: bisque;
  display: flex;
  flex-direction: column;
}

.detail-side {
  width: 400px;
  background-color: rgb(231, 24, 24);
}

.right-side {
  width: 70%;
  background-color: aqua;
}
</style>
