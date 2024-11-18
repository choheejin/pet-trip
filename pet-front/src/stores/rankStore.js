// stores/rankStore.js
import { defineStore } from "pinia";
import axios from "axios";

export const useRankStore = defineStore("rankStore", {
  state: () => ({
    hotPlaces: [],
    travelPlans: [],
    loading: false,
    error: null,
  }),
  actions: {
    async fetchHotPlaces() {
      this.loading = true;
      try {
        const response = await axios.get(
          "http://localhost:8080/pet/attraction/hotplace-ranking"
        ); // 실제 API URL로 변경
        console.log("핫 플레이스 순위~~", response);
        this.hotPlaces = response.data;
      } catch (error) {
        this.error = error;
      } finally {
        this.loading = false;
      }
    },
    async fetchTravelPlans() {
      this.loading = true;
      try {
        const response = await axios.get(
          "http://localhost:8080/pet/attraction/plan-ranking"
        ); // 실제 API URL로 변경
        console.log("좋아요 많은 여행코스~~", response);
        this.travelPlans = response.data;
      } catch (error) {
        this.error = error;
      } finally {
        this.loading = false;
      }
    },
  },
  getters: {
    sortedHotPlaces: (state) => {
      return state.hotPlaces.sort((a, b) => a.name.localeCompare(b.name));
    },
    sortedTravelPlans: (state) => {
      return state.travelPlans.sort((a, b) => a.name.localeCompare(b.name));
    },
  },
});
