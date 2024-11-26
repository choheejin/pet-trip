<script setup>
import { onMounted, ref } from "vue";
import { useAuthStore } from "@/stores/user";
import LoginModal from "@/components/layout/LoginModal.vue";
import SignupModal from "@/components/layout/SignupModal.vue";
import myPageApi from "@/api/myPageApi.js";
import router from "@/router/index.js";

const userStore = useAuthStore();
const img = ref("/icon.png");
const userImg = ref(null);
const userInfo = ref([]);

const logout = () => {
  userStore.logout();
  userImg.value = img.value;
  router.push({ name: "main" });
};

// 사용자 프로필 이미지
const getUserImage = async () => {
  const { data } = await myPageApi.get("/user/info", {});
  userInfo.value = data;
  if (userInfo.value.image !== null) {
    // userInfo.value.image 경로가 "profile/ssafy1.png" 형태라면
    userImg.value =
      "http://localhost:8080/pet/profile/" +
      userInfo.value.image.split("/").pop();
  } else {
    userImg.value = img.value;
  }
  // console.log("사용자 정보 출력하기 : ", userInfo.value);
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

// 로그인 했을 때 다시 프로필 이미지 로드
const handleLogin = async () => {
  // 로그인된 이후에 프로필 이미지 업데이트
  getUserImage();
};

// 초기 이미지 로드
onMounted(() => {
  if (userStore.token) {
    getUserImage();
  }
});
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
          <li class="nav-item">
            <a class="nav-link" href="/review">후기</a>
          </li>
        </ul>

        <!-- 오른쪽 메뉴 -->
        <ul class="navbar-nav">
          <template v-if="userStore.token">
            <li class="nav-item">
              <a class="nav-link" href="/mypage">마이페이지</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#" @click.prevent="logout">로그아웃</a>
            </li>
            <li class="nav-item">
              <!-- 프로필 이미지 누르면 마이페이지로 이동 -->
              <router-link to="/mypage">
                <img :src="userImg" alt="Profile" class="profile-image" />
              </router-link>
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
  <LoginModal
    v-if="isLoginModalVisible"
    @close="closeModal"
    @loginSuccess="handleLogin"
  />
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
  font-size: 3rem;
}

.navbar-nav {
  display: flex;
  justify-content: center;
}

.navbar-nav .nav-link {
  margin: 0 10px;
  font-size: 1rem;

  color: #333;
}

.navbar-nav .nav-link:hover {
  color: #0056b3;
}
.navbar-nav.me-auto .nav-link:hover {
  color: #000000;
  background-color: rgb(254, 250, 224);
  border-radius: 5px;
  transition: background-color 0.3s ease;
}

.profile-image {
  display: flex;
  justify-content: center;
  border: 1px solid black;
  height: 40px;
  width: 40px;
  border-radius: 50%;
  object-fit: cover;
  cursor: pointer;
}
.profile-image:hover {
  box-shadow: 5px 5px 20px rgba(0, 0, 0, 0.5);
}
.container-fluid {
  padding-left: 15px;
  padding-right: 15px;
  max-width: 1000px;
  margin: 0 auto;
}
</style>
