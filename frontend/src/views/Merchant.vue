<template>
  <section class="page split">
    <div>
      <h1>商户商品管理</h1>
      <el-form label-width="80">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="分类"><el-input v-model="form.category" /></el-form-item>
        <el-form-item label="价格"><el-input-number v-model="form.price" :min="0" /></el-form-item>
        <el-form-item label="库存"><el-input-number v-model="form.stock" :min="0" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" /></el-form-item>
        <el-button type="primary" @click="create">新增商品</el-button>
      </el-form>
    </div>
    <div>
      <h1>商户订单</h1>
      <el-table :data="orders">
        <el-table-column prop="orderId" label="订单号" />
        <el-table-column prop="status" label="状态" />
        <el-table-column prop="amount" label="金额" />
      </el-table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { post, request } from '../api/http'

const form = reactive({ name: '', category: '', price: 0, stock: 0, description: '' })
const orders = ref([])

async function create() {
  await post('/api/merchant/products', form)
}

onMounted(async () => {
  orders.value = await request('/api/merchant/orders')
})
</script>
