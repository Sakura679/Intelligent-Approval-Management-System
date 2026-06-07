import router from "@/router"
import { useUserStore } from "@/stores/user"
import axios from "axios"

const instance = axios.create({
  // baseURL: "http://localhost:8081/api/v1",
  timeout: 60000,
  headers: {
    "Content-Type": "application/json",
  },
})

// 添加请求拦截器
instance.interceptors.request.use(
  (config) => {
    // 在发送请求之前做些什么

    // 1. 对 /login 请求进行过滤
    // 2. 添加 token
    if (config.url !== "/login") {
      let userStore = useUserStore()
      let token = userStore.token
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }
    }

    return config
  },
  (error) => {
    // 对请求错误做些什么
    return Promise.reject(error)
  },
)

// 添加响应拦截器
instance.interceptors.response.use(
  (response) => {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么

    let status = response.status
    // 对响应数据进行解包
    if (status === 200) {
      return response.data
    }

    return response
  },
  (error) => {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么

    let status = error.response.status
    // 身份认证失败、过期处理
    if (status === 401 || status === 403) {
      let userStore = useUserStore()
      userStore.logout()

      if (router.currentRoute.value.name !== "login") {
        router.push({
          name: "login",
          query: { redirect: router.currentRoute.value.fullPath },
        })
      }
    }
    return Promise.reject(error)
  },
)

export default instance
