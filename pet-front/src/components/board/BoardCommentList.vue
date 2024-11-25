<script setup>
import travelplanApi from "@/api/travelplanApi";
import { onMounted, ref } from "vue";
import BoardCommentWrite from "./BoardCommentWrite.vue";
import BoardCommentItem from "./BoardCommentItem.vue";

const props = defineProps(["plan_id", "comments", "parent_comment_id"]);

// TODO: 부모에서 리스트 초기화가 필요한지 받아오는 기능이 필요하다
// TODO: 유저 정보를 받아올 수 있게 된다면 이미지 셋팅하기~

const isWriting = ref([]);

const getChildComments = async (id) => {
  if (!id) return;
  const params = {
    parent_comment_id: id,
  };
  const { data } = await travelplanApi.get("/child-comments", { params });
  console.log(data);
  return data;
};

const setShowWriting = (idx) => {
  isWriting.value[idx] = !isWriting.value[idx];
};
</script>

<template>
  <BoardCommentItem
    class="comment"
    v-for="comment in comments"
    :key="comment.id"
    :comment="comment"
  ></BoardCommentItem>
  <!-- <div class="comment" v-for="comment in comments" :key="comment.id">
    <div class="strong">
      {{ comment.user_id }}
    </div>
    <div class="comment-detail">{{ comment.comment }}</div>

    <div class="not-root">
      <BoardCommentList
        :plan_id="plan_id"
        :parent_comment_id="comment.id"
        :comments="getChildComments(comment.id)"
      />
    </div>

    <div class="write">
      <div>
        <span class="create-date">{{ comment.created_at }}</span>
        <div class="write-button" @click="setShowWriting(comment.id)">답글</div>
      </div>

      <div v-if="isWriting[comment.id]" class="write-input">
        <BoardCommentWrite :plan_id="plan_id" :parent_comment_id="comment.id" />
      </div>
    </div>
  </div> -->
</template>

<style scoped>
.not-root {
  background-color: rgb(203 213 225);
}

.strong {
  font-weight: 700;
}

.comment-container {
  margin-top: 3rem;
}

.comment {
  border-bottom: 1px solid rgb(209 213 219);
  padding: 1rem 0.5rem;
}

.write {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.write > div {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.comment-detail {
  padding: 0.5rem 0.25rem;
}

.create-date {
  min-width: fit-content;
  font-size: small;
}

.write-button {
  display: flex;
  font-weight: 700;
  cursor: pointer;
  color: rgb(107 114 128);
}

.write-button > svg {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 15px;
}

.write-input {
  width: 100%;
}
</style>
