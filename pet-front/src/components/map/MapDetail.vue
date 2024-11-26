<script setup>
import attractionApi from "@/api/attractionApi";
import { defineProps, onMounted, ref } from "vue";

const props = defineProps(["attraction", "detailInfo", "dogInfo"]);

const debounce = (func, delay) => {
  let timer;
  return function (...args) {
    clearTimeout(timer);
    timer = setTimeout(() => {
      func(...args);
    }, delay);
  };
};

const handleLikeOrDisLike = debounce(() => {
  const params = {
    content_id: props.detailInfo.contentid,
  };

  if (!props.detailInfo.isLiked) {
    console.log("좋아요 등록 요청");
    attractionApi.post("/add-user-hotplace", null, { params }).then((res) => {
      if (res.status == 200) {
        props.detailInfo.isLiked = true;
      }
    });
  }

  if (props.detailInfo.isLiked) {
    console.log("좋아요 취소 요청");
    attractionApi.delete("/delete-user-hotplace", { params }).then((res) => {
      if (res.status == 200) {
        props.detailInfo.isLiked = false;
      }
    });
  }
}, 500);

const emit = defineEmits(["addCartItem"]);

const emitHandler = () => {
  console.log("경로 추가");
  emit("addCartItem", props.attraction);
};

onMounted(() => {});
</script>

