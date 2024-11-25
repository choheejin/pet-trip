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
      const originalRequest = error.config;

      if (originalRequest.method == "get" && !originalRequest._retry) {
        originalRequest._retry = true;
        const authStore = useAuthStore();
        authStore.logout();

        delete originalRequest.headers.accessToken;

        return travelplanApi(originalRequest);
      }
    }
  }
);
export default travelplanApi;
