<template>
  <section class="page narrow">
    <h1>模拟支付</h1>
    <p>订单号：{{ orderId }}</p>
    <p>金额：￥{{ amount }}</p>
    <el-button type="primary" @click="createPayment">创建支付单</el-button>
    <el-button type="success" :disabled="!payment" @click="success">支付成功</el-button>
    <el-alert v-if="payment" :title="`支付单 ${payment.paymentNo}：${payment.status}`" type="info" show-icon />
  </section>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { post } from '../api/http'
import { useSessionStore } from '../stores/session'

const route = useRoute()
const session = useSessionStore()
const orderId = computed(() => route.params.orderId)
const amount = computed(() => route.query.amount || '0.00')
const payment = ref(null)

async function createPayment() {
  payment.value = await post('/api/payments', {
    orderId: Number(orderId.value),
    userId: session.user?.userId || 1,
    amount: amount.value,
    idempotencyKey: `order:${orderId.value}`
  })
}

async function success() {
  payment.value = await post(`/api/payments/${payment.value.paymentNo}/mock-success`)
  await post(`/api/orders/${orderId.value}/paid`)
}
</script>
