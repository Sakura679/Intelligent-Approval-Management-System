import { defineStore } from "pinia"
import { ref } from "vue"

export const useUserStore = defineStore(
  "IAMS-user",
  () => {
    let token = ref("")
    let userInfo = ref({})

    let setToken = (value) => {
      token.value = value
    }

    let setUserInfo = (user) => {
      userInfo.value = user
    }

    let logout = () => {
      token.value = ""
      userInfo.value = {}
    }

    return { token, userInfo, setToken, setUserInfo, logout }
  },
  { persist: true },
)
