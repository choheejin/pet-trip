<script setup>
import reviewApi from "@/api/reviewApi";
import { useAuthStore } from "@/stores/user";
import axios from "axios";
import { ref } from "vue";
import { useRoute } from "vue-router";
import router from "@/router/index.js";

const route = useRoute();
const planInfo = ref({
  plan_id: route.params.plan_id,
  plan_title: route.query.plan_title,
}); // 동적 파라미터 plan_id를 받아옵니다.
const reviewTitle = ref("");
const reviewContent = ref("");
const thumbnail = ref({
  url: "",
  name: "",
  index: -1,
}); // 썸네일 이미지 URL
const images = ref([]); // 업로드된 이미지들
const dogSize = ref(0); // 강아지 크기 (소: 1, 중: 2, 대형: 3)
const isPublic = ref("공개"); // 공개/비공개
const fileInputFiles = ref([]);
const reviewForm = ref(null);
const uploadImages = ref([]);

// 강아지 크기 선택
const toggleDogSize = (size) => {
  dogSize.value = dogSize.value === size ? 0 : size;
};

// 유효성 검사 상태
const reviewTitleError = ref(false); // 제목 오류 여부
const reviewContentError = ref(false); // 내용 오류 여부

// 이미지 파일 업로드 처리
const handleImageUpload = (event) => {
  const files = event.target.files;
  if (files) {
    if (images.value.length + files.length > 10) {
      alert("최대 10장까지만 업로드 가능합니다.");
      return;
    }
    const newFiles = Array.from(files).filter((file) => {
      const allowedTypes = ["image/jpeg", "image/png", "image/gif"];
      if (!allowedTypes.includes(file.type)) {
        alert(`지원하지 않는 파일 형식: ${file.type}`);
        return false;
      }
      return true;
    });

    uploadImages.value = newFiles;
    newFiles.forEach((file) => {
      const reader = new FileReader();
      reader.onload = (e) => {
        const newImage = { url: e.target.result, name: file.name };
        images.value.push(newImage);
        // 첫 번째 이미지일 경우 자동으로 썸네일 설정
        if (images.value.length === 1) {
          setThumbnail(newImage, 0);
        }
      };
      reader.readAsDataURL(file);
    });

    updateFileInput([...fileInputFiles.value, ...newFiles]);
  }
};

const updateFileInput = (newFiles) => {
  fileInputFiles.value = newFiles;
};

// 이미지 삭제 메서드
const deleteImage = (image) => {
  const index = images.value.findIndex(
    (img) => img.name === image.name && img.url === image.url
  );
  if (index !== -1) {
    images.value.splice(index, 1);

    // 파일 인풋에서도 제거
    const fileIndex = fileInputFiles.value.findIndex(
      (file) => file.name === image.name
    );
    if (fileIndex !== -1) {
      const updatedFiles = [...fileInputFiles.value];
      updatedFiles.splice(fileIndex, 1);
      updateFileInput(updatedFiles); // 업데이트된 파일 목록 반영
    }

    // 썸네일 처리
    if (
      thumbnail.value.url === image.url &&
      thumbnail.value.name === image.name
    ) {
      thumbnail.value = images.value[0]
        ? { url: images.value[0].url, name: images.value[0].name, index: 0 }
        : { url: "", name: "", index: -1 };
    } else if (index < thumbnail.value.index) {
      // 삭제된 이미지가 썸네일 이전 인덱스였다면, 썸네일 인덱스 조정
      thumbnail.value.index--;
    }
  }
};

