import { useAuthStore } from "@/stores/user";
import axios from "axios";

const reviewApi = axios.create({
  baseURL: "http://localhost:8080/pet/review",
});

reviewApi.interceptors.request.use((config) => {
  const authStore = useAuthStore();
  const token = authStore.token;
  if (token != null) {
    config.headers.accessToken = token;
  }
  // console.log("인터셉터 작동");
  // console.log(config.headers);
  return config;
});

reviewApi.interceptors.response.use(
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

export default reviewApi;
