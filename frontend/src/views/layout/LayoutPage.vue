<script setup>
import { getChatMessageHistoryApi, toChatApi } from "@/api/ai"
import router from "@/router"
import { useMenuStore } from "@/stores/menu"
import { useUserStore } from "@/stores/user"
import { ArrowDown } from "@element-plus/icons-vue"

const userStore = useUserStore()
const menuStore = useMenuStore()
// 顶部导航
const handleCommand = (command) => {
  if (command === "退出") {
    userStore.logout()
    menuStore.reset_activated()
    router.push("/login")
  }
}

// 侧边菜单
const handleSelect = (key, keyPath) => {
  menuStore.set_activated(key)
  router.push(key)
}
</script>

<template>
  <div class="layout-root">
    <el-container>
      <el-aside class="el-aside">
        <div>
          <el-menu
            :default-active="menuStore.menu_activated"
            background-color="#ecf5ff"
            class="el-aside-menu"
            @select="handleSelect"
          >
            <el-menu-item index="/">
              <span>仪表盘</span>
            </el-menu-item>
            <el-menu-item index="/apply">
              <span>发起申请</span>
            </el-menu-item>
            <el-menu-item index="/my-apply">
              <span>我的申请</span>
            </el-menu-item>
            <el-menu-item index="/approve">
              <span>审批管理</span>
            </el-menu-item>
          </el-menu>
        </div>
      </el-aside>

      <el-container>
        <el-header class="el-header">
          <div>欢迎，{{ userStore.userInfo.account }}</div>
          <div class="el-header-dropdown">
            <el-dropdown @command="handleCommand">
              <span class="el-dropdown-link">
                <el-icon class="el-icon--right">
                  <arrow-down />
                </el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="退出">退出</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <el-main class="el-main">
          <router-view></router-view>
        </el-main>

        <el-footer class="el-footer"> Footer </el-footer>
      </el-container>
    </el-container>

    <div class="ai-box">
      <ai-assistant :chat="toChatApi" :history="getChatMessageHistoryApi"></ai-assistant>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.layout-root {
  height: 100vh;
  width: 100vw;
  display: flex;

  .el-aside {
    background-color: #ecf5ff;
    text-align: center;
    padding-top: 80px;

    .el-aside-menu {
      .el-menu-item {
        justify-content: center;
        font-size: 16px;
      }
    }
  }

  .el-main {
    background-color: #f4f4f7;
  }

  .el-header,
  .el-footer {
    background-color: #99a9bf;
  }

  .el-header {
    display: flex;
    align-items: center;
    // 使子元素居右
    justify-content: flex-end;
    gap: 10px;

    .el-header-dropdown {
      display: flex;
      align-items: center;
      flex-direction: column;
    }
  }
}
</style>
