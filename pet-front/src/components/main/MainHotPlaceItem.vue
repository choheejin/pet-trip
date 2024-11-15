<script setup>
defineProps({
  item: {
    type: Object,
    required: true,
  },
  type: {
    type: String,
    required: true,
  },
});
</script>

<template>
  <div class="item">
    <p v-if="type === 'place'">
      <strong>{{ item.name }}</strong> - 방문수: {{ item.cnt }}
    </p>
    <p v-else-if="type === 'travel'">
      <strong>{{ item.title }}</strong> - 즐겨찾기수: {{ item.favorite_cnt }}
    </p>
  </div>
</template>

<style scoped>
.item {
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
}
strong {
  color: #333;
}
</style>
