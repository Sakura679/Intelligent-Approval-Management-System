<script setup>
import { approvalStatistics, leaveApprovalPageListApi } from "@/api/approval"
import echarts from "@/utils/echarts"
import { PieChart } from "echarts/charts"
import { onMounted, ref } from "vue"

// 初始化数据
const dayStatistics = ref({})
const weekStatistics = ref({})
const monthStatistics = ref({})
const tableData = ref([])
const tableDataColumns = ref([
  { label: "标题", prop: "title" },
  { label: "类型", prop: "typeName" },
  { label: "状态", prop: "status" },
  { label: "创建时间", prop: "createTime" },
])

// 饼状图
echarts.use(PieChart)
const piechartRef = ref(null)
let myChart = null

const option = ref({
  title: {
    text: "近一月申请分布",
    // subtext: "Fake Data",
    left: "center",
  },
  tooltip: {
    trigger: "item",
  },
  legend: {
    orient: "vertical",
    left: "left",
  },
  series: [
    {
      // name: "Access From",
      type: "pie",
      radius: "50%",
      data: [
        { value: 0, name: "待审" },
        { value: 0, name: "通过" },
        { value: 0, name: "拒绝" },
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: "rgba(0, 0, 0, 0.5)",
        },
      },
    },
  ],
})

const initPageData = async () => {
  try {
    // 获取统计数据
    let res = await approvalStatistics()

    if (res.code == 200) {
      dayStatistics.value = res.data.day
      weekStatistics.value = res.data.week
      monthStatistics.value = res.data.month

      option.value.series[0].data = [
        { value: monthStatistics.value.pendingCount, name: "待审" },
        { value: monthStatistics.value.approvedCount, name: "通过" },
        { value: monthStatistics.value.rejectedCount, name: "拒绝" },
      ]

      if (myChart) {
        myChart.setOption(option.value)
      }
    } else {
      ElMessage.error(res.message)
    }

    // 获取最近申请列表
    res = await leaveApprovalPageListApi({
      pageNum: 1,
      pageSize: 10,
    })

    if (res.code === 200) {
      tableData.value = res.data
    } else {
      ElMessage.error(res.message)
    }
  } catch (error) {
    ElMessage.error(error.message)
  }
}

onMounted(() => {
  myChart = echarts.init(piechartRef.value)
  myChart.setOption(option.value)

  initPageData()
})
</script>

<template>
  <div class="home-root">
    <div class="content-box">
      <div class="content-box-header">我的申请</div>

      <div class="card-box">
        <div class="card-item">
          <div class="card-item-header">今日</div>

          <div class="card-item-content">
            <div style="color: #666666; font-size: 15px">待审</div>
            <div style="color: #0064ff">{{ dayStatistics.pendingCount }}</div>
          </div>

          <div class="card-item-content">
            <div style="color: #666666; font-size: 15px">通过</div>
            <div style="color: #0064ff">{{ dayStatistics.approvedCount }}</div>
          </div>

          <div class="card-item-content">
            <div style="color: #666666; font-size: 15px">拒绝</div>
            <div style="color: #0064ff">{{ dayStatistics.rejectedCount }}</div>
          </div>
        </div>

        <div class="card-item">
          <div class="card-item-header">近一周</div>

          <div class="card-item-content">
            <div style="color: #666666; font-size: 15px">待审</div>
            <div style="color: #0064ff">{{ weekStatistics.pendingCount }}</div>
          </div>

          <div class="card-item-content">
            <div style="color: #666666; font-size: 15px">通过</div>
            <div style="color: #0064ff">{{ weekStatistics.approvedCount }}</div>
          </div>

          <div class="card-item-content">
            <div style="color: #666666; font-size: 15px">拒绝</div>
            <div style="color: #0064ff">{{ weekStatistics.rejectedCount }}</div>
          </div>
        </div>

        <div class="card-item">
          <div class="card-item-header">近一月</div>

          <div class="card-item-content">
            <div style="color: #666666; font-size: 15px">待审</div>
            <div style="color: #0064ff">{{ monthStatistics.pendingCount }}</div>
          </div>

          <div class="card-item-content">
            <div style="color: #666666; font-size: 15px">通过</div>
            <div style="color: #0064ff">
              {{ monthStatistics.approvedCount }}
            </div>
          </div>

          <div class="card-item-content">
            <div style="color: #666666; font-size: 15px">拒绝</div>
            <div style="color: #0064ff">
              {{ monthStatistics.rejectedCount }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="content-box">
      <div class="content-box-header">我的审批</div>

      <div class="card-box">
        <div class="card-item">
          <div class="card-item-content">
            <div style="color: #666666; font-size: 15px">今日</div>
            <div style="color: #0064ff">{{ dayStatistics.processedCount }}</div>
          </div>
        </div>

        <div class="card-item">
          <div class="card-item-content">
            <div style="color: #666666; font-size: 15px">近一周</div>
            <div style="color: #0064ff">
              {{ weekStatistics.processedCount }}
            </div>
          </div>
        </div>

        <div class="card-item">
          <div class="card-item-content">
            <div style="color: #666666; font-size: 15px">近一月</div>
            <div style="color: #0064ff">
              {{ monthStatistics.processedCount }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="echart-box">
      <div class="piechart-box">
        <div class="piechart" ref="piechartRef"></div>
      </div>
      <div class="near-time-box">
        <div class="box-header">最近申请列表</div>
        <div class="near-time-content-box">
          <iams-table
            v-model:data="tableData"
            :dataColumns="tableDataColumns"
          ></iams-table>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.home-root {
  .content-box {
    background-color: #ffffff;
    padding: 20px;
    margin: 20px 0;
    margin-top: 0;

    .content-box-header {
      padding-bottom: 20px;
      font-weight: bold;
    }

    .card-box {
      display: flex;
      gap: 20px;

      .card-item {
        background-color: #f3f6fb;
        padding: 10px;
        width: 50%;

        .card-item-header {
          text-align: center;
        }

        .card-item-content {
          display: flex;
          justify-content: space-between;
          padding: 10px;
        }
      }
    }
  }

  .echart-box {
    display: flex;
    margin: 20px 0;
    margin-bottom: 0;
    background-color: #ffffff;
    justify-content: center;
    // gap: 200px;

    .piechart-box {
      width: 50%;

      .piechart {
        width: 100%;
        height: 400px;
      }
    }

    .near-time-box {
      padding: 20px 0;
      width: 40%;

      .box-header {
        margin-bottom: 20px;
        font-weight: bold;
        text-align: center;
      }
    }
  }
}
</style>
