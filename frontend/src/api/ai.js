import request from "@/utils/request"

export const toChatApi = (data) => {
  return request.post("/api/v1/ai/chat", data)
}

export const getChatMessageHistoryApi = (data) => {
  return request.post("/api/v1/ai/chat/history", data)
}

export const clearChatMessageHistoryApi = (data) => {
  return request.post("/api/v1/ai/chat/clear", data)
}

