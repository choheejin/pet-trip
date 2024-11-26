<script setup>
import { ref } from "vue";
import axios from "axios";

const props = defineProps({
  resetToken: {
    type: String,
    required: true,
  },
});

const emit = defineEmits(["close"]); // 부모 컴포넌트에 close 이벤트 전달
const newPassword = ref("");
const confirmPassword = ref("");

const resetPassword = async () => {
  if (newPassword.value !== confirmPassword.value) {
    alert("비밀번호가 일치하지 않습니다.");
    return;
  }

  try {
    const response = await axios.patch(
      "/api/reset-password",
      { new_password: newPassword.value },
      { headers: { accessToken: props.resetToken } }
    );
    alert("비밀번호가 성공적으로 변경되었습니다.");
    emit("close"); // 모달 닫기
  } catch (error) {
    console.error("비밀번호 재설정 실패:", error);
    alert("비밀번호 재설정 실패");
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
        <h1 class="modal-title fs-5">비밀번호 재설정</h1>
      </div>
      <div class="modal-body">
        <form @submit.prevent="resetPassword">
          <div class="mb-3">
            <label for="new_password" class="form-label">새 비밀번호</label>
            <input
              v-model="newPassword"
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
              v-model="confirmPassword"
              type="password"
              class="form-control"
              placeholder="비밀번호 확인"
            />
          </div>
          <div class="modal-footer">
            <button type="submit" class="btn btn-login">변경하기</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
