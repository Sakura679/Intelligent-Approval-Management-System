<script setup>
import { ref } from "vue"

const tableData = defineModel("data", {
  type: Array,
  default: ref([]),
})

const props = defineProps({
  dataColumns: {
    type: Array,
    default: () => [],
  },
  useLastColumn: {
    type: Boolean,
    default: false,
  },
})
</script>

<template>
  <div class="iams-table-box">
    <div class="container">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column
          v-for="(item, index) in props.dataColumns"
          :key="index"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
        />

        <el-table-column label="操作" v-if="props.useLastColumn">
          <template #default="scope">
            <slot :row="scope.row">
              <!-- <el-button
                size="small"
                @click="handleWatch(scope.$index, scope.row)"
              >
                查看
              </el-button> -->
            </slot>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<style lang="scss" scoped></style>
