<script setup>
import { computed, ref } from "vue";
import MapCartDetail from "./MapCartDetail.vue";
import { useCartStore } from "@/stores/cart";
import mapApi from "@/api/mapApi";

const cartStore = useCartStore();

const emit = defineEmits(["clickHandler"]);
const summary = ref({});
const isShowedSummary = ref(false);

const plan = ref({
  title: "",
  is_public: "1",
  description: "",
});

const draggedIdx = ref();
const emitHandler = () => {
  emit("clickHandler", false);
};

const onDragStart = (idx) => {
  console.log(idx);
  draggedIdx.value = idx;
};

const onDrop = (idx) => {
  if (draggedIdx.value == null) return;

  console.log(idx);
  const itemsCopy = [...cartStore.attraction];
  const [draggedItem] = itemsCopy.splice(draggedIdx.value, 1);
  itemsCopy.splice(idx, 0, draggedItem);
  cartStore.setAttraction(itemsCopy);
  draggedIdx.value = null;
};

const totalCnt = computed(() => {
  return cartStore.attraction.length;
});

const postPlan = async () => {
  const items = cartStore.attraction.map((item, idx) => {
    return {
      note: "내용없음",
      order: idx + 1,
      content_id: item.content_id,
    };
  });

  const data = {
    plan: plan.value,
    items: items,
  };

  await mapApi.post("", data).then(() => {
    alert("게시글 작성 완료");
    window.location.reload(true);
  });
};

const getRecommendRoute = async () => {
  const len = cartStore.attraction.length - 1;
  if (len == 0) return;
  const startString =
    cartStore.attraction[0].longitude + "," + cartStore.attraction[0].latitude;

  // 경유지 계산
  const waypointsString = cartStore.attraction
    .reduce((acc, curr, idx) => {
      if (idx != 0 && idx != cartStore.attraction.length - 1) {
        return acc + curr.longitude + "," + curr.latitude + "|";
      }
      return acc;
    }, "")
    .slice(0, -1);

  const goalString =
    cartStore.attraction[len].longitude +
    "," +
    cartStore.attraction[len].latitude;

  const params = {
    start: startString,
    goal: goalString,
    waypoints: waypointsString,
  };
  await mapApi.get("/naver-map", { params }).then((res) => {
    console.log(
      "전체 경로 거리: " + res.data.route.traoptimal[0].summary.distance
    );
    console.log(
      "전체 경로 소요 시간" + res.data.route.traoptimal[0].summary.duration
    );
    summary.value = res.data.route.traoptimal[0].summary;
    isShowedSummary.value = true;
  });
};
</script>

<template>
  <div class="cart-container">
    <div class="title">게시글 작성</div>
    <div class="input-group">
      <label>제목 </label
      ><input
        type="text"
        placeholder="제목을 입력하세요"
        v-model="plan.title"
      />
    </div>
    <div style="cursor: pointer" @click="emitHandler">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        stroke-width="1.5"
        stroke="currentColor"
        class="zoom-out"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M9 9V4.5M9 9H4.5M9 9 3.75 3.75M9 15v4.5M9 15H4.5M9 15l-5.25 5.25M15 9h4.5M15 9V4.5M15 9l5.25-5.25M15 15h4.5M15 15v4.5m0-4.5 5.25 5.25"
        />
      </svg>
    </div>
    <div class="input-group-detail">
      <div class="input-group-private">
        <label>공개 설정 </label>
        <div>
          <label for="option1">공개</label>
          <input type="radio" id="option1" v-model="plan.is_public" value="1" />
        </div>
        <div>
          <label for="option2">비공개</label>
          <input type="radio" id="option2" v-model="plan.is_public" value="0" />
        </div>
      </div>
      <div class="button" @click="getRecommendRoute">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="1.5"
          stroke="currentColor"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M3.75 3v11.25A2.25 2.25 0 0 0 6 16.5h2.25M3.75 3h-1.5m1.5 0h16.5m0 0h1.5m-1.5 0v11.25A2.25 2.25 0 0 1 18 16.5h-2.25m-7.5 0h7.5m-7.5 0-1 3m8.5-3 1 3m0 0 .5 1.5m-.5-1.5h-9.5m0 0-.5 1.5M9 11.25v1.5M12 9v3.75m3-6v6"
          />
        </svg>

        <button>실시간 교통 시간 분석</button>
      </div>
    </div>

    <div class="cart-detail-container">
      <div class="cart-detail">
        <div class="title">장소명</div>
        <div class="addr1">주소</div>
      </div>
      <div class="cart-detail-item">
        <MapCartDetail
          v-for="attraction in cartStore.attraction"
          @on-drop="onDrop(attraction.idx)"
          @on-drag-start="onDragStart(attraction.idx)"
          :key="attraction.idx"
          :attraction="attraction"
        />
      </div>
      <div class="cart-compute">
        <div v-if="isShowedSummary" class="summary">
          <div>
            <span>택시비: </span
            >{{ summary.taxiFare.toLocaleString("ko-KR") }}원
          </div>
          <div>
            <span>총 소요시간: </span
            >{{ Math.ceil((summary.duration / (1000 * 60)) % 60) }}분
          </div>
          <div>
            <span>총 거리: </span
            >{{ summary.distance.toLocaleString("ko-KR") }}m
          </div>
        </div>
        <div><span>총 장소 수: </span>{{ totalCnt }}</div>
      </div>
    </div>

    <div class="input-group">
      <textarea
        v-model="plan.description"
        placeholder="내용을 입력하세요"
      ></textarea>
    </div>

    <div><button @click="postPlan">게시글 작성</button></div>
  </div>