// 이미지 업데이트하는 api 호출
const insertImage = async (file, isThumbnail, plan_id, reviewId) => {
  const formData = new FormData();

  // console.log(
  //   "여기까지 왔나?? : ",
  //   file,
  //   "|",
  //   isThumbnail,
  //   "|",
  //   plan_id,
  //   "|",
  //   reviewId
  // );

  formData.append("file", file);
  formData.append("is_thumbnail", isThumbnail);
  formData.append("plan_id", plan_id);
  formData.append("review_id", reviewId);

  const authStore = useAuthStore();
  try {
    console.log(formData.get("file"));
    console.log(formData);
    const { data } = await axios.post(
      "http://localhost:8080/pet/review/insertimage",
      formData,
      {
        headers: {
          accessToken: authStore.token,
          "Content-Type": "multipart/form-data", // 파일 전송에 필요한 헤더
        },
      }
    );
    console.log("이미지 업로드 성공 : ", data);
  } catch (error) {
    console.log("이미지 업데이트 실패 : ", error);
  }
};

// 리뷰 저장하는 api 호출
const saveReview = async (reviewData) => {
  try {
    const { data: reviewId } = await reviewApi.post("", reviewData);

    // console.log("사진 목록중 썸넬 : ", thumbnail.value);

    // 리뷰 ID를 얻은 후, 이미지 업로드 호출
    for (let i = 0; i < images.value.length; i++) {
      const file = uploadImages.value[i];
      const isThumbnail = i === thumbnail.value.index; // 썸네일 여부 확인

      // console.log("저장할 파일 : ", file);

      // 이미지 업로드 호출 (리뷰 ID 포함)
      // console.log("썸네일 ? :", isThumbnail);
      // console.log("플랜 아이디 : ", parseInt(planInfo.value.plan_id, 10));
      await insertImage(
        file,
        isThumbnail,
        parseInt(planInfo.value.plan_id, 10),
        reviewId
      );
    }
    router.push({ name: "Review" });
  } catch (error) {
    console.log("리뷰 저장 실패 : ", error);
  }
};

// 썸네일 설정
const setThumbnail = (image, index) => {
  if (
    thumbnail.value.url !== image.url ||
    thumbnail.value.name !== image.name
  ) {
    thumbnail.value = {
      url: image.url,
      name: image.name,
      index: index,
    };
    console.log("썸네일이 설정되었습니다:", thumbnail.value);
  }
};

// 후기 제출 처리
const submitReview = async () => {
  // 제목과 내용 유효성 검사
  reviewTitleError.value = !reviewTitle.value;
  reviewContentError.value = !reviewContent.value;

  if (reviewTitleError.value || reviewContentError.value) {
    return;
  }

  const reviewData = {
    title: reviewTitle.value,
    content: reviewContent.value,
    plan_id: parseInt(planInfo.value.plan_id, 10), // plan_id를 정수로 변환
    dog_size: dogSize.value,
    is_public: isPublic.value === "공개", // "공개"면 true, "비공개"면 false
  };
  // review 업로드
  saveReview(reviewData);

  console.log("Review submitted:", reviewData);
};
</script>

