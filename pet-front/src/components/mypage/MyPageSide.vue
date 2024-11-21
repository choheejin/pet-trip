<script setup>
import { ref } from "vue";
import myPageApi from "@/api/myPageApi";

const img = ref("/icon.png");
const userImg = ref("");
const fileInput = ref(null);
const userInfo = ref([]);

// 사용자 정보 조회 /info
const getUserInfo = async () => {
  const { data } = await myPageApi.get("/user/info", {});
  userInfo.value = data;
  console.log("사용자 정보 출력하기 : ", userInfo.value);
  if (userInfo.value.image !== null) {
    // userInfo.value.image 경로가 "profile/ssafy1.png" 형태라면
    userImg.value =
      "http://localhost:8080/pet/profile/" +
      userInfo.value.image.split("/").pop();
  } else {
    userImg.value = img.value;
  }
  // console.log("사용자 이미지 정보 : ", img.value);
};

const openFilePicker = () => {
  fileInput.value.click();
};

// 프로필 이미지 변경
const changeProfile = () => {
  const file = fileInput.value.files[0]; // 파일 선택
  if (file) {
    const reader = new FileReader(); // FileReader 객체 생성

    // 파일 읽기가 완료되면
    reader.onload = async (e) => {
      userImg.value = e.target.result; // 새로운 이미지 미리보기

      // 이미지 변경 API 호출
      updateImage(file);
    };

    // 선택한 파일을 Data URL로 읽기
    reader.readAsDataURL(file);
  }
};

// 이미지 업데이트하는 api 호출
const updateImage = async (file) => {
  const formData = new FormData();
  formData.append("file", file); // 이미지만 전송

  try {
    const { data } = await myPageApi.patch("/updateimage", formData, {
      headers: {
        "Content-Type": "multipart/form-data", // 파일 전송에 필요한 헤더
      },
    });

    // 서버에서 반환된 이미지 URL을 img에 할당하여 미리보기 업데이트
    userImg.value = `http://localhost:8080/pet/profile/${data.newImageName}`;
    console.log("이미지 업데이트 성공!!!", userImg.value);

    // 사용자 정보 재조회
    getUserInfo();
  } catch (error) {
    console.log("이미지 업데이트 실패 : ", error);
  }
};

getUserInfo();
</script>

<template>
  <div class="sidebar">
    <div class="profile-image">
      <div>
        <img :src="userImg" alt="Profile Image" />
      </div>
      <div class="edit-button">
        <button @click="openFilePicker">
          <i class="fa-solid fa-gear"></i>
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
