<script setup></script>

<template>
  <div class="sidebar">
    <!-- <h1>오른쪽입니다!~~</h1> -->
    <!-- 탭 버튼들 -->
    <div class="tab-group">
      <router-link
        to="/post-travel-plan"
        class="tab-button"
        active-class="active-tab"
      >
        <i class="fa-solid fa-pencil"></i> 게시글
      </router-link>
      <router-link
        to="/like-attraction"
        class="tab-button"
        active-class="active-tab"
      >
        <i class="fa-solid fa-heart"></i> 관광지
      </router-link>
      <router-link
        to="/like-travel-plan"
        class="tab-button"
        active-class="active-tab"
      >
        <i class="fa-solid fa-file"></i> 게시글
      </router-link>
    </div>
    <div class="select-router">
      <router-view></router-view>
    </div>
  </div>
</template>

<style scoped>
.sidebar {
  border: 1px solid rgb(247, 247, 247);
  border-radius: 15px;
  height: calc(100vh - 82px);
  padding: 20px 10px;
  margin: 10px 5px;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.tab-group {
  display: flex;
  width: 100%;
}

.tab-button {
  border: 1px solid whitesmoke;
  display: block;
  width: 33.33%;
  text-align: center;
  padding: 10px;
  background-color: #dbdbdb;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
  color: #333;
  font-size: 20px;
  text-decoration: none;
  transition: background-color 0.3s;
}

.tab-button:hover {
  background-color: #e7eed1;
}

.active-tab {
  border: 1px solid #ccd5ae;
  background-color: #ccd5ae;
  color: white;
  box-shadow: 3px 0px 10px rgba(0, 0, 0, 0.4);
  z-index: 1;
}
</style>
