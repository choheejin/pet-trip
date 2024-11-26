<script setup>
import { computed, onMounted, ref } from "vue";
import BoardCommentItem from "./BoardCommentItem.vue";
import travelplanApi from "@/api/travelplanApi";
import BoardCommentWrite from "./BoardCommentWrite.vue";

// TODO: 부모에서 리스트 초기화가 필요한지 받아오는 기능이 필요하다
// TODO: 유저 정보를 받아올 수 있게 된다면 이미지 셋팅하기~

const comments = ref([]);

const props = defineProps(["plan_id"]);

const getCommentList = async () => {
  const params = {
    plan_id: props.plan_id,
  };

  await travelplanApi.get("/parent-comments", { params }).then((res) => {
    comments.value = res.data;
  });
};

const totalComments = computed(() => {
  return comments.value.length;
});

const handlePostComment = async (data) => {
  await travelplanApi.post("/post-comment", data).then((res) => {
    if (res.status == 201) {
      comments.value.push(res.data);
    }
  });
};

onMounted(() => {
  getCommentList();
});
</script>

<template>
  <BoardCommentWrite
    :parent_comment_id="0"
    :plan_id="plan_id"
    @handle-submit="handlePostComment"
  />

  <div class="strong">댓글 {{ totalComments }}개</div>
  <BoardCommentItem
    class="comment"
    v-for="comment in comments"
    :key="comment.id"
    :comment="comment"
    :plan_id="plan_id"
  />
</template>

<style scoped>
.strong {
  font-weight: bold;
  padding-bottom: 0.5rem;
  margin-bottom: 1rem;
  border-bottom: 1px solid rgb(212 212 212);
}
</style>
