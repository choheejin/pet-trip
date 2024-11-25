import BaseMap from "@/views/BaseMap.vue";
import { createRouter, createWebHistory } from "vue-router";
import TheMainView from "@/views/TheMainView.vue";
import TheBoardView from "@/views/TheBoardView.vue";
import TheMyPageView from "@/views/TheMyPageView.vue";
import PostTravelPlan from "@/components/mypage/mypages/PostTravelPlan.vue";
import LikeAttractions from "@/components/mypage/mypages/LikeAttractions.vue";
import LikeTravelPlan from "@/components/mypage/mypages/LikeTravelPlan.vue";
import BoardTravelPlanDetail from "@/components/board/BoardTravelPlanDetail.vue";
import ThePlanReview from "@/views/ThePlanReview.vue";
import PlanReviewWrite from "@/components/review/PlanReviewWrite.vue";
import PlanReviewDetail from "@/components/review/PlanReviewDetail.vue";


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
      path: "/planDetail",
      name: "PlanDetail",
      component: BoardTravelPlanDetail,
      meta: { requiresAuth: false },
    },
    {
      path: "/review",
      name: "Review",
      component: ThePlanReview,
      meta: {requiresAuth: false}
    },
    {
      path: "/review-detail",
      name: "ReviewDetail",
      component: PlanReviewDetail,
      meta: {requiresAuth: false}
    },
    {
      path: "/mypage",
      name: "MyPage",
      redirect: "/post-travel-plan", // 기본 active router
      component: TheMyPageView,
      meta: { requiresAuth: true },
      children: [
        {
          path: "/post-travel-plan",
          name: "PostTravelPlan",
          component: PostTravelPlan,
          meta: { requiresAuth: true },
        },
        {
          path: "/like-attraction",
          name: "LikeAttractions",
          component: LikeAttractions,
          meta: { requiresAuth: true },
        },
        {
          path: "/like-travel-plan",
          name: "LikeTravelPlan",
          component: LikeTravelPlan,
          meta: { requiresAuth: true },
        },
      ],
    },
    {
      path: "/plan-review-write/:plan_id",
      name: "PlanReviewWrite",
      component: PlanReviewWrite,
      meta: {requiresAuth: true}
    }
  ],
});

export default router;
