<template>
  <section class="page">
    <h1>购物车</h1>
    <el-table :data="items">
      <el-table-column prop="productName" label="商品" />
      <el-table-column prop="price" label="单价" width="120" />
      <el-table-column prop="quantity" label="数量" width="120" />
    </el-table>
    <div class="action-bar">
      <strong>合计 ¥{{ total }}</strong>
      <el-button type="primary" :disabled="items.length === 0" @click="checkout">提交订单</el-button>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { post, request } from '../api/http'

const router = useRouter()
const items = ref([])
const total = computed(() => items.value.reduce((sum, item) => sum + Number(item.price) * item.quantity, 0).toFixed(2))

onMounted(async () => {
  items.value = await request('/api/cart')
})

async function checkout() {
  const order = await post('/api/orders', { items: items.value, addressId: '1' })
  router.push(`/pay/${order.id}?amount=${order.totalAmount}`)
}
</script>
