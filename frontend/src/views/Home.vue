<template>
  <section class="page">
    <div class="toolbar">
      <h1>商城首页</h1>
      <el-input v-model="keyword" placeholder="搜索商品" clearable @keyup.enter="loadProducts" />
      <el-button type="primary" @click="loadProducts">搜索</el-button>
    </div>
    <div class="product-grid">
      <article v-for="product in products" :key="product.id" class="product-card">
        <div class="thumb">{{ product.category }}</div>
        <h2>{{ product.name }}</h2>
        <p>{{ product.description }}</p>
        <div class="row">
          <strong>￥{{ product.price }}</strong>
          <span>库存 {{ product.stock }}</span>
        </div>
        <el-button type="primary" @click="$router.push(`/products/${product.id}`)">查看详情</el-button>
      </article>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { request } from '../api/http'

const products = ref([])
const keyword = ref('')

async function loadProducts() {
  products.value = await request(`/api/products${keyword.value ? `?keyword=${encodeURIComponent(keyword.value)}` : ''}`)
}

onMounted(loadProducts)
</script>
