import BaseMap from "@/views/BaseMap.vue";
import { createRouter, createWebHistory } from "vue-router";
import TheMainView from "@/views/TheMainView.vue";
import TheBoardView from "@/views/TheBoardView.vue";
import TheMyPageView from "@/views/TheMyPageView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "main",
      component: TheMainView,
      meta: { requiresAuth: false },
    },
    {
      path: "/map",
      name: "BaseMap",
      component: BaseMap,
      meta: { requiresAuth: false },
    },
    {
      path: "/board",
      name: "Board",
      component: TheBoardView,
      meta: { requiresAuth: false },
    },
    {
      path: "/mypage",
      name: "MyPage",
      component: TheMyPageView,
      meta: { requiresAuth: true },
    },
  ],
});

export default router;