<template>
  <div class="container">
    <div class="write-box">
      <div class="title">
        <div class="title-left">
          <h1>{{ planInfo.plan_title }}</h1>
        </div>
        <!-- 공개 여부 설정 -->
        <div class="title-right">
          <v-switch
            v-model="isPublic"
            :label="`${isPublic}`"
            color="success"
            false-value="비공개"
            true-value="공개"
          />
        </div>
      </div>
      <form class="input-group" ref="reviewForm">
        <!-- 강아지 크기 선택 -->
        <div class="input-item dog-size-buttons">
          <button
            @click="toggleDogSize(1)"
            :class="{ selected: dogSize === 1 }"
          >
            소형견
          </button>
          <button
            @click="toggleDogSize(2)"
            :class="{ selected: dogSize === 2 }"
          >
            중형견
          </button>
          <button
            @click="toggleDogSize(3)"
            :class="{ selected: dogSize === 3 }"
          >
            대형견
          </button>
        </div>

        <!-- 이미지 업로드 -->
        <div class="input-item">
          <div style="display: flex; flex-direction: row">
            <v-file-input
              label="File input"
              prepend-icon="fa-regular fa-image"
              variant="outlined"
              multiple
              @change="handleImageUpload"
              @update:modelValue="updateFileInput"
            />
          </div>
        </div>

        <!-- 이미지 미리보기 -->
        <div
          class="images"
          :scroll-distance="100"
          style="overflow-x: auto; display: flex"
        >
          <div v-for="(image, index) in images" :key="index" class="image-item">
            <img :src="image.url" :alt="'Image ' + index" />
            <div class="select-thumbnail">
              <i
                class="select-thumbnail-selected fa-solid fa-square-check"
                v-if="
                  thumbnail.url === image.url && thumbnail.name === image.name
                "
              ></i>
              <i
                class="fa-solid fa-square-check"
                @click="setThumbnail(image, index)"
                v-else
              >
              </i>
            </div>
            <div class="delete-image">
              <i
                class="fa-solid fa-delete-left"
                @click="deleteImage(image)"
              ></i>
            </div>
            <div
              v-if="
                thumbnail.url === image.url && thumbnail.name === image.name
              "
              class="thumbnail-indicator"
            ></div>
          </div>
        </div>

        <!-- 제목 입력 -->
        <div class="input-item">
          <v-text-field
            class="review-title"
            label="제목을 입력하세요."
            v-model="reviewTitle"
            placeholder="제목을 입력하세요."
            variant="outlined"
            color="primary"
            required
            :error="reviewTitleError"
            :error-messages="
              reviewTitleError ? ['제목은 필수 항목입니다.'] : []
            "
          ></v-text-field>
        </div>

        <!-- 내용 입력 -->
        <div class="input-item">
          <v-text-field
            class="review-content"
            label="내용을 입력하세요."
            v-model="reviewContent"
            placeholder="내용을 입력하세요."
            variant="outlined"
            color="primary"
            required
            :error="reviewContentError"
            :error-messages="
              reviewContentError ? ['내용은 필수 항목입니다.'] : []
            "
            multiline
          ></v-text-field>
        </div>
      </form>

      <!-- 리뷰 제출 버튼 -->
      <div>
        <button @click="submitReview">리뷰 제출</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  border: 1px solid rgb(228, 228, 228);
  border-radius: 15px;
  height: calc(100vh - 82px);
  width: 986px;
  display: flex;
  margin: 10px auto;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  justify-content: center;
  align-items: center;
  padding: 20px;
  background-color: #ffffff;
}
.write-box {
  /* height: 70%;*/
  width: 70%;
}
.title {
  display: flex;
}
.title-left {
  text-align: center;
  color: #333333;
  width: 80%;
}
.title-right {
  width: 20%;
  display: flex;
  justify-content: flex-end;
}

.input-group {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.input-item {
  margin-top: 10px;
}

.images {
  padding: 10px;
  display: flex;
  gap: 10px;
  overflow-x: auto;
  width: 100%;
  max-width: 100%;
  white-space: nowrap; /* 이미지들이 줄바꿈 없이 가로로 나열되도록 */
}

.image-item {
  position: relative;
  flex-shrink: 0;
  text-align: center;
}

.image-item img {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 10px;
  transition: transform 0.3s ease;
}

.select-thumbnail {
  position: absolute;
  top: 0;
  left: 5px;
  color: rgb(255, 255, 255);
  font-size: 20px;
  cursor: pointer;
}
.select-thumbnail:hover {
  color: #8bb7ec;
}
.select-thumbnail-selected {
  color: #5995ff;
}

.delete-image {
  position: absolute;
  top: 0;
  right: 5px;
  color: white;
  font-size: 20px;
  cursor: pointer;
}
.delete-image:hover {
  color: #ccd5ae;
}

.thumbnail-indicator {
  margin-top: 5px;
  background-color: #5995ff;
  font-weight: bold;
  padding: 5px;
  z-index: 1;
}

.dog-size-buttons {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.dog-size-buttons button {
  padding: 5px 15px;
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  border-radius: 5px;
  cursor: pointer;
}

.dog-size-buttons button.selected {
  background-color: #ccd5ae;
  color: white;
}

button {
  padding: 10px 20px;
  background-color: #ccd5ae;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
  width: 100%;
}

button:hover {
  background-color: #8f987d;
}
</style>
