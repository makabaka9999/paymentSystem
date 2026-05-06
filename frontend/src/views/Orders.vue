<template>
  <section class="page">
    <h1>我的订单</h1>
    <el-table :data="orders">
      <el-table-column prop="id" label="订单号" />
      <el-table-column prop="totalAmount" label="金额" />
      <el-table-column prop="status" label="状态" />
      <el-table-column prop="createdAt" label="创建时间" />
    </el-table>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { request } from '../api/http'

const orders = ref([])
onMounted(async () => {
  orders.value = await request('/api/orders')
})
</script>
