<script setup>
import BoardCommentItem from "@/components/board/BoardCommentItem.vue";
import BoardCommentWrite from "./BoardCommentWrite.vue";
import { ref, onMounted } from "vue";
import travelplanApi from "@/api/travelplanApi";

const props = defineProps(["comment"]);

const children = ref([]);

const getChildComments = async (id) => {
  if (!id) return;
  const params = {
    parent_comment_id: id,
  };
  const { data } = await travelplanApi.get("/child-comments", { params });
  console.log(data);
  children.value = data;
};

onMounted(() => {
  getChildComments(props.comment.id);
});
</script>

<template>
  <div class="strong">
    {{ comment.user_id }}
  </div>
  <div class="comment-detail">{{ comment.comment }}</div>

  <div class="not-root">
    <BoardCommentItem
      v-for="child in children"
      :key="child.id"
      :comment="child"
    ></BoardCommentItem>
  </div>

  <div class="write">
    <div>
      <span class="create-date">{{ comment.created_at }}</span>
      <div class="write-button" @click="setShowWriting(comment.id)">답글</div>
    </div>

    <div v-if="isWriting" class="write-input">
      <BoardCommentWrite :plan_id="plan_id" :parent_comment_id="comment.id" />
    </div>
  </div>
</template>

<style scoped></style>
