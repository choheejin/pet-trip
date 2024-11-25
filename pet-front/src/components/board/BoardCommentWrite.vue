<script setup>
import travelplanApi from "@/api/travelplanApi";
import { ref } from "vue";

const props = defineProps(["parent_comment_id", "plan_id"]);
const comment = ref("");

const emit = defineEmits(["handleSubmit"]);

const handleSubmit = () => {
  if (comment.value == "") return;

  const data = {
    plan_id: props.plan_id,
    comment: comment.value,
    parent_comment_id: props.parent_comment_id,
  };

  emit("handleSubmit", data);
  comment.value = "";
};
</script>

<template>
  <div class="write-comment">
    <textarea
      name=""
      id=""
      v-model="comment"
      placeholder="댓글을 작성하세요"
    ></textarea>
    <div class="submit">
      <button @click="handleSubmit">댓글 작성</button>
    </div>
  </div>
</template>

<style scoped>
.write-comment {
  margin-top: 1rem;
  width: 100%;
}

.write-comment > textarea {
  width: 100%;
  height: 4rem;
  padding: 0.25rem 0.5rem;
  border: 1px solid rgb(229 229 229);
  border-radius: 0.325rem;
}

.write-comment > .submit {
  width: 100%;
  display: flex;
  justify-content: flex-end;
}

.write-comment > .submit > button {
  background-color: rgb(34 197 94);
  width: 9rem;
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 0.325rem;
}
</style>
