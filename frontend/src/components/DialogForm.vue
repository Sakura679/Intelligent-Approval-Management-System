<script setup>
const detailData = defineModel("data", {
  type: Object,
  default: () => {},
})
const isShow = defineModel("isShow", {
  type: Boolean,
  default: () => false,
})

const props = defineProps({
  dataColumns: {
    type: Array,
    default: () => [],
  },
  onClose: {
    type: Function,
    required: true,
  },
  width: {
    type: Number,
    default: () => 500,
  },
  useButton: {
    type: Boolean,
    default: () => false,
  },
})
</script>

<template>
  <div class="dialog-box">
    <el-dialog
      v-model="isShow"
      :width="props.width"
      align-center
      @close="props.onClose"
    >
      <div class="form-box">
        <el-form :model="detailData" label-width="auto">
          <el-form-item
            v-for="item in props.dataColumns"
            :key="item.prop"
            :label="item.label"
          >
            <el-input
              v-model="detailData[item.prop]"
              :type="item.type"
              :resize="item.resize"
              :autosize="item.autosize"
              :readonly="item.readonly"
            />
          </el-form-item>

          <el-form-item v-if="props.useButton">
            <slot></slot>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.dialog-box {
  .form-box {
    padding: 20px;

    ::v-deep(.el-form) {
      .el-form-item {
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
</style>
