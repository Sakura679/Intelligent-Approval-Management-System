import request from "@/utils/request"

export const loginApi = (data) => {
  return request.post("/api/v1/user/login", data)
}

export const registerApi = (data) => {
  return request.post("/api/v1/user/register", data)
}

export const shiciApi = () => {
  return request.get("/shici/home/shici/ajax")
}


