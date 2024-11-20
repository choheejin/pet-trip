<script setup>
import MyPageSide from "@/components/mypage/MyPageSide.vue";
import MyPageItem from "@/components/mypage/MyPageItem.vue";
</script>

<template>
  <div style="height: calc(100vh - 66px); display: flex; align-items: center">
    <div style="width: 1000px; margin: 0 auto; height: 100%; display: flex">
      <div class="left" style="width: 30%">
        <MyPageSide />
      </div>
      <div class="right" style="width: 70%">
        <MyPageItem />
      </div>
    </div>
  </div>
</template>

<style scoped></style>
