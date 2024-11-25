<script setup>
import BoardCommentItem from "@/components/board/BoardCommentItem.vue";
import BoardCommentWrite from "./BoardCommentWrite.vue";
import { ref, onMounted } from "vue";
import travelplanApi from "@/api/travelplanApi";
import { useAuthStore } from "@/stores/user";

const props = defineProps(["comment", "plan_id"]);
const emit = defineEmits(["handleDelete"]);

const children = ref([]);

const isWriting = ref(false);
const authStore = useAuthStore();

const handlePostComment = async (data) => {
  await travelplanApi.post("/post-comment", data).then((res) => {
    if (res.status == 201) {
      console.log(children.value);
      children.value.push(res.data);
      isWriting.value = false;
    }
  });
};

const handle = (comment) => {
  if (comment.level > 0) {
    console.log("item에서:: " + comment.id);
    deleteComment(comment);
    console.log("?");
  }
  emit("handleDelete", comment);
};

const deleteComment = async (comment) => {
  const params = {
    comment_pk: comment.id,
  };

  await travelplanApi.delete("/delete-comment", { params }).then((res) => {
    if (res.status == 200 && comment.level == 0) {
      console.log("list에서:: " + comment.id);
      children.value = children.value.filter((item) => item.id != comment.id);
    }
  });
};

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
  <div>
    <div class="comment-container" :class="{ 'not-root': comment.level != 0 }">
      <div v-if="comment.level != 0">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="1.5"
          stroke="currentColor"
          class="reply"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="m16.49 12 3.75 3.75m0 0-3.75 3.75m3.75-3.75H3.74V4.499"
          />
        </svg>
      </div>
      <div>
        <div class="strong">
          {{ comment.user_id }}
        </div>
        <div class="comment-detail">{{ comment.comment }}</div>

        <div class="write-group">
          <div class="write-data">
            <span class="create-date">{{ comment.created_at }}</span>
            <div class="button" @click="isWriting = !isWriting">답글</div>
            <div
              class="button"
              v-if="authStore.user == comment.user_id"
              @click="handle(comment)"
            >
              삭제
            </div>
          </div>
        </div>
      </div>
    </div>

    <div
      v-if="isWriting"
      :class="{
        'write-input': comment.level != 0,
        'write-input-root': comment.level == 0,
      }"
    >
      <svg
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        stroke-width="1.5"
        stroke="currentColor"
        class="reply"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="m16.49 12 3.75 3.75m0 0-3.75 3.75m3.75-3.75H3.74V4.499"
        />
      </svg>

      <BoardCommentWrite
        @handle-submit="handlePostComment"
        :key="comment.id"
        :plan_id="plan_id"
        :parent_comment_id="comment.id"
      />
    </div>

    <BoardCommentItem
      v-for="child in children"
      :key="child.id"
      :comment="child"
      :plan_id="plan_id"
    />
  </div>
</template>

<style scoped>
.comment-container {
  padding: 1.25rem 0.5rem;
  border-bottom: 1px solid rgb(212 212 212);
}
.not-root {
  display: flex;
  gap: 0.5rem;
  padding-left: 60px;
}

.strong {
  font-weight: 700;
}

.write-group {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.write-group > .write-data {
  display: flex;
  gap: 1rem;
}

.write-group > .write-data > .create-date {
  min-width: fit-content;
  font-size: small;
}

.write-group > .write-data > .button {
  display: flex;
  font-weight: 700;
  cursor: pointer;
  color: rgb(107 114 128);
}

.comment-detail {
  padding: 0.5rem 0.25rem;
}

.write-input-root {
  display: flex;
  width: 100%;
}

.write-input {
  display: flex;
  width: 100%;
  padding-left: 60px;
}

.reply {
  width: 20px;
}
</style>
