import BaseMap from "@/components/BaseMap.vue";
import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/map",
      name: "BaseMap",
      component: BaseMap,
      meta: { requiresAuth: false },
    },
  ],
});

export default router;
