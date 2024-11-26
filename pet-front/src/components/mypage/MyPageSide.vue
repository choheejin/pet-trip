<script setup>
import { computed, ref, watch, onMounted } from "vue";
import myPageApi from "@/api/myPageApi";

const img = ref("/icon.png");
const userImg = ref("");
const fileInput = ref(null);
const emailerror = ref(false);
const pwderror = ref(false);
const userInfo = ref({
  user_id: "",
  username: "",
  password: "",
  email: "",
});

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
    const { data } = await myPageApi.patch("/user/updateimage", formData, {
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

// 비밀번호 유효성 검사
const pwdValid = computed(() => {
  // 비밀번호는 최소 6자 이상이어야 함
  return userInfo.value.password && userInfo.value.password.length >= 6;
});

watch(
  () => userInfo.value.password,
  (newVal) => {
    // 비밀번호 길이에 맞춰 오류 메시지 설정
    pwderror.value = !pwdValid.value;
  }
);

// 이메일 유효성 검사
const emailValid = computed(() => {
  const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  return emailPattern.test(userInfo.value.email);
});

// 이메일 입력란 클릭 시 유효성 검사 시작
const handleFocus = (error) => {
  if (error == emailerror) {
    if (!emailValid.value) {
      emailerror.value = true;
    }
  }
  if (error == pwderror) {
    if (!pwdValid.value) {
      pwderror.value = true;
    }
  }
};
const handleBlur = (error) => {
  emailerror.value = false; // 포커스 아웃 시 오류 상태 비활성화
  if (error == emailerror) emailerror.value = false;
  if (error == pwderror) pwderror.value = false;
};

// 변경하는 버튼
const checkEdit = () => {
  console.log("변경하기 버튼 클릭", userInfo.value);
  const confirmEdit = window.confirm("정보를 수정하시겠습니까?");
  if (confirmEdit) {
    // API 호출 파라미터
    const updateData = {
      user_id: userInfo.value.user_id,
      username: userInfo.value.username,
      password: userInfo.value.password,
      email: userInfo.value.email,
    };
    console.log("수정할 정보 : ", updateData);
    // API 호출
    myPageApi
      .patch("/user/update-info", updateData)
      .then((response) => {
        console.log("정보 수정 완료 ", response);
        alert("정보가 수정되었습니다.");
        getUserInfo(); // 사용자 정보 갱신
      })
      .catch((error) => {
        console.log("정보 수정 실패 : ", error);
        alert("정보 수정에 실패함!");
      });
  }
};

// 모든 필드가 입력되었는지 확인
const isFormValid = computed(() => {
  return (
    userInfo.value.user_id !== "" &&
    userInfo.value.username !== "" &&
    userInfo.value.password !== "" &&
    userInfo.value.email !== "" &&
    pwdValid.value &&
    emailValid.value
  );
});
getUserInfo();
</script>

<template>
  <div class="sidebar">
    <div class="sidebar-items">
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
      <div class="info">
        <v-text-field
          class="mb-4"
          label="아이디 수정"
          v-model="userInfo.user_id"
          :placeholder="userInfo.user_id"
          variant="outlined"
          color="primary"
        ></v-text-field>
        <v-text-field
          class="mb-4"
          label="이름 수정"
          v-model="userInfo.username"
          :placeholder="userInfo.username"
          variant="outlined"
          color="primary"
        ></v-text-field>
        <v-text-field
          class="mb-4"
          type="password"
          label="비밀번호 수정"
          v-model="userInfo.password"
          placeholder=""
          variant="outlined"
          color="primary"
          :error="pwderror"
          :error-messages="
            !pwderror
              ? []
              : ['6자 이상의 비밀번호 재입력 혹은 새 비밀번호를 입력하세요.']
          "
          @focus="handleFocus(pwderror)"
          @blur="handleBlur(pwderror)"
        ></v-text-field>
        <v-text-field
          class="mb-4"
          label="이메일 수정"
          v-model="userInfo.email"
          :placeholder="userInfo.email"
          variant="outlined"
          color="primary"
          :error="emailerror"
          :error-messages="!emailerror ? [] : ['유효하지 않은 이메일입니다.']"
          @focus="handleFocus(emailerror)"
          @blur="handleBlur(emailerror)"
        ></v-text-field>
        <v-btn
          class="mb-4"
          width="100%"
          variant="tonal"
          :disabled="!isFormValid"
          @click="checkEdit"
        >
          수정하기
        </v-btn>
      </div>
    </div>
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
.sidebar-items {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.profile-image {
  width: 150px;
  height: 150px;
  border-radius: 50%;
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

.mb-4 {
  margin-bottom: 16px;
}
.info {
  width: 80%;
  margin-top: 40px;
}
</style>
