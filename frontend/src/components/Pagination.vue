<script setup>
const props = defineProps({
  onPageChange: {
    type: Function,
    required: true,
  },
  total: {
    type: Number,
    default: () => 0,
  },
  layout: {
    type: String,
    default: () => "prev, pager, next",
  },
})
</script>

<template>
  <div class="pagination-box">
    <el-pagination
      :layout="props.layout"
      :total="props.total"
      @current-change="props.onPageChange"
    />
  </div>
</template>

<style lang="scss" scoped>
.pagination-box {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
