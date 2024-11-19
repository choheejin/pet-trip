import { defineStore } from 'pinia';

export const useMainSelectStore = defineStore('mainselect', {
  state: () => ({
    selectedSidoCode: '',  // 선택된 지역 코드
    selectedDogSize: '',   // 선택된 강아지 크기
    selectedAttraction: '', // 선택된 핫플레이스
    selectedTravelPlan: '', // 선택된 여행 계획
  }),
  actions: {
    setSidoCode(code) {
      this.selectedSidoCode = code;
    },
    setDogSize(size) {
      this.selectedDogSize = size;
    },
    setAttraction(content_id){
      this.selectedAttraction = content_id
    },
    setTravelPlan(id) {
      this.selectedTravelPlan = id
    },
  },
});
