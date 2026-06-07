// src/router/index.js
import { createRouter, createWebHistory } from "vue-router"
// 假设你有一个获取登录状态的工具函数或 store
import { useUserStore } from "@/stores/user"

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/login",
      name: "login",
      component: () => import("@/views/login/LoginPage.vue"),
    },
    {
      path: "/",
      name: "layout",
      component: () => import("@/views/layout/LayoutPage.vue"),
      meta: { requiresAuth: true }, // 建议添加元信息，标记需要登录
      redirect: "/dashboard",
      children: [
        {
          path: "/dashboard",
          name: "home",
          component: () => import("@/views/home/HomePage.vue"),
        },
        {
          path: '/apply',
          name: 'apply',
          component: () => import('@/views/work_order/ApplyPage.vue')
        },
        {
          path: '/my-apply',
          name: 'my_apply',
          component: () => import('@/views/work_order/MyApplyPage.vue') 
        },
        {
          path: '/approve',
          name: 'approve',
          component: () => import('@/views/work_order/ApprovePage.vue')
        }
      ],
    },
  ],
})

router.beforeEach(async (to, from) => {
  let userStore = useUserStore()
  let isAuthenticated = userStore.token // 或者检查 localStorage

  // 如果目标路由需要认证
  if (to.meta.requiresAuth && !isAuthenticated) {
    // 将用户重定向到登录页面，并保留原路径以便登录后跳回
    return { name: "login", query: { redirect: to.fullPath } }
  }

  // 如果已登录且试图访问登录页，可重定向到首页
  if (to.name === "login" && isAuthenticated) {
    return { name: "home" }
  }
})

export default router
