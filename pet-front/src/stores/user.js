import { ref } from "vue";
import { defineStore } from "pinia";
import userApi from "@/api/userApi.js";
import { jwtDecode } from "jwt-decode";
// import { useMenuStore } from "@/stores/menu";

export const useAuthStore = defineStore(
  "auth",
  () => {
    const token = ref(null);
    const user = ref(null);
    // const menuStore = useMenuStore();

    const initailizeInfo = () => {
      if (localStorage.getItem("authToken") == null) return;

      const decode = jwtDecode(localStorage.getItem("authToken"));
      token.value = localStorage.getItem("authToken");
      user.value = decode.user_id;
    };

    initailizeInfo();

    const join = async (joinInfo) => {
      // console.log("user.js에서 회원가입 확인 : ", joinInfo);
      const response = await userApi.post("/signup", joinInfo);
    };

    const login = async (loginInfo) => {
      // console.log("User.js에서 확인 ", loginInfo);
      const response = await userApi.post("/login", loginInfo);

      //토큰 정보 및 유저 정보 세팅
      token.value = response.data["access-token"];
      user.value = jwtDecode(token.value).user_id;

      localStorage.setItem("authToken", token.value);
      // console.log(token.value);

      // // 로그인 성공 후 메뉴 상태 변경
      // menuStore.changeLoginState(true);
      return response;
    };

    const logout = () => {
      // console.log("로그아웃하기~~");
      //토큰 정보 및 유저 정보 삭제
      localStorage.removeItem("authToken");

      token.value = null;
      user.value = null;

      // menuStore.changeLoginState(false);
    };

    const checkId = async (user_id) => {
      // console.log("유효성 검사 대상인 id : ", user_id);
      const response = await userApi.get(`/${user_id}`);
      return response.data;
    };

    return { user, token, join, login, logout, checkId };
  },
  {
    persist: true, // 로컬 스토리지와 연동
  }
);