<template>
  <div class="detail-container">
    <div class="img-container">
      <img
        v-if="attraction.first_image1"
        :src="attraction.first_image1"
        alt=""
      />
      <svg
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        stroke-width="1.5"
        stroke="white"
        class="svg-button"
        @click="$emit('setShowDetail', false)"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M6 18 18 6M6 6l12 12"
        />
      </svg>
    </div>
    <div class="text-container">
      <div class="text-title">{{ attraction.title }}</div>
      <div class="text-addr">{{ attraction.addr1 }}</div>

      <div class="button-group">
        <div class="button-cart" @click="emitHandler">
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
              d="M12 4.5v15m7.5-7.5h-15"
            />
          </svg>
          <span>여행경로 추가</span>
        </div>
        <div
          :class="{
            'button-heart-blank': detailInfo.isLiked,
            'button-heart': !detailInfo.isLiked,
          }"
          @click="handleLikeOrDisLike"
        >
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
              d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
            />
          </svg>
          <span>좋아요 등록</span>
        </div>
      </div>

      <div class="info-container">
        <!-- 전화 -->
        <div>
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
              d="M2.25 6.75c0 8.284 6.716 15 15 15h2.25a2.25 2.25 0 0 0 2.25-2.25v-1.372c0-.516-.351-.966-.852-1.091l-4.423-1.106c-.44-.11-.902.055-1.173.417l-.97 1.293c-.282.376-.769.542-1.21.38a12.035 12.035 0 0 1-7.143-7.143c-.162-.441.004-.928.38-1.21l1.293-.97c.363-.271.527-.734.417-1.173L6.963 3.102a1.125 1.125 0 0 0-1.091-.852H4.5A2.25 2.25 0 0 0 2.25 4.5v2.25Z"
            />
          </svg>
          <div>
            {{
              !detailInfo.infocenter
                ? "관리자 문의 부탁드립니다"
                : detailInfo.infocenter
            }}
          </div>
        </div>

        <!-- 쉬는 날  -->
        <div>
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
              d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 0 1 2.25-2.25h13.5A2.25 2.25 0 0 1 21 7.5v11.25m-18 0A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75m-18 0v-7.5A2.25 2.25 0 0 1 5.25 9h13.5A2.25 2.25 0 0 1 21 11.25v7.5m-9-6h.008v.008H12v-.008ZM12 15h.008v.008H12V15Zm0 2.25h.008v.008H12v-.008ZM9.75 15h.008v.008H9.75V15Zm0 2.25h.008v.008H9.75v-.008ZM7.5 15h.008v.008H7.5V15Zm0 2.25h.008v.008H7.5v-.008Zm6.75-4.5h.008v.008h-.008v-.008Zm0 2.25h.008v.008h-.008V15Zm0 2.25h.008v.008h-.008v-.008Zm2.25-4.5h.008v.008H16.5v-.008Zm0 2.25h.008v.008H16.5V15Z"
            />
          </svg>
          <div>
            {{
              !detailInfo.restdate
                ? "관리자 문의 부탁드립니다"
                : detailInfo.restdate
            }}
          </div>
        </div>

        <!-- 사용시간 -->
        <div>
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
              d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"
            />
          </svg>
          <div>
            {{
              !detailInfo.usetime
                ? "관리자 문의 부탁드립니다"
                : detailInfo.usetime
            }}
          </div>
        </div>

        <!-- 주차 -->
        <div>
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
              d="m11.25 11.25.041-.02a.75.75 0 0 1 1.063.852l-.708 2.836a.75.75 0 0 0 1.063.853l.041-.021M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Zm-9-3.75h.008v.008H12V8.25Z"
            />
          </svg>
          <div>
            {{
              !detailInfo.parking
                ? "관리자 문의 부탁드립니다"
                : "주차" + detailInfo.parking
            }}
          </div>
        </div>

        <div v-if="dogInfo">
          <span class="material-symbols-outlined"> pet_supplies </span>
          <div>
            <p>
              {{ dogInfo.acmpyTypeCd }}
            </p>
            <p>
              {{ dogInfo.etcAcmpyInfo }}
            </p>
            <p>{{ dogInfo.acmpyNeedMtr }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.detail-container {
  height: 100%;
  border-radius: 0.375rem;
  background-color: white;
  box-shadow: 0 1px 3px 0 rgb(0 0 0 / 0.1), 0 1px 2px -1px rgb(0 0 0 / 0.1);
  border: 1px solid rgba(209, 213, 219, 0.418);
}

img {
  width: 100%;
  height: 22rem;
  object-position: center;
  border-top-left-radius: 0.375rem;
  border-top-right-radius: 0.375rem;
}

.svg-button {
  position: absolute;
  right: 0;
  width: 2rem;
  height: 2rem;
  cursor: pointer;
  margin: 5px;
  mix-blend-mode: difference;
  filter: drop-shadow(0 20px 13px rgb(0 0 0 / 0.03))
    drop-shadow(0 8px 5px rgb(0 0 0 / 0.08));
}

.img-container {
  position: relative;
  min-height: 2.5rem;
}

.text-container {
  width: 100%;
  padding: 1rem 1.5rem;
}

.text-container > .text-title {
  font-weight: bold;
  font-size: large;
}

.text-container > .text-addr {
  color: rgb(120 113 108);
}

.button-group {
  padding-top: 10px;
  width: 100%;
  display: flex;
  justify-content: space-between;
}

.button-group > .button-cart {
  display: flex;
  gap: 0.35rem;
  cursor: pointer;
  width: 48%;
  color: rgb(113 113 122);
  border: 1px solid rgb(113 113 122);
  border-radius: 0.35rem;
  padding: 0.25rem 0.5rem;
}

.button-group > .button-cart > svg {
  width: 1.5rem;
  stroke: rgb(113 113 122);
}

.button-group > .button-cart:hover {
  background-color: rgb(113 113 122);
  color: white;
}

.button-group > .button-cart:hover > svg {
  stroke: white;
}

.button-group > .button-heart {
  display: flex;
  gap: 0.35rem;
  cursor: pointer;
  width: 48%;
  color: rgb(244 63 94);
  border: 1px solid rgb(244 63 94);
  border-radius: 0.35rem;
  padding: 0.25rem 0.5rem;
}

.button-group > .button-heart > svg {
  width: 1.5rem;
  stroke: rgb(244 63 94);
}

.button-group > .button-heart:hover {
  background-color: rgb(244 63 94);
  color: white;
}

.button-group > .button-heart:hover > svg {
  stroke: white;
}

.button-group > .button-heart-blank {
  display: flex;
  gap: 0.35rem;
  cursor: pointer;
  width: 48%;
  color: white;
  background-color: rgb(244 63 94);
  border: 1px solid white;
  border-radius: 0.35rem;
  padding: 0.25rem 0.5rem;
}

.button-group > .button-heart-blank > svg {
  width: 1.5rem;
  stroke: white;
}

.info-container {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
  border-top: 1px solid rgba(209, 213, 219, 0.418);
  margin-top: 1rem;
  padding-top: 0.5rem;
}

.info-container > div {
  display: flex;
  gap: 0.75rem;
  width: 100%;
  height: fit-content;
}

.info-container > div > svg {
  padding-top: 2px;
  max-width: 20px;
}

.info-container > div > span {
  padding-top: 2px;
  max-width: 20px;
}

.info-container > div > div > p {
  margin-bottom: 0;
}
</style>
