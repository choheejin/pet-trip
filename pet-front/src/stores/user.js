import { ref } from "vue";
import { defineStore } from "pinia";
import userApi from "@/api/userApi.js";
import { jwtDecode } from "jwt-decode";
import { useMenuStore } from "@/stores/menu";

export const useAuthStore = defineStore(
  "auth",
  () => {
    const token = ref(null);
    const user = ref(null);
    const menuStore = useMenuStore();

    const join = async (joinInfo) => {
      console.log("user.js에서 회원가입 확인 : ", joinInfo);
      const response = await userApi.post("/join", joinInfo);

      //토큰 정보 및 유저 정보 세팅(회원 가입 후, 로그인 따로 할 필요 없음)
      token.value = response.data;
      user.value = jwtDecode(token.value);
    };

    const login = async (loginInfo) => {
      console.log("User.js에서 확인 ", loginInfo);
      const response = await userApi.post("/login", loginInfo);

      //토큰 정보 및 유저 정보 세팅
      token.value = response.data["access-token"];
      user.value = jwtDecode(token.value);
      // console.log(token.value);

      // 로그인 성공 후 메뉴 상태 변경
      menuStore.changeLoginState(true);
    };

    const logout = () => {
      console.log("로그아웃하기~~");
      //토큰 정보 및 유저 정보 삭제
      token.value = null;
      user.value = null;

      menuStore.changeLoginState(false);
    };

    return { user, token, join, login, logout };
  },
  {
    persist: true, // 로컬 스토리지와 연동
  }
);