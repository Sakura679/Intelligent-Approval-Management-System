<script setup>
import { applyLeaveApprovalApi } from "@/api/approval"
import dayjs from "dayjs"
import { ref } from "vue"

// do not use same name with ref
const form = ref({
  title: "",
  reason: "",
  dateRange: null,
  typeId: null,
})
const dateFormat = "YYYY-MM-DD HH:mm:ss"

const onSubmit = async () => {
  // 参数校验
  if (!form.value.title) {
    ElMessage.error("请填写标题")
    return
  }
  if (!form.value.reason) {
    ElMessage.error("请填写申请原因")
    return
  }
  if (!form.value.dateRange) {
    ElMessage.error("请选择时间")
    return
  }
  if (!form.value.typeId) {
    ElMessage.error("请选择申请类型")
    return
  }

  try {
    let res = {}
    if (form.value.typeId === 1) {
      res = await applyLeaveApprovalApi({
        title: form.value.title,
        reason: form.value.reason,
        startTime: dayjs(form.value.dateRange[0]).format(dateFormat),
        endTime: dayjs(form.value.dateRange[1]).format(dateFormat),
        typeId: form.value.typeId,
      })
    }

    if (res.code === 200) {
      ElMessage.success("submit!")
      form.value = {}
    } else {
      ElMessage.error(res.message)
    }
  } catch (error) {
    ElMessage.error(error.message)
  }
}
</script>

<template>
  <div class="apply-root">
    <div class="container">
      <div class="form-box">
        <el-form :model="form" label-width="auto">
          <el-form-item label="标题" required>
            <el-input v-model.trim="form.title" />
          </el-form-item>

          <el-form-item label="申请类型" required>
            <el-select v-model="form.typeId" placeholder="选择申请类型">
              <el-option label="请假" :value="1" />
              <el-option label="报销" :value="2" />
            </el-select>
          </el-form-item>

          <el-form-item label="起止时间" required>
            <el-date-picker
              v-model="form.dateRange"
              type="datetimerange"
              start-placeholder="Start date"
              end-placeholder="End date"
              format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>

          <el-form-item label="申请原因" required>
            <el-input
              v-model.trim="form.reason"
              type="textarea"
              :autosize="{ minRows: 5, maxRows: 5 }"
              resize="none"
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="onSubmit">submit</el-button>
            <el-button @click="form = {}">reset</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.apply-root {
  display: flex;
  justify-content: center;
  background-color: #ffffff;
  height: 100%;

  .container {
    padding: 20px;

    .form-box {
      ::v-deep(.el-form) {
        width: 40vw;

        .el-form-item {
          .el-form-item__content {
            justify-content: center;
          }

          // 隐藏文本域滚动条，保留滚动功能
          .el-textarea__inner {
            // 1. 允许垂直滚动，但不要强制显示滚动条
            overflow-y: auto !important;
            overflow-x: hidden;

            // 2. Firefox 隐藏滚动条
            scrollbar-width: none;

            // 3. IE/Edge 旧版隐藏滚动条
            -ms-overflow-style: none;
          }

          // 4. Chrome, Safari, Edge (Webkit) 隐藏滚动条
          .el-textarea__inner::-webkit-scrollbar {
            display: none;
          }
        }
      }
    }
  }
}
</style>