</template>

<style scoped>
.cart-container {
  padding: 3rem 8rem;
  width: 100%;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1rem;
}

.cart-container > .title {
  font-size: larger;
  font-weight: bold;
}

.cart-container > .input-group {
  display: flex;
  width: 100%;
}

.cart-container > .input-group > label {
  font-weight: bold;
  width: 6rem;
}

.cart-container > .input-group > input,
textarea {
  flex-grow: 1;
}

.input-group-detail {
  display: flex;
  width: 100%;
  justify-content: center;
  align-items: stretch;
}

.input-group-detail > .input-group-private {
  display: flex;
  width: 100%;
  align-items: center;
  gap: 1rem;
}

.input-group-detail > .input-group-private > label {
  width: 5rem;
  font-weight: bold;
}

.input-group-detail > .button {
  display: flex;
  justify-content: center;
  min-width: fit-content;
  padding: 0.5rem 1rem;
  background-color: rgb(16 185 129);
  border: 1px solid rgb(16 185 129);
  border-radius: 0.5rem;
  color: white;
}

.input-group-detail > .button:hover {
  display: flex;
  justify-content: center;
  min-width: fit-content;
  padding: 0.5rem 1rem;
  background-color: white;
  border: 1px solid rgb(16 185 129);
  border-radius: 0.5rem;
  color: rgb(16 185 129);
}

.input-group-detail > .button > svg {
  width: 30px;
}

.input-group-detail > .button > button {
  width: 100%;
}

.cart-container > .input-group > textarea {
  height: 8rem;
  overflow-y: auto;
}

.zoom-out {
  position: absolute;
  right: 10px;
  top: 10px;
  width: 2rem;
}

.cart-detail-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 100%;
  padding: 2rem 3rem;
  border: 1px solid rgba(209, 213, 219, 0.418);
}

.cart-detail-container > .cart-detail-item {
  height: 14rem;
  overflow-y: auto;
}

.cart-detail-container > .cart-compute {
  display: flex;
  flex-direction: column;
  margin-top: 1rem;
  justify-content: end;
}

.cart-detail-container > .cart-compute > .summary {
  display: flex;
  gap: 1rem;
}

.cart-detail-container > .cart-compute > div > span {
  font-weight: bold;
}

.cart-detail {
  display: flex;
  gap: 1.25rem;
  border-bottom: 1px solid rgba(209, 213, 219, 0.418);
  padding-bottom: 1rem;
}

.cart-detail > .title {
  text-align: center;
  font-weight: bold;
  min-width: 15rem;
}
.cart-detail > .addr1 {
  text-align: center;
  font-weight: bold;
  width: 100%;
}
</style>
