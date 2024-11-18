<script setup>
import { ref } from "vue";
import { useMenuStore } from "@/stores/menu";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import LoginModal from "@/components/layout/LoginModal.vue";
import SignupModal from "@/components/layout/SignupModal.vue";

const userStore = useAuthStore();
const menuStore = useMenuStore();
const { isLoggedIn } = storeToRefs(menuStore);

const logout = () => {
  userStore.logout();
};

// 모달 상태 관리
const isLoginModalVisible = ref(false);
const isSignupModalVisible = ref(false);

const showLoginModal = () => {
  isLoginModalVisible.value = true;
};
const showSignUpModal = () => {
  console.log("회원가입 모달 열기"); // 디버깅 로그
  isSignupModalVisible.value = true;
};

const closeModal = () => {
  isLoginModalVisible.value = false; // 로그인 성공 후 모달 닫기
  isSignupModalVisible.value = false;
};
</script>
<template>
  <nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
      <!-- 로고 및 토글 버튼 -->
      <a class="navbar-brand" href="/"
        ><img class="logo" src="@/assets/logo.png"
      /></a>

      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarNav"
        aria-controls="navbarNav"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>

      <!-- 메뉴 리스트 -->
      <div class="collapse navbar-collapse" id="navbarNav">
        <!-- 왼쪽 메뉴 -->
        <ul class="navbar-nav me-auto">
          <li class="nav-item">
            <a class="nav-link" href="/board">게시판</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/map">지도</a>
          </li>
        </ul>

        <!-- 오른쪽 메뉴 -->
        <ul class="navbar-nav">
          <template v-if="isLoggedIn">
            <li class="nav-item">
              <a class="nav-link" href="/mypage">마이페이지</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#" @click.prevent="logout">로그아웃</a>
            </li>
          </template>
          <template v-else>
            <li class="nav-item">
              <a class="nav-link" href="#" @click.prevent="showSignUpModal"
                >회원가입</a
              >
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#" @click.prevent="showLoginModal"
                >로그인</a
              >
            </li>
          </template>
        </ul>
      </div>
    </div>
  </nav>

  <!-- 모달 컴포넌트 삽입 -->
  <LoginModal v-if="isLoginModalVisible" @close="closeModal" />
  <SignupModal v-if="isSignupModalVisible" @close="closeModal" />
</template>

<style scoped>
.navbar {
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.logo {
  height: 40px;
  width: auto;
}
.navbar-collapse {
  font-weight: bold;
  font-size: 1.25rem;
}
.navbar-nav .nav-link {
  margin: 0 10px; /* 메뉴 항목 사이 여백 */
  font-size: 1rem;
  color: #333;
}

.navbar-nav .nav-link:hover {
  color: #0056b3;
}
.navbar-nav.me-auto .nav-link:hover {
  color: #000000; /* 초록색으로 변경 */
  background-color: rgb(254, 250, 224); /* 배경 색상 추가 */
  border-radius: 5px; /* 약간 둥근 효과 */
  transition: background-color 0.3s ease; /* 부드러운 전환 */
}

.container-fluid {
  padding-left: 15px;
  padding-right: 15px;
  max-width: 1000px;
  margin: 0 auto;
}

.navbar-toggler {
  border: none; /* 토글 버튼 테두리 제거 */
}
</style>
