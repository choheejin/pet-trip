import axios from "axios";

const travelplanApi = axios.create({
  baseURL: "http://localhost:8080/pet/plan/plans",
});

export default travelplanApi;
