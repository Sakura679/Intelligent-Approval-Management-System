<script setup>
import { leaveApprovalPageListApi, traceApi } from "@/api/approval"
import dayjs from "dayjs"
import { ref } from "vue"
import { useUserStore } from "@/stores/user"

const tableData = ref([])
const tableDataColumns = ref([
  { label: "标题", prop: "title", width: "180" },
  { label: "类型", prop: "typeName", width: "180" },
  { label: "状态", prop: "status" },
  { label: "创建时间", prop: "createTime" },
])
const totalData = ref(0)
const param = ref({
  pageNum: 1,
  pageSize: 10,
})

const initPageData = async () => {
  try {
    if (param.value.dateRange) {
      param.value.createTimeMin = dayjs(param.value.dateRange[0]).format(
        "YYYY-MM-DD HH:mm:ss",
      )
      param.value.createTimeMax = dayjs(param.value.dateRange[1]).format(
        "YYYY-MM-DD HH:mm:ss",
      )
    }
    let res = await leaveApprovalPageListApi(param.value)

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

const isShowDialogFormBox = ref(false)
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
const imageUrl = ref("")
const isShowDialogImgBox = ref(false)

const handleWatch = (row) => {
  // console.log(row)
  // ElMessage.success(JSON.stringify(row))

  detailData.value = {
    id: row.id,
    title: row.title,
    typeName: row.typeName,
    status: row.status,
    createTime: row.createTime,
    reason: row.reason,
    dateRange: row.startTime + " - " + row.endTime,
  }
  isShowDialogFormBox.value = true
}

const toTrace = async (row) => {
  try {
    let userStore = useUserStore()
    const token = userStore.token
    const response = await traceApi(row.id, token)

    let blob = await response.blob()
    imageUrl.value = URL.createObjectURL(blob)

    isShowDialogImgBox.value = true
  } catch (error) {
    ElMessage.error(error.message)
  }
}

const onDialogClose = () => {
  detailData.value = {}
}

const onPaginationChange = (page) => {
  tableData.value = []
  param.value.pageNum = page

  initPageData()
}
</script>

<template>
  <div class="my-apply-root">
    <div class="container">
      <div class="search-box">
        <div>
          <el-form-item label="标题">
            <el-input placeholder="请输入内容" v-model.trim="param.title" />
          </el-form-item>
        </div>

        <div>
          <el-form-item label="类型">
            <el-select v-model="param.typeId" placeholder="请选择">
              <el-option label="请假" :value="1"></el-option>
              <!-- <el-option label="报销" :value="2"></el-option> -->
            </el-select>
          </el-form-item>
        </div>

        <div>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="param.dateRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD HH:mm:ss"
            ></el-date-picker>
          </el-form-item>
        </div>

        <div>
          <el-form-item label="状态">
            <el-select v-model="param.status" placeholder="请选择">
              <el-option label="待审" value="pending"></el-option>
              <el-option label="通过" value="approved"></el-option>
              <el-option label="拒绝" value="rejected"></el-option>
            </el-select>
          </el-form-item>
        </div>

        <div>
          <el-form-item>
            <el-button type="primary" @click="initPageData">查询</el-button>
            <el-button @click="param = { pageNum: 1, pageSize: 10 }"
              >重置</el-button
            >
          </el-form-item>
        </div>
      </div>

      <div class="table-box">
        <iams-table
          v-model:data="tableData"
          :dataColumns="tableDataColumns"
          :useLastColumn="true"
        >
          <template #default="scope">
            <el-button size="small" @click="handleWatch(scope.row)">
              查看
            </el-button>

            <el-button
              v-if="scope.row.status != '通过'"
              size="small"
              @click="toTrace(scope.row)"
            >
              进度
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
          v-model:isShow="isShowDialogFormBox"
          v-model:data="detailData"
          :dataColumns="dialogFormColumns"
          :onClose="onDialogClose"
        ></dialog-form>
      </div>

      <div class="dialog-img-box">
        <el-dialog v-model="isShowDialogImgBox" width="800" align-center>
          <div class="img-container">
            <img class="full-image" :src="imageUrl" alt="流程追踪图" />
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.my-apply-root {
  background-color: #ffffff;
  height: 100%;
  overflow: auto;

  .container {
    padding: 20px;

    .search-box {
      display: flex;
      align-items: center;
      margin-bottom: 20px;

      > div {
        width: 25%;
        margin-right: 20px;
      }
    }

    .dialog-img-box {
      .img-container {
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
        overflow: hidden;
      }

      .full-image {
        max-width: 100%;
        max-height: 100%;
        object-fit: contain; /* 保证图像完整显示，不裁剪 */
        // 若需“拉伸填充”则用：object-fit: cover;
      }
    }
  }
}
</style>
