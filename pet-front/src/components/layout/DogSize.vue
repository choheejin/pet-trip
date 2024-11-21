<!-- <script setup>
defineProps(["size"]);
</script>

<template>
  <div class="buttons">
    <button
      v-for="size in sizes"
      :key="size.label"
      :style="{ width: size.width, 'font-size': size.font }"
      :class="['size-btn', selectedSize === size.label ? 'selected' : '']"
      @click="selectSize(size.label)"
    >
      <img :src="size.iconSrc" alt="아이콘" class="size-icon" />
      {{ size.label }}
    </button>
  </div>
</template>

<style scoped>
.buttons {
  height: 100%;
  display: flex;
  gap: 15px;
  justify-content: center;
}

.size-btn {
  border: solid 2px #ccd5ae;
  border-radius: 15px;
  font-weight: bold;
  font-size: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 198px;
  padding: 5px 20px;
  background-color: white;
  cursor: pointer;
  transition: background-color 0.3s ease, opacity 0.3s ease;
}

.size-btn.selected {
  background-color: #ccd5ae;
  opacity: 1;
}

.size-btn:hover {
  background-color: #ccd5ae;
  opacity: 0.8;
}

.size-icon {
  margin-right: 15px;
  width: 35px;
  height: 35px;
}
</style> -->
