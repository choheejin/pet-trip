import axios from "axios";

const mapApi = axios.create({
  baseURL: "http://localhost:8080/pet/plan",
});

mapApi.interceptors.request.use((config) => {
  console.log("요청 config", config);

  return config;
});

export default mapApi;
