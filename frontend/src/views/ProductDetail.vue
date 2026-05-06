<template>
  <section class="page detail" v-if="product">
    <div class="detail-media">{{ product.category }}</div>
    <div class="detail-panel">
      <h1>{{ product.name }}</h1>
      <p>{{ product.description }}</p>
      <strong class="price">￥{{ product.price }}</strong>
      <el-input-number v-model="quantity" :min="1" :max="product.stock" />
      <el-button type="primary" @click="addCart">加入购物车</el-button>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { post, request } from '../api/http'

const route = useRoute()
const router = useRouter()
const product = ref(null)
const quantity = ref(1)

onMounted(async () => {
  product.value = await request(`/api/products/${route.params.id}`)
})

async function addCart() {
  await post('/api/cart/items', {
    skuId: product.value.skuId,
    productName: product.value.name,
    price: product.value.price,
    quantity: quantity.value
  })
  router.push('/cart')
}
</script>
