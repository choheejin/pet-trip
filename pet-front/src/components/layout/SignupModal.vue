<script setup>
import { ref, computed, watch } from "vue";
import { useAuthStore } from "@/stores/user";
const userStore = useAuthStore();

const emit = defineEmits(["close"]);
const signupForm = ref({
  user_id: "",
  username: "",
  email: "",
  password: "",
});

const idErrorMessage = ref("");
const emailErrorMessage = ref("");
const pwdErrorMessage = ref("");

const idValid = async () => {
  try {
    await userStore.checkId(signupForm.value.user_id);
    idErrorMessage.value = ""; // 중복된 아이디가 아닌 경우
    return true;
  } catch (error) {
    idErrorMessage.value =
      error.response?.data?.message || "이미 존재하는 아이디입니다.";
    return false;
  }
};
// 이메일 유효성 검사
const emailValid = computed(() => {
  const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  return emailPattern.test(signupForm.value.email);
});

watch(
  () => signupForm.value.email,
  (newVal) => {
    emailErrorMessage.value = emailValid.value
      ? ""
      : "이메일에는 '@'를 포함해야 합니다.";
  }
);

// 비밀번호 유효성 검사
const passwordValid = computed(() => signupForm.value.password.length >= 6);
watch(
  () => signupForm.value.password,
  (newVal) => {
    pwdErrorMessage.value = passwordValid.value
      ? ""
      : "비밀번호는 최소 6자 이상이어야 합니다.";
  }
);
// 회원가입 함수
const join = async () => {
  if (emailValid.value && passwordValid.value) {
    try {
      // user.js 스토어의 signup 호출
      await userStore.join(signupForm.value);

      // 회원가입 후 모달 닫기
      emit("close");
    } catch (error) {
      console.error("회원가입 실패:", error);
    }
  } else {
    console.log("유효성 검사 실패");
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
        <h1 class="modal-title fs-5" id="signupModalLabel">회원가입</h1>
      </div>
      <div class="modal-body">
        <form @submit.prevent="join">
          <div class="mb-3">
            <label for="user_id" class="form-label">아이디</label>
            <input
              v-model="signupForm.user_id"
              type="text"
              class="form-control"
              id="user_id"
              placeholder="아이디"
              required
              @blur="idValid"
            />
            <div v-if="idErrorMessage" class="text-danger">
              {{ idErrorMessage }}
            </div>
          </div>
          <div class="mb-3">
            <label for="username" class="form-label">이름</label>
            <input
              v-model="signupForm.username"
              type="text"
              class="form-control"
              id="username"
              placeholder="이름"
              required
            />
          </div>
          <div class="mb-3">
            <label for="email" class="form-label">이메일</label>
            <input
              v-model="signupForm.email"
              type="email"
              class="form-control"
              id="email"
              placeholder="이메일"
              required
            />
            <div v-if="emailErrorMessage" class="text-danger">
              {{ emailErrorMessage }}
            </div>
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">비밀번호</label>
            <input
              v-model="signupForm.password"
              type="password"
              class="form-control"
              id="password"
              placeholder="비밀번호"
              required
            />
            <div v-if="pwdErrorMessage" class="text-danger">
              {{ pwdErrorMessage }}
            </div>
          </div>
          <div class="modal-footer">
            <button
              type="submit"
              class="btn w-100"
              :disabled="idErrorMessage || !emailValid || !passwordValid"
            >
              회원가입
            </button>
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
  font-weight: bolder;
}
.icon {
  width: 80px;
}
.text-danger {
  font-size: 0.9rem;
}

.btn {
  background-color: #e0e5b6;
}
.btn:hover {
  background-color: #c0c49c;
}
</style>
