import BaseMap from "@/components/map/BaseMap.vue";
import { createRouter, createWebHistory } from "vue-router";
import TheMainView from "@/views/TheMainView.vue";

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
  ],
});

export default router;
