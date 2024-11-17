import axios from "axios";

const userApi = axios.create({
  baseURL: "http://localhost:8080/pet/user"
})

export default userApi;
