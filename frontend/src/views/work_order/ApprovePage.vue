<script setup>
import { approveLeaveApi, approvePageListApi } from "@/api/approval"
import { reactive, ref } from "vue"

const tableData = ref([])
const tableDataColumns = ref([
  { label: "标题", prop: "title", width: "180" },
  { label: "类型", prop: "typeName", width: "180" },
  { label: "状态", prop: "status" },
  { label: "创建时间", prop: "createTime" },
])
const totalData = ref(0)
const param = reactive({
  pageNum: 1,
  pageSize: 10,
})
const initPageData = async () => {
  try {
    let res = await approvePageListApi(param)

    if (res.code === 200) {
      tableData.value = res.data
      totalData.value = res.total
    } else {
      ElMessage.error(res.message)
    }
  } catch (error) {
    ElMessage.error(error.message)
  }
}
initPageData()

const centerDialogVisible = ref(false)
const detailData = ref({})
const dialogFormColumns = ref([
  { label: "标题", prop: "title", readonly: true },
  { label: "类型", prop: "typeName", readonly: true },
  { label: "状态", prop: "status", readonly: true },
  { label: "起止时间", prop: "dateRange", readonly: true },
  { label: "创建时间", prop: "createTime", readonly: true },
  {
    label: "原因",
    prop: "reason",
    type: "textarea",
    readonly: true,
    resize: "none",
    autosize: { minRows: 5, maxRows: 5 },
  },
])

const handleApprove = (row) => {
  console.log(row)
  // ElMessage.success(JSON.stringify(row))

  detailData.value = {
    id: row.id,
    title: row.title,
    typeName: row.typeName,
    status: row.status,
    createTime: row.createTime,
    reason: row.reason,
    dateRange: row.startTime + " - " + row.endTime,
    taskId: row.taskId,
  }
  centerDialogVisible.value = true
}

const onDialogClose = () => {
  detailData.value = {}
}

const onSubmitButton = async () => {
  try {
    param.id = detailData.value.id
    param.taskId = detailData.value.taskId
    param.flag = true
    let res = await approveLeaveApi(param)

    if (res.code === 200) {
      ElMessage.success("提交成功")
    } else {
      ElMessage.error(res.message)
    }
  } catch (error) {
    ElMessage.error(error.message)
  }
  
  initPageData()
  centerDialogVisible.value = false
}

const onCloseButton = () => {
  initPageData()
  centerDialogVisible.value = false
  detailData.value = {}
  ElMessage.success("取消成功")
}

const onPaginationChange = (page) => {
  tableData.value = []

  for (let i = 1; i <= 10; i++) {
    let index = (page - 1) * 10 + i
    tableData.value.push({
      id: index,
      title: "标题" + index,
      type: "类型",
      date: "2021-01-01",
      status: "状态",
      createTime: "2021-01-01 00:00:00",
    })
  }
}
</script>

<template>
  <div class="approve-root">
    <div class="container">
      <!-- <div>搜索部分</div> -->

      <div class="table-box">
        <iams-table
          v-model:data="tableData"
          :dataColumns="tableDataColumns"
          :useLastColumn="true"
        >
          <template #default="scope">
            <el-button size="small" @click="handleApprove(scope.row)">
              审批
            </el-button>
          </template>
        </iams-table>
      </div>

      <div class="pagination-box">
        <pagination
          :total="totalData"
          :onPageChange="onPaginationChange"
        ></pagination>
      </div>

      <div class="dialog-form-box">
        <dialog-form
          v-model:isShow="centerDialogVisible"
          v-model:data="detailData"
          :dataColumns="dialogFormColumns"
          :onClose="onDialogClose"
          :useButton="true"
        >
          <template #default>
            <el-button type="primary" @click="onSubmitButton">通过</el-button>
            <el-button @click="onCloseButton">拒绝</el-button>
          </template>
        </dialog-form>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.approve-root {
  height: 100%;
  background-color: #ffffff;

  .container {
    padding: 20px;

    ::v-deep(.el-form) {
      .el-form-item__content {
        justify-content: center;
      }
    }
  }
}
</style>
