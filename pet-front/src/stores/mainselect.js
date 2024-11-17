import { defineStore } from 'pinia';

export const useMainSelectStore = defineStore('mainselect', {
  state: () => ({
    selectedSidoCode: '',  // 선택된 지역 코드
    selectedDogSize: '',   // 선택된 강아지 크기
  }),
  actions: {
    setSidoCode(code) {
      this.selectedSidoCode = code;
    },
    setDogSize(size) {
      this.selectedDogSize = size;
    },
  },
});
