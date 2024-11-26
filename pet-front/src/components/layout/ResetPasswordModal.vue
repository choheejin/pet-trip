<script setup>
import { ref, computed, watch } from "vue";
import axios from "axios";
import userApi from "@/api/userApi";
import { errorMessages } from "vue/compiler-sfc";

const props = defineProps({
  resetToken: {
    type: String,
    required: true,
  },
});

const isDisabled = ref(true);

const pwdCheckForm = ref({
  newPassword: "",
  confirmPassword: "",
});

const emit = defineEmits(["close"]); // 부모 컴포넌트에 close 이벤트 전달
const pwdErrorMessage = ref("");

const resetPassword = async () => {
  console.log(pwdCheckForm.value.newPassword);
  console.log(pwdCheckForm.value.confirmPassword);
  if (pwdCheckForm.value.newPassword !== pwdCheckForm.value.confirmPassword) {
    alert("비밀번호가 일치하지 않습니다.");
    return;
  }

  try {
    // 서버로 비밀번호 재설정 요청
    const response = await userApi.patch(
      "/reset-password",
      null, // Body가 없으므로 null 전달
      {
        headers: {
          accessToken: props.resetToken,
        },
        params: {
          new_password: pwdCheckForm.value.newPassword, // 쿼리 파라미터로 전달
        },
      }
    );

    console.log(response);

    alert(response.data || "비밀번호가 성공적으로 변경되었습니다.");
    emit("close"); // 성공 시 모달 닫기
  } catch (error) {
    console.error(
      "비밀번호 재설정 실패:",
      error.response?.data || error.message
    );
    alert(error.response?.data || "비밀번호 재설정 실패");
  }
};

// 비밀번호 유효성 검사
const passwordValid = () => {
  return pwdCheckForm.value.newPassword.length >= 6;
};

const passwordIdenticalValid = () => {
  return pwdCheckForm.value.confirmPassword === pwdCheckForm.value.newPassword;
};

watch(
  () => [pwdCheckForm.value.newPassword, pwdCheckForm.value.confirmPassword],
  (newVal) => {
    console.log("watch되는중");
    if (!passwordValid()) {
      console.log("길이 확인");
      pwdErrorMessage.value = passwordValid()
        ? ""
        : "비밀번호는 최소 6자 이상이어야 합니다.";
      isDisabled.value = true;
    }
    if (!passwordIdenticalValid()) {
      console.log("동일 확인");
      pwdErrorMessage.value = passwordIdenticalValid()
        ? ""
        : "비밀번호를 확인해주세요";
      isDisabled.value = true;
    }
    if (passwordIdenticalValid() && passwordValid()) {
      pwdErrorMessage.value = "";
      isDisabled.value = false;
    }
  }
);
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
        <h1 class="modal-title fs-5">비밀번호 재설정</h1>
      </div>
      <div class="modal-body">
        <form @submit.prevent="resetPassword">
          <div class="mb-3">
            <label for="new_password" class="form-label">새 비밀번호</label>
            <input
              v-model="pwdCheckForm.newPassword"
              type="password"
              class="form-control"
              placeholder="새 비밀번호"
            />
          </div>
          <div class="mb-3">
            <label for="confirm_password" class="form-label"
              >비밀번호 확인</label
            >
            <input
              v-model="pwdCheckForm.confirmPassword"
              type="password"
              class="form-control"
              placeholder="비밀번호 확인"
            />
            <div v-if="pwdErrorMessage" class="text-danger">
              {{ pwdErrorMessage }}
            </div>
          </div>
          <div class="modal-footer">
            <button type="submit" class="btn btn-login" :disabled="isDisabled">
              변경하기
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
}
.icon {
  width: 70px;
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
