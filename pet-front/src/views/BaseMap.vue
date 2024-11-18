<script setup>
import axios from "axios";
import { ref, computed, watch, onMounted } from "vue";
import MapList from "@/components/map/MapList.vue";
import MapDetail from "@/components/map/MapDetail.vue";
import { useMainSelectStore } from "@/stores/mainselect.js";

// 검색관련
const sido_code = ref("");
const gugun_code = ref("");
const content_type_id = ref("");
const title = ref("");

const attractions = ref([]); // 리스트의 값
const selectAttraction = ref(null); // 상세 보기의 값
const showDetail = ref(false);
const dogsize = ref(""); // 강아지 크기

const cartItems = ref([1]); // 여행경로 계획 예정 아이템들

const setShowDetail = (isShowed) => {
  showDetail.value = isShowed;
};

// 검색하기
const getAttractions = async () => {
  console.log("?");
  console.log(title.value);

  setShowDetail(false);

  const { data } = await axios.get(
    `http://localhost:8080/pet/attraction/search?sidoCode=${sido_code.value}&gugunCode=${gugun_code.value}&contentTypeId=${content_type_id.value}&title=${title.value}`
  );
  console.log(data);
  attractions.value = data;
};

// 반려견 사이즈로 검색하기
const getAttractionsBySize = async () => {
  console.log("사이즈 : ", dogsize.value);
  setShowDetail(false);

  const { data } = await axios.get(
    `http://localhost:8080/pet/attraction/detail?keyword=${dogsize.value}`
  );
  console.log(data);
  attractions.value = data.map((item) => item.attraction);
};

const selected = (attraction) => {
  console.log("체크!");
  console.log(attraction);
  setShowDetail(true);
  selectAttraction.value = attraction;
};

// 메인화면에서 선택된거
const mainSelectStore = useMainSelectStore();
const selectedFileName = computed(() => mainSelectStore.selectedSidoCode);
const selectedDogSize = computed(() => mainSelectStore.selectedDogSize);

onMounted(() => {
  // 초기 설정
  if (selectedFileName.value !== null) {
    sido_code.value = selectedFileName.value;
  }

  if (selectedDogSize.value !== null) {
    dogsize.value = selectedDogSize.value;
  }
});

// `sido_code` 또는 `dogsize`가 변경될 때마다 호출
watch([sido_code, dogsize], () => {
  // `sido_code`가 변경되었을 때는 getAttractions() 호출
  if (sido_code.value) {
    getAttractions();
  }
  // `dogsize`가 변경되었을 때는 getAttractionsBySize() 호출
  if (dogsize.value) {
    getAttractionsBySize();
  }
});
</script>

<template>
  <div
    style="
      display: flex;
      height: calc(100vh - 56px);
      position: relative;
      box-sizing: content-box;
    "
  >
    <!-- 왼쪽화면 -->
    <div class="left-side">
      <!-- 검색어입력  -->
      <div class="search-group">
        <div class="input-group">
          <input
            class="form-control"
            placeholder="검색어를 입력해주세요"
            type="text"
            v-model="title"
            v-on:keyup.enter="getAttractions"
          />
          <div class="input-group-append">
            <button class="btn btn-outline-secondary" @click="getAttractions">
              검색
            </button>
          </div>
        </div>

        <!-- 시군구 고르기 -->
        <div class="select-tag">
          <select name="" id="" v-model="sido_code">
            <option value="">전체</option>
          </select>
          <select name="" id="" v-model="gugun_code">
            <option value="">전체</option>
          </select>
        </div>
      </div>

      <!-- 리스트 -->
      <div class="outer">
        <div class="list-group">
          <div v-if="attractions.length <= 0" class="no-items">
            검색 결과가 존재하지 않습니다.
          </div>
          <div v-else>
            <MapList
              v-for="attraction in attractions"
              :attraction="attraction"
              :id="attraction.id"
              @click="selected(attraction)"
            />
          </div>
        </div>
      </div>

      <!-- 페이지네이션 -->
      <div class="pagination-group">
        <nav aria-label="Page navigation example">
          <ul class="pagination">
            <li class="page-item">
              <a class="page-link" href="#" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
                <span class="sr-only">Previous</span>
              </a>
            </li>
            <li class="page-item"><a class="page-link" href="#">1</a></li>
            <li class="page-item"><a class="page-link" href="#">2</a></li>
            <li class="page-item"><a class="page-link" href="#">3</a></li>
            <li class="page-item">
              <a class="page-link" href="#" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
                <span class="sr-only">Next</span>
              </a>
            </li>
          </ul>
        </nav>
      </div>
    </div>

    <!-- 오른쪽 화면 -->
    <div class="right-side">
      <!-- 상세조회 -->
      <div class="detail-side" v-if="showDetail">
        <MapDetail
          :attraction="selectAttraction"
          @set-show-detail="setShowDetail"
        />

        <!-- 게시글 작성버튼 -->

        <!-- 지도 -->
      </div>

      지도 화면
    </div>

    <div class="cart-group cart-hover">
      <div class="cart-button">
        <div v-if="cartItems.length != 0">
          <span>{{ cartItems.length }}</span>
        </div>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="1.5"
          stroke="white"
          class="size-6"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M15.75 10.5V6a3.75 3.75 0 1 0-7.5 0v4.5m11.356-1.993 1.263 12c.07.665-.45 1.243-1.119 1.243H4.25a1.125 1.125 0 0 1-1.12-1.243l1.264-12A1.125 1.125 0 0 1 5.513 7.5h12.974c.576 0 1.059.435 1.119 1.007ZM8.625 10.5a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm7.5 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Z"
          />
        </svg>
      </div>
    </div>
  </div>
</template>

<style scoped>
.left-side {
  position: relative;
  width: 390px;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  margin-bottom: 54px;
  box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
}

.left-side > .search-group {
  top: 0;
  position: sticky;
  background-color: white;
  padding: 20px 10px 0px 10px;
}

.left-side > .pagination-group {
  position: fixed;
  bottom: 0;
  width: 390px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: white;
  padding-top: 10px;
}

.list-group {
  box-sizing: border-box;
  padding-bottom: 66px;
}

.outer {
  min-height: calc(100% - 200px);
}

.left-side > .list-group > .no-items {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.detail-side {
  position: absolute;
  margin: 20px 15px;
  width: 400px;
  min-width: 384px;
  height: calc(100% - 40px);
  z-index: 100;
  overflow: auto;
}

.right-side {
  width: calc(100% - 380px);
  z-index: 0;
  position: relative;
}

.select-tag {
  padding: 10px 0px 8px 0px;
  display: flex;
  gap: 5px;
}

.cart-group {
  position: fixed;
  bottom: 10px;
  right: 10px;
  width: 3rem;
  padding: 0.5rem;
  border-radius: 3rem;
  background-color: rgb(34 197 94);
}

.cart-group:hover {
  border: 2px solid rgb(34 197 94);
  background-color: white;
}

.cart-group:hover > .cart-button > svg {
  stroke: rgb(34 197 94);
}

.cart-group > .cart-button {
  position: relative;
  cursor: pointer;
}

.cart-group > .cart-button > div {
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: -12px;
  right: -7px;
  width: 17px;
  height: 17px;
  min-width: fit-content;
  min-height: fit-content;
  background-color: red;
  font-size: smaller;
  color: white;
  font-weight: bold;
  border-radius: 3rem;
}
</style>
