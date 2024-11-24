import { useAuthStore } from "@/stores/user";
import axios from "axios";

const attractionApi = axios.create({
  baseURL: "http://localhost:8080/pet/attraction",
});

attractionApi.interceptors.request.use((config) => {
  const authStore = useAuthStore();
  if (config.url == "/openapi-detail" && authStore.token) {
    config.headers.accessToken = authStore.token;
  }
  if (config.method === "post" || config.method === "delete") {
    config.headers.accessToken = authStore.token;
  }
  return config;
});

export default attractionApi;
