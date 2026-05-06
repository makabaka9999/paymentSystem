<template>
  <section class="page">
    <h1>秒杀专区</h1>
    <div class="product-grid">
      <article v-for="activity in activities" :key="activity.id" class="product-card">
        <div class="thumb">秒杀</div>
        <h2>{{ activity.title }}</h2>
        <strong>￥{{ activity.seckillPrice }}</strong>
        <span>库存 {{ activity.stock }}</span>
        <el-button type="danger" @click="submit(activity.id)">立即抢购</el-button>
      </article>
    </div>
    <el-alert v-if="record" :title="`${record.requestId}：${record.status}`" type="success" show-icon />
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { post, request } from '../api/http'

const activities = ref([])
const record = ref(null)

onMounted(async () => {
  activities.value = await request('/api/seckill/activities')
})

async function submit(id) {
  record.value = await post(`/api/seckill/activities/${id}/submit`)
}
</script>
