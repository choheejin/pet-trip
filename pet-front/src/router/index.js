import BaseMap from "@/views/BaseMap.vue";
import { createRouter, createWebHistory } from "vue-router";
import TheMainView from "@/views/TheMainView.vue";
import TheBoardView from "@/views/TheBoardView.vue";

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
      path:"/board",
      name: "Board",
      component: TheBoardView,
      meta : {requiresAuth: false}
    }
  ],
});

export default router;
