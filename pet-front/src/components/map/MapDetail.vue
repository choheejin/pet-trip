<script setup>
import { defineProps, ref } from "vue";

const selectAttraction = ref(false);

defineProps(["attraction"]);
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
        <div
          class="button-cart"
          @click="$emit('selectAttraction', 'attraction.id')"
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
              d="M12 4.5v15m7.5-7.5h-15"
            />
          </svg>
          <span>여행경로 추가</span>
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
}

.button-group > .button-cart {
  display: flex;
  gap: 0.35rem;
  cursor: pointer;
  width: 50%;
  border: 1px solid;
  border-radius: 0.35rem;
  padding: 0.25rem 0.5rem;
}

.button-group > .button-cart > svg {
  width: 1.5rem;
}
</style>
