import { useAuthStore } from "@/stores/user";
import axios from "axios";

const mapApi = axios.create({
  baseURL: "http://localhost:8080/pet/plan",
});

mapApi.interceptors.request.use((config) => {
  // 요청이 POST일 때만 Authorization 헤더 추가
  const authStore = useAuthStore();
  if (config.method === "POST") {
    const token = authStore.token;
    config.headers.accessToken = token;
  }
  return config;
});

mapApi.interceptors.response.use(
  (config) => config,
  (error) => {
    console.log(error);
    if (error.status == 401) {
      const authStore = useAuthStore();
      authStore.logout();
      alert("로그인이 필요합니다");
    }

    return Promise.reject(error);
  }
);

export default mapApi;
