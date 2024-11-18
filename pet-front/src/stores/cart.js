import { defineStore } from "pinia";

export const useCartStore = defineStore("cart", {
  state: () => ({
    attraction: [],
  }),
  actions: {
    setAttraction(attraction) {
      this.attraction = attraction;
    },
  },
});
