import { ref } from "vue";
import { defineStore } from "pinia";
import userApi from "@/api/userApi.js";
import { jwtDecode } from "jwt-decode";

export const useAuthStore = defineStore(
  "auth",
  () => {
    const token = ref(null);
    const user = ref(null);

    const join = async (joinInfo) => {
      const response = await userApi.post("/join", joinInfo);

      //토큰 정보 및 유저 정보 세팅(회원 가입 후, 로그인 따로 할 필요 없음)
      token.value = response.data;
      user.value = jwtDecode(token.value);
    };

    const login = async (loginInfo) => {
      console.log("User.js에서 확인 ",loginInfo)
      const response = await userApi.post("/login", loginInfo);
      console.log(response)

      //토큰 정보 및 유저 정보 세팅
      token.value = response.data;
      user.value = jwtDecode(token.value);
    };

    const logout = () => {
      //토큰 정보 및 유저 정보 삭제
      token.value = null;
      user.value = null;
    };

    return { user, token, join, login, logout };
  },
  {
    persist: true, // 로컬 스토리지와 연동
  }
);
