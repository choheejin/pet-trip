<script setup>
import axios from "axios";
import MapList from "@/components/map/MapList.vue";
import MapDetail from "@/components/map/MapDetail.vue";
import MapCart from "@/components/map/MapCart.vue";
import { ref, computed, watch, onMounted } from "vue";
import { useMainSelectStore } from "@/stores/mainselect.js";
import attractionApi from "@/api/attractionApi";
import { useCartStore } from "@/stores/cart";
import { useSidoStore } from "@/stores/sido";

/* 전체적으로 자주 쓰이는 값 */
const attractions = ref([]); // 리스트의 값
const selectAttraction = ref(null); // 상세 보기의 값

/* ================상세보기START================ */
const showDetail = ref(false);

const setShowDetail = (isShowed) => {
  showDetail.value = isShowed;
};

// 특정 여행장소 클릭
const selected = (attraction) => {
  setShowDetail(true);
  isCartShowed.value = false;
  selectAttraction.value = attraction;
};

/* ================상세보기END================*/

/* ==============여행경로관련START============== */
const cartStore = useCartStore();
const isCartShowed = ref(false); //  장바구니 보기

const setShowCart = (isShowed) => {
  isCartShowed.value = isShowed;
};

// 장바구니 버튼 클릭
const clickCart = () => {
  if (isCartShowed.value) {
    setShowCart(false);
  } else {
    setShowDetail(false);
    setShowCart(true);
  }
  console.log("카트 클릭");
};

// 장바구니 추가
const selectCartItem = (attraction) => {
  alert("상품이 추가 되었습니다");

  cartStore.attraction.push(attraction);

  const value = cartStore.attraction.map((value, idx) => ({
    ...value,
    idx: idx,
  }));

  cartStore.setAttraction(value);
};

/* ==============여행경로관련END============== */

/*==============검색관련START=============== */
const sidoStore = useSidoStore();
const gugunStore = ref([]);

const sido_code = ref("");
const gugun_code = ref("");
const content_type_id = ref("");
const title = ref("");

const dogsize = ref(""); // 강아지 크기

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

const resetGugun = () => {
  gugun_code.value = "";
};

// `sido_code` 또는 `dogsize`가 변경될 때마다 호출
watch([sido_code, dogsize, gugun_code], () => {
  // `sido_code`가 변경되었을 때는 getAttractions() 호출
  if (sido_code.value) {
    getAttractions();
    getGugunCode();
  }
  // `dogsize`가 변경되었을 때는 getAttractionsBySize() 호출
  if (dogsize.value) {
    getAttractionsBySize();
  }
});

