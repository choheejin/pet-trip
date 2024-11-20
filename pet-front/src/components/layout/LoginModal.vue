<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/user.js";
const userStore = useAuthStore();
import { defineEmits } from "vue";

const emit = defineEmits(["close", "login-success"]); // 이벤트를 부모 컴포넌트에 전달하기 위한 설정

const loginForm = ref({
  user_id: "",
  password: "",
});

const login = async () => {
  try {
    // console.log("로그인 시도");
    // console.log("로그인 폼 데이터:", loginForm.value); // 로그인 폼 데이터 콘솔 출력
    await userStore.login(loginForm.value); // 로그인 처리
    emit("login-success") // 로그인 성공 이벤트 전달
    emit("close"); // 로그인 성공 후 모달 닫기
  } catch (error) {
    console.error("에러:", error);
    alert("로그인 실패");
  }
};
</script>

<template>
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
            <button type="button" class="btn btn-light">아이디 찾기</button>
            <button type="button" class="btn btn-light">비밀번호 찾기</button>
          </div>
        </form>
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
