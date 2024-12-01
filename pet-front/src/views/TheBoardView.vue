<script setup>
import { onMounted, ref, computed } from "vue";
import travelplanApi from "@/api/travelplanApi";
import BoardSide from "@/components/board/BoardSide.vue";
import BoardTravelPlan from "@/components/board/BoardTravelPlan.vue";

// 검색 결과
const travelplans = ref([]);
const favorites = ref([]);
const page = ref(1);
const sort = ref("");
const totalPages = ref(1);
const range = 5;

const start = computed(() => range * parseInt((page.value - 1) / range) + 1);
const end = computed(() => {
  const number = (parseInt((page.value - 1) / range) + 1) * range;
  return number > totalPages.value ? totalPages.value : number;
});
const pages = computed(() =>
  Array.from({ length: end.value - start.value + 1 }, (_, i) => start.value + i)
);

// 좋아요 추가
const addFavorite = async (param) => {
  try {
    const data = {
      plan_id: param.plan_id,
      user_id: param.user_id,
    };
    console.log("데이터 확인해보자!!! : ", data);
    // API 요청
    await travelplanApi.post(`/add-user-favorite-plan?plan_id=${data.plan_id}`);

    // 로컬 상태 업데이트
    const index = travelplans.value.findIndex(
      (plan) => plan.id === param.plan_id
    );
    if (index !== -1) {
      favorites.value[index] = true; // 좋아요로 업데이트
    }
  } catch (error) {
    console.error("좋아요 요청 실패:", error);
  }
};

// 좋아요 취소
const removeFavorite = async (param) => {
  try {
    const data = {
      plan_id: param.plan_id,
      user_id: param.user_id,
    };
    console.log("취소 데이터 확인해보자!!! : ", data);
    // API 요청
    await travelplanApi.delete(
      `/delete-user-favorite-plan?plan_id=${data.plan_id}`
    );

    // 로컬 상태 업데이트
    const index = travelplans.value.findIndex(
      (plan) => plan.id === param.plan_id
    );
    if (index !== -1) {
      favorites.value[index] = false; // 좋아요 취소로 업데이트
    }
  } catch (error) {
    console.error("좋아요 취소 요청 실패:", error);
  }
};

// 검색 하기 - 정렬 조건
const getTravelPlansBySorting = async () => {
  try {
    const { data } = await travelplanApi.get("/plans", {
      params: {
        sort: sort.value,
        page: page.value,
      },
    });
    console.log("정렬!!!", data);
    travelplans.value = data.plans;
    favorites.value = data.favoritePlans;
    totalPages.value = data.total_pages;
    // console.log(sort.value, "조건으로 정렬된 계획 : ", travelplans.value);
  } catch (error) {
    console.error("Error fetching travel plans:", error);
  }
};

// 페이지 네이션에서 페이지 변경 시 호출되는 함수
const changePage = (newPage) => {
  page.value = newPage;
  console.log(page.value);
  // sort가 빈 값이면 오래된 순으로 getTravelPlansBySorting() 호출
  if (sort.value === "") {
    sort.value = "oldest";
  }
  // console.log("정렬 조건 주기:", sort.value);
  getTravelPlansBySorting(); // 정렬된 목록 가져오기
};

// update-sort 이벤트를 받을 때 처리
const updateSort = (newSort) => {
  sort.value = newSort;
  // sort가 바뀔 때마다 changePage 호출하여 페이지를 초기화
  changePage(1); // 정렬이 바뀌면 첫 페이지부터 다시 로드
};

// 처음 데이터 불러오기
onMounted(async () => {
  await getTravelPlansBySorting();
});
</script>

