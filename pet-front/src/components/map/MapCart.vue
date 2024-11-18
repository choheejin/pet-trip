<script setup>
import { computed, ref } from "vue";
import MapCartDetail from "./MapCartDetail.vue";
import { useCartStore } from "@/stores/cart";
import { useAuthStore } from "@/stores/user";
import mapApi from "@/api/mapApi";

const cartStore = useCartStore();
const authStore = useAuthStore();
const props = defineProps(["attractions"]);
const emit = defineEmits(["clickHandler"]);

const plan = ref({
  title: "",
  is_public: "1",
  description: "",
});

// 스토어 사용
cartStore.setAttraction(props.attractions);

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

const postPlan = () => {
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

  mapApi.post("", data, { headers: { accessToken: authStore.token } });
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

    <div class="cart-detail-container">
      <div class="cart-detail">
        <div class="title">장소명</div>
        <div class="addr1">주소</div>
      </div>
      <div class="cart-detail-item">
        <MapCartDetail
          v-for="(attraction, idx) in cartStore.attraction"
          @on-drop="onDrop(idx)"
          @on-drag-start="onDragStart(idx)"
          :key="attraction.id"
          :attraction="attraction"
          :idx="idx"
        />
      </div>
      <div class="cart-compute">
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

.input-group-private {
  display: flex;
  width: 100%;
  justify-content: baseline;
  gap: 1rem;
}

.input-group-private > label {
  width: 5rem;
  font-weight: bold;
}

.cart-container > .input-group > textarea {
  height: 100px;
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
  height: 300px;
  overflow-y: auto;
}

.cart-detail-container > .cart-compute {
  display: flex;
  margin-top: 1rem;
  justify-content: end;
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
  min-width: 20rem;
}
.cart-detail > .addr1 {
  text-align: center;
  font-weight: bold;
  width: 100%;
}
</style>
