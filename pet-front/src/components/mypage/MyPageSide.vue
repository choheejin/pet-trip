<script setup>
import { ref } from "vue";
import myPageApi from "@/api/mypageApi";

const img = ref("/icon.png");
const fileInput = ref(null);
const userInfo = ref([]);

// 사용자 정보 조회 /info
const getUserInfo = async () => {
  const { data } = await myPageApi.get("/info", {});
  userInfo.value = data;
  console.log("사용자 정보 출력하기 : ", userInfo);
};

const openFilePicker = () => {
  fileInput.value.click();
};

const changeProfile = () => {
  const file = fileInput.value.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      img.value = e.target.result; // 새로운 이미지 설정
      console.log("프로필 이미지 변경! : ", img.value);
    };
    reader.readAsDataURL(file);
  }
};

getUserInfo();
</script>

<template>
  <div class="sidebar">
    <div class="profile-image">
      <div>
        <img :src="img" alt="Profile Image" />
      </div>
      <div class="edit-button">
        <button @click="openFilePicker">
          <i class="fa-solid fa-pen"></i>
        </button>
        <input
          type="file"
          ref="fileInput"
          style="display: none"
          @change="changeProfile"
        />
      </div>
    </div>
    <div class="info"></div>
  </div>
</template>

<style scoped>
.sidebar {
  border: 1px solid rgb(228, 228, 228);
  border-radius: 15px;
  height: calc(100vh - 82px);
  padding: 20px 10px;
  margin: 10px 5px;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
.profile-image {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  z-index: 0;
  box-shadow: 2px 5px 10px rgba(0, 0, 0, 0.2);
}

.profile-image img {
  width: 150px;
  height: 150px;
  object-fit: cover;
  object-position: center;
  border-radius: 100%;
}

.edit-button {
  position: absolute;
  bottom: -10px;
  left: 80%;
  transform: translateX(-50%);
  background-color: #ccd5ae;
  color: white;
  border: none;
  border-radius: 50%;
  padding: 5px 10px;
  font-size: 20px;
  cursor: pointer;
  z-index: 1;
}

.edit-button:hover {
  background-color: #a8ad95;
}
</style>
