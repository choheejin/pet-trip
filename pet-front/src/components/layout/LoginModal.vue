<script setup>
import { ref } from "vue";
import userApi from "@/api/userApi";
import { useAuthStore } from "@/stores/user.js";
import ResetPasswordModal from "@/components/layout/ResetPasswordModal.vue"; // ResetPasswordModal 컴포넌트 가져오기
const userStore = useAuthStore();
import { defineEmits } from "vue";
import router from "@/router";

const showFindPasswordModal = ref(false); // 모달 상태
const resetToken = ref("");

const emit = defineEmits(["close", "login-success"]); // 이벤트를 부모 컴포넌트에 전달하기 위한 설정

const findPasswordForm = ref({
  user_id: "",
  email: "",
});

const loginForm = ref({
  user_id: "",
  password: "",
});

const login = async () => {
  try {
    const response = await userStore.login(loginForm.value);
    const {
      message,
      resetToken: token,
      "access-token": accessToken,
    } = response.data;

    if (token) {
      resetToken.value = token;
      localStorage.setItem("resetToken", token);
      alert(message);
      showResetPasswordModal.value = true;
    } else if (accessToken) {
      // console.log("로그인 시도");
      // console.log("로그인 폼 데이터:", loginForm.value); // 로그인 폼 데이터 콘솔 출력
      await userStore.login(loginForm.value); // 로그인 처리
      emit("login-success"); // 로그인 성공 이벤트 전달
      emit("close"); // 로그인 성공 후 모달 닫기
      // console.log("로그인 확인", useAuthStore());
    }
  } catch (error) {
    console.error("에러:", error);
    alert("로그인 실패");
  }
};

// 비밀번호 찾기 모달 열기
const openFindPasswordModal = () => {
  showFindPasswordModal.value = true;
};

// 비밀번호 찾기 모달 닫기
const closeFindPasswordModal = () => {
  showFindPasswordModal.value = false;
};

const findPassword = async () => {
  try {
    // userApi 사용
    const response = await userApi.patch(
      "/forgot-password",
      findPasswordForm.value
    );

    const { message } = response.data;
    alert(message); // 성공 메시지 출력
    emit("close"); // 모달 닫기
  } catch (error) {
    if (error.response?.status === 404) {
      alert("등록되지 않은 사용자입니다.");
    } else {
      alert("비밀번호 찾기 요청에 실패했습니다. 다시 시도해주세요.");
    }
    console.error("비밀번호 찾기 요청 오류:", error);
  }
};
</script>

<template>
  <div v-if="!showFindPasswordModal">
    <div class="modal">
      <div class="modal-content">
        <div class="modal-header">
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
            @click="$emit('close')"
          ></button>
          <img class="icon" src="/icon.png" />
          <h1 class="modal-title fs-5" id="loginModalLabel">로그인</h1>
        </div>
        <div class="modal-body">
          <form @submit.prevent="login">
            <div class="mb-3">
              <label for="user_id" class="form-label">아이디</label>
              <input
                v-model="loginForm.user_id"
                type="text"
                class="form-control"
                placeholder="아이디"
              />
            </div>
            <div class="mb-3">
              <label for="password" class="form-label">비밀번호</label>
              <input
                v-model="loginForm.password"
                type="password"
                class="form-control"
                placeholder="비밀번호"
              />
            </div>
            <div class="modal-footer">
              <button type="submit" class="btn btn-login">로그인</button>
              <button
                @click="openFindPasswordModal"
                type="button"
                class="btn btn-light"
              >
                비밀번호 찾기
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>

  <!-- 비밀번호 찾기 모달 -->
  <div v-else>
    <div class="modal">
      <div class="modal-content">
        <div class="modal-header">
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
            @click="closeFindPasswordModal"
          ></button>
          <h1 class="modal-title fs-5">비밀번호 찾기</h1>
        </div>
        <div class="modal-body">
          <form @submit.prevent="findPassword">
            <div class="mb-3">
              <label for="user_id" class="form-label">아이디</label>
              <input
                v-model="findPasswordForm.user_id"
                type="text"
                class="form-control"
                placeholder="아이디"
              />
            </div>
            <div class="mb-3">
              <label for="email" class="form-label">이메일</label>
              <input
                v-model="findPasswordForm.email"
                type="email"
                class="form-control"
                placeholder="이메일"
              />
            </div>
            <div class="modal-footer">
              <button type="submit" class="btn btn-login">비밀번호 찾기</button>
              <button
                type="button"
                class="btn btn-light"
                @click="closeFindPasswordModal"
              >
                취소
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 15px;
  width: 50%;
}
.modal-header {
  flex-direction: column;
}
.icon {
  width: 80px;
}
.modal-title {
  font-weight: bold;
}
.btn-login {
  background-color: #e0e5b6;
}
.btn-login:hover {
  background-color: #c0c49c;
}
</style>
