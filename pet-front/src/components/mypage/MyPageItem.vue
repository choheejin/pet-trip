<template>
  <div class="sidebar">
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
  border: 1px solid transparent; /* 기본 테두리는 투명 */
  display: block;
  width: 33.33%;
  text-align: center;
  padding: 10px;
  background-color: rgba(154, 154, 154, 0.3); /* 흐린 배경 */
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
  color: #333;
  font-size: 20px;
  text-decoration: none;
  transition: background-color 0.3s, border-color 0.3s;
}

.tab-button:hover {
  background-color: #efefef;
}

.active-tab {
  background-color: #ffffff;
  color: #767c61;
  border: 1px solid #dddddd; /* 테두리 강조 */
  border-bottom: none; /* 아래쪽 테두리 제거 */
  z-index: 1;
}

.select-router {
  width: 100%;
  height: calc(100% - 42px);
  border: 1px solid #dddddd; /* 상단 제외 테두리 */
  border-top: none; /* 상단 테두리 제거 */
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.3); /* 부드러운 그림자 */
  z-index: 0;
}

.tab-button:not(.active-tab) {
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1); /* 흐린 가장자리 효과 */
}
</style>
