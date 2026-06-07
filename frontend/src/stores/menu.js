import { defineStore } from "pinia"
import { ref } from "vue"

export const useMenuStore = defineStore("IAMS-menu", () => {
  let menu_activated = ref("/")

  let set_activated = (activated) => {
    menu_activated.value = activated
  }

  let reset_activated = () => {
    menu_activated.value = "/"
  }

  return { menu_activated, set_activated, reset_activated }
},{
    persist: {
        storage: sessionStorage
    }
})
