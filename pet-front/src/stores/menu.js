import { ref } from "vue";
import { defineStore } from "pinia";

export const useMenuStore = defineStore("menuStore", () => {
  // 사용자 로그인 상태 추적
  const isLoggedIn = ref(false); // 로그인 여부

  const menuList = ref([
    { name: "회원가입", show: true, routeName: "user-join" },
    { name: "로그인", show: true, routeName: "user-login" },
    { name: "지도", show: false, routeName: "map" },
    { name: "마이페이지", show: false, routeName: "mypage" },
    { name: "게시판", show: false, routeName: "board" },
  ]);

  // 로그인 상태 변경 함수
  const changeLoginState = (state) => {
    isLoggedIn.value = state;
    changeMenuState(); // 로그인 상태 변경 후 메뉴 상태 업데이트
  };

  // 메뉴 항목의 표시 여부를 로그인 상태에 따라 업데이트
  const changeMenuState = () => {
    menuList.value = menuList.value.map((item) => {
      // "지도"는 항상 보이게 하기
      if (item.routeName === "map") {
        return { ...item, show: true };
      }

      // 로그인 상태에 따른 메뉴 표시 설정
      if (isLoggedIn.value) {
        // 로그인 상태일 때 "로그인"과 "회원가입" 메뉴는 숨기기
        if (item.routeName === "user-login" || item.routeName === "user-join") {
          return { ...item, show: false };
        }

        // 로그인 상태일 때 "마이페이지"는 보이게 하기
        if (item.routeName === "mypage") {
          return { ...item, show: true };
        }

        // 로그인 상태에서 "게시판"은 보이게 하기
        if (item.routeName === "board") {
          return { ...item, show: true };
        }
      } else {
        // 로그인하지 않았을 때 "로그인", "회원가입", "게시판" 보이게 하기
        if (
          item.routeName === "user-login" ||
          item.routeName === "user-join" ||
          item.routeName === "board"
        ) {
          return { ...item, show: true };
        }

        // 로그인하지 않았을 때 "마이페이지"는 숨기기
        if (item.routeName === "mypage") {
          return { ...item, show: false };
        }
      }

      return item; // 기본적으로 다른 항목들은 상태 반전
    });
  };

  changeMenuState(); // 초기 메뉴 상태 설정

  return {
    menuList,
    changeLoginState,
    isLoggedIn,
  };
});
