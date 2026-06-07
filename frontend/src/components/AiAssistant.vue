<script setup>
import { nextTick, onMounted, ref } from "vue"
import { Service, Close, Position } from "@element-plus/icons-vue"
import { marked } from "marked"

const props = defineProps({
  history: {
    type: Function,
    required: true,
  },
  chat: {
    type: Function,
    required: true,
  },
})

// 页面ai助手
const isShowChat = ref(false)
const chatInput = ref("")
const chatMessages = ref([])
const chatBodyRef = ref(null)

const scrollToBottom = () => {
  if (chatBodyRef.value) {
    chatBodyRef.value.scrollTop = chatBodyRef.value.scrollHeight
  }
}

const toChat = async () => {
  try {
    let res = await props.history({})

    if (res.code === 200) {
      chatMessages.value = res.data.map((item) => {
        return {
          content: item.type == "ai" ? marked(item.content) : item.content,
          type: item.type,
        }
      })

      isShowChat.value = true

      await nextTick()
      scrollToBottom()
    } else {
      ElMessage.error(res.message)
    }
  } catch (error) {
    ElMessage.error(error.message)
  }
}

// 在 sendChat 中调用
const sendChat = async () => {
  if (chatInput.value.trim() === "") {
    ElMessage.warning("请输入内容")
    return
  }

  const userMsg = chatInput.value
  chatInput.value = "" // 先清空输入框

  // 添加用户消息
  chatMessages.value.push({
    content: [{ type: "text", text: userMsg }],
    type: "human",
  })

  // 等待 DOM 更新后滚动到底部
  await nextTick()
  scrollToBottom()

  // 发送消息
  try {
    let res = await props.chat({
      request: userMsg,
      imageUrl: "",
    })

    if (res.code === 200) {
      // 添加机器人消息
      // const htmlContent = marked(res.data)
      // chatMessages.value.push({
      //   content: htmlContent,
      //   type: "ai",
      // })

      // 滚动到底部
      await toChat()
      // await nextTick()
      // scrollToBottom()
    } else {
      ElMessage.error(res.message)
    }
  } catch (error) {
    ElMessage.error(error.message)
  }
}

onMounted(() => {
  // 初始加载时也滚动到底部
  nextTick(() => {
    scrollToBottom()
  })
})
</script>

<template>
  <div class="ai-box">
    <div class="ai-box-icon-box" @click="toChat" v-show="!isShowChat">
      <div class="ai-box-content">
        <el-icon :size="30"><Service /></el-icon>
      </div>
      <div class="ai-box-footer">ai小助手</div>
    </div>

    <div class="ai-box-chat-box" v-show="isShowChat">
      <div class="chat-header">
        <span>AI 助手</span>
        <el-icon @click.stop="isShowChat = false">
          <Close />
        </el-icon>
      </div>

      <div class="chat-body" ref="chatBodyRef">
        <div
          class="message-item"
          :style="item.type == 'human' ? 'justify-content: end' : ''"
          v-for="(item, index) in chatMessages"
          :key="index"
        >
          <div class="message-item-content-box" v-if="item.type == 'ai'">
            <div class="message-item-content" v-html="item.content"></div>
          </div>

          <div
            class="message-item-content-box"
            :style="'justify-content: end'"
            v-else
          >
            <div
              class="message-item-content"
              style="padding-top: 5px; padding-bottom: 5px"
              v-for="(item_item, index) in item.content"
              :key="index"
            >
              <div
                v-if="item_item.type == 'text'"
                v-html="item_item.text"
              ></div>

              <div v-else-if="item_item.type == 'image'">
                <img :src="item_item.url" />
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="chat-footer">
        <el-input
          placeholder="请输入内容"
          v-model.trim="chatInput"
          @keyup.enter="sendChat"
        >
          <template #append>
            <el-button @click="sendChat" :icon="Position" />
          </template>
        </el-input>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.ai-box {
  // height: 40px;
  // width: 40px;
  position: absolute;
  right: 30px;
  bottom: 200px;
  // background-color: #0064ff;
  background-color: unset !important;
  z-index: 999;
  max-width: 400px;
  // transition: all 0.3s ease; /* 平滑动画 */
  // cursor: move; // 显示为可拖动的手型
  // border-radius: 50%; /* 可选：让它变成圆形更像助手图标 */
  // box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2); /* 可选：加点阴影 */

  .ai-box-icon-box {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    cursor: pointer;

    .ai-box-content {
      color: white;
      background-color: #0064ff;
      border-radius: 50%; /* 可选：让它变成圆形更像助手图标 */
      width: 40px;
      height: 40px;
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .ai-box-footer {
      font-size: 14px;
    }
  }

  .ai-box-chat-box {
    background-color: #f3f6fb;
    padding: 20px;
    width: 350px;
    height: 500px;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
    position: relative;
    overflow: hidden;
    display: flex;
    flex-direction: column;

    .chat-header {
      display: flex;
      justify-content: space-between; // 左右两端对齐
      align-items: center; // 垂直居中
      margin-bottom: 15px;
      font-weight: bold;
      color: #333;

      ::v-deep(.el-icon) {
        font-size: 16px;
        color: #999;
        cursor: pointer;
        transition: color 0.2s;

        &:hover {
          color: #ff4d4f;
        }
      }
    }

    .chat-body {
      flex-grow: 1;
      overflow-y: auto;
      background-color: white;
      border-radius: 8px;
      scroll-behavior: smooth;

      .message-item {
        width: 100%;
        display: flex;

        .message-item-content-box {
          width: 80%;
          display: flex;

          .message-item-content {
            margin: 10px;
            padding: 0 10px;
            background-color: cornflowerblue;
            border-radius: 20px;
            align-items: center;
            color: white;
            font-size: 13px;
          }
        }
      }
    }

    .chat-footer {
      margin-top: 10px;
    }
  }
}
</style>
