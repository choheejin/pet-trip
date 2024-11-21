import axios from "axios";
import {useAuthStore} from "@/stores/user.js";

const travelplanApi = axios.create({
  baseURL: "http://localhost:8080/pet/plan",
});

travelplanApi.interceptors.request.use((config) => {
  const authStore = useAuthStore();
  const token = authStore.token;
  if (token != null) {
    config.headers.accessToken = token;
  }
  // console.log("인터셉터 작동");
  // console.log(config.headers);
  return config;
});
export default travelplanApi;
