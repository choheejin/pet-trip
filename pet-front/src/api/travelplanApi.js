import axios from "axios";
import { useAuthStore } from "@/stores/user.js";

const travelplanApi = axios.create({
  baseURL: "http://localhost:8080/pet/plan",
});

travelplanApi.interceptors.request.use((config) => {
  const authStore = useAuthStore();
  const token = authStore.token;

  if (token != null) {
    config.headers.accessToken = token;
  }
  return config;
});

travelplanApi.interceptors.response.use(
  (config) => config,
  (error) => {
    if (error.status == 401) {
      alert("로그인 하세요");
    }
  }
);
export default travelplanApi;
