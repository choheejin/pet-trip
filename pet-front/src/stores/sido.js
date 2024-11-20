import { defineStore } from "pinia";

export const useSidoStore = defineStore("sido", {
  state: () => ({
    sido: [
      { sido_name: "서울", sido_code: 1 },
      { sido_name: "인천", sido_code: 2 },
      { sido_name: "대전", sido_code: 3 },
      { sido_name: "대구", sido_code: 4 },
      { sido_name: "광주", sido_code: 5 },
      { sido_name: "부산", sido_code: 6 },
      { sido_name: "울산", sido_code: 7 },
      { sido_name: "세종특별자치시", sido_code: 8 },
      { sido_name: "경기도", sido_code: 31 },
      { sido_name: "강원특별자치도", sido_code: 32 },
      { sido_name: "충청북도", sido_code: 33 },
      { sido_name: "충청남도", sido_code: 34 },
      { sido_name: "경상북도", sido_code: 35 },
      { sido_name: "경상남도", sido_code: 36 },
      { sido_name: "전북특별자치도", sido_code: 37 },
      { sido_name: "전라남도", sido_code: 38 },
      { sido_name: "제주도", sido_code: 39 },
    ],
  }),
});
