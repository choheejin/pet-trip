<script setup>
import { ref } from "vue";
import mapApi from "@/api/mapApi";
import travelplanApi from "@/api/travelplanApi";
import BoardSide from "@/components/board/BoardSide.vue";
import BoardTravelPlan from "@/components/board/BoardTravelPlan.vue";

// 검색 결과
const travelplans = ref([]);
const page = ref(1);
const sort = ref("");

// 검색 하기 - 무작위로
const getTravelPlans = async () => {
  // console.log("게시판?!", page.value);
  const { data } = await mapApi.get("", {
    params: {
      page: page.value,
    },
  });
  travelplans.value = data.list;
  console.log("기본 계획 : ",travelplans.value);
};

// 검색 하기 - 정렬 조건
const getTravelPlansBySorting = async () => {
  // console.log("게시판 정렬!!!", page.value);
  const { data } = await travelplanApi.get("", {
    params: {
      sort: sort.value,
      page: page.value,
    },
  });
  travelplans.value = data;
  console.log("정렬된 계획 : ", travelplans.value)
};

// 페이지 네이션에서 페이지 변경 시 호출되는 함수
const changePage = (newPage) => {
  page.value = newPage;
  // sort가 빈 값이면 getTravelPlans() 호출, 아니면 getTravelPlansBySorting() 호출
  if (sort.value === "") {
    getTravelPlans(); // 정렬 없이 가져오기
  } else {
    // console.log("정렬 조건 주기:", sort.value);
    getTravelPlansBySorting(); // 정렬된 목록 가져오기
  }
};

// update-sort 이벤트를 받을 때 처리
const updateSort = (newSort) => {
  sort.value = newSort;
  // sort가 바뀔 때마다 changePage 호출하여 페이지를 초기화
  changePage(1); // 정렬이 바뀌면 첫 페이지부터 다시 로드
};

// 처음 데이터 불러오기
getTravelPlans();
</script>

<template>
  <div style="height: calc(100vh - 66px); display: flex; align-items: center">
    <div style="width: 1000px; margin: 0 auto; height: 100%; display: flex">
      <div class="left" style="width: 30%">
        <BoardSide class="selectOption" @update-sort="updateSort" />
      </div>
      <div class="right"
           style="width: 70%">
        <div><h1>글 등록 버튼 만들어야즹</h1></div>
        <div>
  <!--        <h1>선택된 정렬 기준 : {{ sort }}</h1>-->
          <BoardTravelPlan :travelplans="travelplans" />
          <!-- 페이지네이션 -->
          <div class="pagination-group">
            <nav aria-label="Page navigation example">
              <ul class="pagination">
                <li class="page-item" :class="{ disabled: page.value === 1 }">
                  <a
                    class="page-link"
                    href="#"
                    aria-label="Previous"
                    @click.prevent="changePage(page.value - 1)"
                  >
                    <span aria-hidden="true">&laquo;</span>
                    <span class="sr-only">Previous</span>
                  </a>
                </li>
                <li class="page-item" :class="{ active: page.value === 1 }">
                  <a class="page-link" href="#" @click.prevent="changePage(1)"
                    >1</a
                  >
                </li>
                <li class="page-item" :class="{ active: page.value === 2 }">
                  <a class="page-link" href="#" @click.prevent="changePage(2)"
                    >2</a
                  >
                </li>
                <li class="page-item" :class="{ active: page.value === 3 }">
                  <a class="page-link" href="#" @click.prevent="changePage(3)"
                    >3</a
                  >
                </li>
                <li class="page-item" :class="{ disabled: page.value === 3 }">
                  <a
                    class="page-link"
                    href="#"
                    aria-label="Next"
                    @click.prevent="changePage(page.value + 1)"
                  >
                    <span aria-hidden="true">&raquo;</span>
                    <span class="sr-only">Next</span>
                  </a>
                </li>
              </ul>
            </nav>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
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
  height: calc(100vh - 66px);
  align-items: center;
}
.pagination-group {
  display: flex;
  justify-content: center;
  margin-top: auto;
}

</style>
