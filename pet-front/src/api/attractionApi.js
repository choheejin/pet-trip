import axios from "axios";

const attractionApi = axios.create({
  baseURL: "http://localhost:8080/pet/attraction",
});

export default attractionApi;