<template>
  <div style="height: calc(100vh - 66px); display: flex; align-items: center">
    <div style="width: 1000px; margin: 0 auto; height: 100%; display: flex">
      <div class="left" style="width: 30%">
        <BoardSide class="selectOption" @update-sort="updateSort" />
      </div>
      <div class="right" style="width: 70%">
        <div style="height: 100%; display: flex; flex-direction: column">
          <div class="button">
            <v-btn
              href="/map"
              rounded="l"
              size="large"
              color="#ccd5ae"
              class="font-weight-bold"
            >
              <i class="fa-solid fa-calendar-check"></i>여행 계획 세우기</v-btn
            >
          </div>
          <div class="CardTravelPlan">
            <!--        <h1>선택된 정렬 기준 : {{ sort }}</h1>-->
            <BoardTravelPlan
              :travelplans="travelplans"
              :favorites="favorites"
              @like="addFavorite"
              @dislike="removeFavorite"
            />
          </div>

          <div class="pagination-group">
            <nav aria-label="Page navigation example">
              <ul class="pagination">
                <li class="page-item" :class="{ disabled: start === 1 }">
                  <a
                    class="page-link"
                    href="#"
                    aria-label="Previous"
                    @click.prevent="changePage(start - 1)"
                  >
                    <span aria-hidden="true">&laquo;</span>
                    <!-- <span class="sr-only">Previous</span> -->
                  </a>
                </li>

                <li
                  v-for="p in pages"
                  :key="p"
                  class="page-item"
                  :class="{ active: p == page }"
                >
                  <a
                    class="page-link"
                    href="#"
                    @click.prevent="changePage(p)"
                    >{{ p }}</a
                  >
                </li>

                <li class="page-item" :class="{ disabled: end == totalPages }">
                  <a
                    class="page-link"
                    href="#"
                    aria-label="Next"
                    @click.prevent="changePage(end + 1)"
                  >
                    <span aria-hidden="true">&raquo;</span>
                    <!-- <span class="sr-only">Next</span> -->
                  </a>
                </li>
              </ul>
            </nav>
          </div>

          <!-- 페이지네이션 -->
          <!-- <div class="pagination-group">
            <nav aria-label="Page navigation example">
              <ul class="pagination">
                <li class="page-item" :class="{ disabled: page === 1 }">
                  <a
                    class="page-link"
                    href="#"
                    aria-label="Previous"
                    @click.prevent="changePage(page - 1)"
                  >
                    <span aria-hidden="true">&laquo;</span>
                    <span class="sr-only">Previous</span>
                  </a>
                </li>
                <li class="page-item" :class="{ active: page % 3 === 1 }">
                  <a
                    class="page-link"
                    href="#"
                    @click.prevent="changePage(page)"
                    >{{ page }}</a
                  >
                </li>
                <li
                  v-if="page + 1 > totalPages"
                  class="page-item"
                  :class="{ active: page % 3 === 2 }"
                >
                  <a
                    class="page-link"
                    href="#"
                    @click.prevent="
                      changePage(Math.ceil(page + 1 / totalPages))
                    "
                    >{{ page }}</a
                  >
                </li>
                <li
                  v-if="page + 2 > totalPages"
                  class="page-item"
                  :class="{ active: page % 3 === 0 }"
                >
                  <a
                    class="page-link"
                    href="#"
                    @click.prevent="
                      changePage(Math.ceil(page + 2 / totalPages))
                    "
                    >{{ page }}</a
                  >
                </li>
                <li class="page-item" :class="{ disabled: page < totalPages }">
                  <a
                    class="page-link"
                    href="#"
                    aria-label="Next"
                    @click.prevent="changePage(page + 1)"
                  >
                    <span aria-hidden="true">&raquo;</span>
                    <span class="sr-only">Next</span>
                  </a>
                </li>
              </ul>
            </nav>
          </div> -->
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.button {
  display: flex;
  justify-content: end;
  margin-top: 81px;
  padding: 0px 8px 8px 50px;
}
.left {
  border: 1px solid whitesmoke;
  height: calc(100vh - 82px);
  padding: 20px 10px;
  margin: 10px 5px;
}
.selectOption {
  margin-top: 50px;
}
.right {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 66px); /* 부모 컨테이너의 높이 */
  justify-content: flex-start; /* 위에서부터 순서대로 배치 */
}

.CardTravelPlan {
  height: fit-content;
  margin-top: 10px;
  display: flex;
  justify-content: center; /* 가로 중심 */
  align-items: center; /* 세로 중심 */
}

.pagination-group {
  height: fit-content;
  margin-top: 25px; /* 아래로 고정 */
  padding-bottom: 20px; /* 하단에 여백 */
  display: flex;
  justify-content: center;
}
</style>
