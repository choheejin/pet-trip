<script setup>
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import travelplanApi from "@/api/travelplanApi.js";
import BoardTravelPlanItemList from "./BoardTravelPlanItemList.vue";
import { useAuthStore } from "@/stores/user";

// 상세조회할 id
const route = useRoute();
const router = useRouter();

const id = route.query.id;

const isLiked = ref(false);
const plan = ref({});
const items = ref({});
const writerInfo = ref({
  user_id: "",
  profile_path: "",
});

const authStore = useAuthStore();

const getDetail = async () => {
  await travelplanApi.get(`/${id}`).then((res) => {
    console.log(res.data);

    isLiked.value = res.data?.isLiked;
    writerInfo.value = res.data.userInfo;
    writerInfo.value.profile_path =
      "http://localhost:8080/pet/profile/" +
      writerInfo.value.profile_path.split("/").pop();

    plan.value = res.data.plan;
    items.value = res.data.items;
    console.log("확인 : ", plan.value);
    console.log("여행지들 : ", items.value);
  });
};

const handleUpdate = () => {
  router.push({ path: "map", query: { id: plan.value.id } });
};

const handleDelete = async () => {
  await travelplanApi.delete(`${id}`).then((res) => {
    if (res.status === 204) {
      alert("삭제되었습니다");
      router.push({ path: "/board" });
    }
  });
};

const debounce = (func, delay) => {
  let timer;
  return function (...args) {
    clearTimeout(timer);
    timer = setTimeout(() => {
      func(...args);
    }, delay);
  };
};

const handleLikeOrDisLike = debounce(() => {
  const params = {
    plan_id: plan.value.id,
  };

  if (!isLiked.value) {
    console.log("좋아요 등록 요청");
    travelplanApi
      .post("/add-user-favorite-plan", null, { params })
      .then((res) => {
        if (res.status == 200) {
          isLiked.value = true;
          plan.value.favorite_cnt++;
        }
      });
  }

  if (isLiked.value) {
    console.log("좋아요 취소 요청");
    travelplanApi
      .delete("/delete-user-favorite-plan", { params })
      .then((res) => {
        if (res.status == 200) {
          isLiked.value = false;
          plan.value.favorite_cnt--;
        }
      });
  }
}, 500);

onMounted(() => {
  getDetail();
});
</script>

<template>
  <div class="board-detail-container">
    <h1>{{ plan.title }}</h1>
    <div class="meta-data">
      <div class="left-side">
        <div class="profile">
          <img
            v-if="
              writerInfo.profile_path != 'http://localhost:8080/pet/profile/'
            "
            :src="writerInfo.profile_path"
            alt="프로필 사진"
          />
          <div class="strong">{{ writerInfo["user_id"] }}</div>
        </div>
        <div>{{ plan.created_at }}</div>
        <div v-if="!plan.is_public">비공개</div>
        <div v-else>조회수: {{ plan.view_cnt }}</div>
      </div>

      <div class="right-side">
        <div v-if="authStore.user == writerInfo.user_id" class="writer-group">
          <div @click="handleUpdate">수정</div>
          <div @click="handleDelete">삭제</div>
        </div>
      </div>
    </div>
    <!-- <div v-if="plan.image" class="thumbnail">{{ plan.image }}</div> -->
    <div class="description">
      {{ plan.description }}

      <div class="item-list">
        <div class="strong item-list-title">여행 경로</div>
        <BoardTravelPlanItemList
          v-for="(item, idx) in items"
          :key="item.id"
          :item="item"
          :idx="idx + 1"
        />
      </div>
    </div>
    <div class="heart-group-container">
      <div
        :class="{ 'heart-group': !isLiked, 'heart-group-fill': isLiked }"
        @click="handleLikeOrDisLike"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="1.5"
          stroke="currentColor"
          class="heart"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
          />
        </svg>
        <div class="cnt">{{ plan.favorite_cnt }}</div>
      </div>
    </div>

    <div class="comment"></div>
  </div>
</template>

<style scoped>
.board-detail-container {
  display: flex;
  flex-direction: column;
  gap: 32px;
  padding: 152px 32rem;
}
.board-detail-container > .meta-data {
  display: flex;
  width: 100%;
  justify-content: space-between;
  padding-bottom: 1rem;
  border-bottom: 1px solid rgb(209 213 219);
}

.board-detail-container > .meta-data > .left-side {
  display: flex;
  align-items: center;
  gap: 20px;
}

.board-detail-container > .meta-data > .left-side > .profile {
  display: flex;
  gap: 0.5rem;
}

.board-detail-container > .meta-data > .left-side > .profile > img {
  width: 30px;
  height: 30px;
  border-radius: 30rem;
  border: 1px solid rgb(209 213 219);
}

.board-detail-container > .meta-data > .right-side {
  display: flex;
  align-items: center;
  gap: 10px;
}

.heart-group-container {
  margin-top: 3rem;
  display: flex;
  justify-content: center;
}

.heart-group {
  width: 3.5rem;
  height: 4.5rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border: 1px solid rgb(244 63 94);
  border-radius: 8rem;
  padding: 0.25rem 0.5rem;
  cursor: pointer;
}

.heart-group > .heart {
  width: 24px;
  stroke: rgb(244 63 94);
}

.heart-group > .cnt {
  color: rgb(244 63 94);
}

.heart-group:hover {
  display: flex;
  justify-content: center;
  border: 1px solid rgb(244 63 94);
  background-color: rgb(244 63 94);
  border-radius: 8rem;
  padding: 0.25rem 0.5rem;
  cursor: pointer;
}

.heart-group:hover > .heart {
  width: 24px;
  stroke: white;
}

.heart-group:hover > .cnt {
  color: white;
}

.heart-group-fill {
  width: 3.5rem;
  height: 4.5rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border: 1px solid rgb(244 63 94);
  background-color: rgb(244 63 94);
  border-radius: 8rem;
  padding: 0.25rem 0.5rem;
  cursor: pointer;
  color: white;
}

.heart-group-fill > .heart {
  width: 24px;
  stroke: white;
}

.right-side > .writer-group {
  display: flex;
  gap: 10px;
}

.right-side > .writer-group > div {
  cursor: pointer;
}

.description > .item-list {
  width: 100%;
  margin-top: 20px;
  padding: 0.5rem 1rem;
  display: flex;
  flex-direction: column;
  box-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1);
}

.description > .item-list > .item-list-title {
  font-size: large;
  text-align: center;
}

.comment {
  border-top: 1px solid rgb(209 213 219);
  margin-top: 3rem;
  padding: 0.5rem 1rem;
  width: 100%;
}

.strong {
  font-weight: bold;
}
</style>
