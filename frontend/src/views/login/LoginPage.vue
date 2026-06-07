<script setup>
import { ref } from "vue"
import { CloseBold, Right } from "@element-plus/icons-vue"
import { loginApi, registerApi, shiciApi } from "@/api/login"
import { useUserStore } from "@/stores/user"
import { useRoute } from "vue-router"
import router from "@/router"

const shici = ref({})
const init_pageData = async () => {
  try {
    let res = await shiciApi()

    if (res.status === 1) {
      shici.value = res.data
      // console.log(shici.value)
    } else {
      ElMessage.error(res.message)
    }
  } catch (error) {
    ElMessage.error(error.message)
    throw error
  }
}
init_pageData()

// ============================================================================
const account = ref("")
const pwd = ref("")
const pwd2 = ref("")
const account_reg = /^[a-zA-Z][a-zA-Z0-9]{5,9}$/
// prettier-ignore
const pwd_reg = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*.])[a-zA-Z\d!@#$%^&*.]{6,16}$/
const isRegister = ref(false)

const userStore = useUserStore()
const route = useRoute()
const to_login = async () => {
  // 去除空格
  // account.value = account.value.trim()
  // pwd.value = pwd.value.trim()

  // 空值判断
  if (!account.value || !pwd.value) {
    ElMessage.error("账户或密码不能为空")
    return
  }

  // 账户正则校验，英文字母、数字，必须以英文字母开头
  if (!account_reg.test(account.value)) {
    ElMessage.error("账户格式错误")
    return
  }

  // 密码正则校验，长度6-16，必须包含数字、字母、特殊字符
  if (!pwd_reg.test(pwd.value)) {
    ElMessage.error("密码格式错误")
    return
  }

  try {
    let res = await loginApi({
      account: account.value,
      password: pwd.value,
    })

    if (res.code === 200) {
      userStore.setToken(res.data.token)
      userStore.setUserInfo(res.data.user)

      let redirect = route.query.redirect
      router.push(redirect || "/")
      ElMessage.success("登录成功")
    } else {
      ElMessage.error(res.message || "登录失败")
    }
  } catch (error) {
    ElMessage.error("网络错误，请稍后重试")
  }
}

const to_register = async () => {
  // 去除空格
  // account.value = account.value.trim()
  // pwd.value = pwd.value.trim()
  // pwd2.value = pwd2.value.trim()

  // 账户、密码空判断
  if (!account.value || !pwd.value) {
    ElMessage.error("账户或密码不能为空")
    return
  }

  // 账户正则校验
  if (!account_reg.test(account.value)) {
    ElMessage.error("账户格式不正确")
    return
  }

  // 密码正则校验
  if (!pwd_reg.test(pwd.value)) {
    ElMessage.error("密码格式不正确")
    return
  }

  // 2次密码等价校验
  if (pwd.value !== pwd2.value) {
    ElMessage.error("两次密码不一致")
    return
  }

  try {
    let res = await registerApi({
      account: account.value,
      password: pwd.value,
    })

    if (res.code === 200) {
      ElMessage.success("注册成功")
      isRegister.value = false
      // 清空表单
      account.value = ""
      pwd.value = ""
      pwd2.value = ""
    } else {
      ElMessage.error(res.message || "注册失败")
    }
  } catch (error) {
    ElMessage.error("网络错误，请稍后重试")
  }
}
</script>

<template>
  <div class="login-root">
    <div class="login-box">
      <div class="header-box">
        <h3>{{ isRegister ? "注册" : "登录" }}</h3>
      </div>

      <div class="form-box">
        <li>
          <div>
            <el-input
              v-model.trim="account"
              clearable
              :clear-icon="CloseBold"
              placeholder="Please input account"
            />
          </div>
          <div class="tips" v-if="isRegister">
            *长度6-10，字母、数字，必须以英文字母开头
          </div>
        </li>
        <li>
          <el-input
            v-model.trim="pwd"
            type="password"
            placeholder="Please input password"
            show-password
          />
          <div class="tips" v-if="isRegister">
            *长度6-16，必须包含数字、字母、特殊字符
          </div>
        </li>
        <li v-if="isRegister">
          <el-input
            v-model.trim="pwd2"
            type="password"
            placeholder="Please input password again"
            show-password
          />
        </li>
      </div>

      <div class="btn-box">
        <el-button
          @click="isRegister ? to_register() : to_login()"
          type="primary"
        >
          {{ isRegister ? "Register" : "Login" }}
        </el-button>
      </div>

      <div class="footer-box">
        <div class="register-box">
          <el-link
            @click="
              () => {
                isRegister = !isRegister
                account = ''
                pwd = ''
                pwd2 = ''
              }
            "
          >
            {{ isRegister ? "登录" : "注册" }}<el-icon><Right /></el-icon>
          </el-link>
        </div>

        <div class="forget-box">
          <el-link v-if="!isRegister" type="primary">忘记密码？</el-link>
        </div>
      </div>

      <div class="other-box" v-if="!isRegister">
        <div class="content">{{ shici.content }}</div>
        <div class="author">--{{ shici.author }}《{{ shici.title }}》</div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.login-root {
  height: 100vh;
  width: 100vw;
  // background-image: url('/src/assets/background_01.jpg');
  // background-image: url("/src/assets/background_02.png");
  // background-image: url("/src/assets/background_03.jpeg");
  background-image: url("/src/assets/background_04.png");
  // background-color: #25aff3;

  /* 关键属性：让背景图覆盖整个容器 */
  background-size: cover; /* 保持宽高比并填满容器，可能会裁剪部分内容 */
  background-position: center; /* 背景图居中显示 */
  background-repeat: no-repeat; /* 防止背景图重复平铺 */

  .login-box {
    display: flex;
    flex-direction: column;
    position: absolute;
    width: 240px;
    height: 400px;
    background-color: white;
    border-radius: 10px;
    justify-content: center;
    // align-items: center;
    top: 25vh;
    right: 20vw;
    padding: 0 80px;

    .header-box {
      h3 {
        margin-top: 0;
        text-align: center;
        font-size: 20px;
      }
    }

    .form-box {
      list-style: none; /* 移除项目符号 */
      padding: 0; /* 移除默认内边距 */
      margin: 0; /* 移除默认外边距 */

      li {
        margin: 10px 0; /* 可选：添加自定义间距 */
        display: flex;
        flex-direction: column;
        justify-content: center;
        .tips {
          font-size: 10px;
          color: #999;
        }
      }
    }
    .btn-box {
      display: flex;
      justify-content: center;
      button {
        width: 100%;
      }
    }
    .footer-box {
      display: flex;
      justify-content: space-between;
      margin-top: 20px;
    }
    .other-box {
      display: flex;
      // justify-content: center;
      flex-direction: column;
      margin-top: 20px;
      .content {
        font-size: 14px;
        color: #999;
      }

      .author {
        font-size: 14px;
        color: #999;
        text-align: right;
      }
    }
  }
}
</style>