// 검색하기
const getAttractions = async () => {
  setShowDetail(false);

  const params = {
    sidoCode: sido_code.value,
    gugunCode: gugun_code.value,
    contentTypeId: content_type_id.value,
    title: title.value,
  };

  const { data, totalPage } = await attractionApi.get("/search", { params });

  attractions.value = data.data;
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

const getGugunCode = async () => {
  const { data } = await attractionApi.get(`/search/${sido_code.value}`);

  gugunStore.value = data;
  console.log(data);
};

/*===============검색관련END=============== */

/*===============지도 START =================*/
const mapDiv = ref(null);
const map = ref(null);
const polyline = ref(null);
const marker = ref([]);

// 평균값 계산
function getMean(values) {
  return values.reduce((sum, value) => sum + value, 0) / values.length;
}

// 표준편차 계산
function getStandardDeviation(values) {
  const mean = getMean(values);
  const variance =
    values.reduce((sum, value) => sum + Math.pow(value - mean, 2), 0) /
    values.length;
  return Math.sqrt(variance);
}

// 위도 및 경도 표준편차 계산
function calculateDeviation(latitudes, longitudes) {
  const latDeviation = getStandardDeviation(latitudes);
  const lngDeviation = getStandardDeviation(longitudes);

  return { latDeviation, lngDeviation };
}

// 줌 레벨 조정 기준
function shouldZoom(latitudes, longitudes) {
  const { latDeviation, lngDeviation } = calculateDeviation(
    latitudes,
    longitudes
  );
  console.log(
    "latDeviation:: " + latDeviation + ", lngDeviation:: " + lngDeviation
  );
  if (latDeviation < 0.03 || lngDeviation < 0.04) {
    return 13;
  }
  if (latDeviation < 0.1 || lngDeviation < 0.05) {
    return 10;
  }
  if (latDeviation < 0.3 || lngDeviation < 0.06) {
    return 9;
  }
  if (latDeviation < 0.4 || lngDeviation < 0.07) {
    return 8;
  }

  return 7;
}

watch(
  cartStore,
  ({ attraction }) => {
    console.log(attraction);

    if (polyline.value != null) {
      polyline.value.setMap(null);
      // polyline.value.setPath([]);
    }

    if (marker.value.length > 0) {
      marker.value.forEach((item) => item.setMap(null));
      marker.value = [];
    }

    polyline.value = new naver.maps.Polyline({
      map: map.value,
      path: attraction.map(
        (item) => new naver.maps.LatLng(item.latitude, item.longitude)
      ),
    });

    attraction.map((item, idx) =>
      marker.value.push(
        new naver.maps.Marker({
          position: new naver.maps.LatLng(item.latitude, item.longitude),
          map: map.value,
          icon: {
            content: [
              `<svg 
                    xmlns="http://www.w3.org/2000/svg" 
                    width="32" 
                    height="32" 
                    viewBox="0 0 64 64"
                >
                  <!-- 배경 마커 -->
                  <linearGradient id="gradient" x1="0" x2="0" y1="0" y2="1">
                    <stop offset="0%" stop-color="#00bfff" />
                    <stop offset="100%" stop-color="#00ff80" />
                  </linearGradient>
                  <path 
                    d="M32 2c-13 0-22 9-22 22 0 15 22 38 22 38s22-23 22-38c0-13-9-22-22-22z" 
                    fill="url(#gradient)" 
                    stroke="#007f5f" 
                    stroke-width="2"
                  />
                  <!-- 중심 원 -->
                  <circle cx="32" cy="24" r="11" fill="white" stroke="#007f5f" stroke-width="2" />
                  <!-- 숫자 텍스트 -->
                  <text 
                    x="31" 
                    y="28" 
                    text-anchor="middle" 
                    font-size="1.5rem" 
                    fill="black" 
                    font-family="Arial, sans-serif"
                    font-weight="bolder"
                    dominant-baseline="middle"
                  >
                ${idx + 1}
                  </text>
                </svg>
                `,
            ].join(""),
            size: new naver.maps.Size(22, 35),
            anchor: new naver.maps.Point(17, 32),
          },
        })
      )
    );

    if (attraction.length == 0) {
      map.value.setCenter(new naver.maps.LatLng(36.8057, 128.6243));
      map.value.setZoom(7);
    } else {
      const latitudes = attraction.map((coord) => coord.latitude);
      const longitudes = attraction.map((coord) => coord.longitude);

      const center_lat = getMean(latitudes);
      const center_lng = getMean(longitudes);

      const zoom = shouldZoom(latitudes, longitudes);

      map.value.setCenter(new naver.maps.LatLng(center_lat, center_lng));
      map.value.setZoom(zoom);
    }
  },
  {
    deep: true,
  }
);

onMounted(() => {
  map.value = new naver.maps.Map(mapDiv.value);
});

/*===============지도 END ================== */
</script>

<template>
  <div
    style="
      display: flex;
      height: calc(100vh - 66px);
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
          <select name="" id="" v-model="sido_code" @change="resetGugun">
            <option value="">전체</option>
            <option
              v-for="sido in sidoStore.sido"
              :id="sido.sido_code"
              :value="sido.sido_code"
            >
              {{ sido.sido_name }}
            </option>
          </select>
          <!-- TODO: 구군 이름 추가 -->
          <select name="" id="" v-model="gugun_code">
            <option value="">전체</option>
            <option v-for="gugun in gugunStore" :value="gugun" :id="gugun">
              {{ gugun }}
            </option>
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
          @add-cart-item="selectCartItem"
        />
      </div>

      <!-- 게시글 작성버튼 -->
      <div class="cart-div" v-if="isCartShowed == true">
        <MapCart @click-handler="setShowCart" />
      </div>

      <!-- 지도 -->
      <div ref="mapDiv" id="map" style="width: 100%; height: 100%"></div>
    </div>

    <div
      class="cart-group cart-hover"
      @click="clickCart"
      v-if="isCartShowed != true"
    >
      <div class="cart-button">
        <div v-if="cartStore.attraction.length != 0">
          <span>{{ cartStore.attraction.length }}</span>
        </div>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="1.5"
          stroke="currentColor"
          class="size-6"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10"
          />
        </svg>
      </div>
    </div>
  </div>
</template>

<style scoped>
.left-side {
  box-sizing: border-box;
  position: relative;
  min-width: 390px;
  width: 390px;
  height: 100%;
  display: flex;
  flex-direction: column;
  margin-bottom: 54px;
  box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
  padding-bottom: 64px;
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
  min-width: 390px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: white;
  padding-top: 10px;
}

.list-group {
  box-sizing: border-box;
}

.outer {
  overflow-y: auto;
  min-height: calc(100% - 200px);
}

.left-side > .list-group > .no-items {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.detail-side {
  background-color: white;
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
  height: 100%;
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
  box-sizing: border-box;
  width: fit-content;
  bottom: 10px;
  right: 10px;
  padding: 0.5rem;
  border-radius: 3rem;
  background-color: rgb(34 197 94);
  border: 2px solid rgb(34 197 94);
}

.cart-group > .cart-button > svg {
  stroke: white;
  width: 2rem;
}

.cart-group:hover {
  box-sizing: border-box;
  width: fit-content;
  border: 2px solid rgb(34 197 94);
  background-color: white;
}

.cart-group:hover > .cart-button > svg {
  stroke: rgb(34 197 94);
  width: 2rem;
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
  background-color: #ff0000;
  font-size: smaller;
  color: white;
  font-weight: bold;
  border-radius: 3rem;
}

.cart-div {
  position: absolute;
  margin: 20px 15px;
  width: calc(100% - 30px);
  height: calc(100% - 40px);
  z-index: 100;
  border-radius: 0.375rem;
  box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
  border: 1px solid rgba(209, 213, 219, 0.418);
  background-color: white;
}
</style>
