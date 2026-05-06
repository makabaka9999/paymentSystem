<template>
  <section class="page split">
    <div>
      <h1>用户管理</h1>
      <el-table :data="users">
        <el-table-column prop="id" label="ID" />
        <el-table-column prop="username" label="账号" />
        <el-table-column prop="role" label="角色" />
      </el-table>
    </div>
    <div>
      <h1>商户管理</h1>
      <el-table :data="merchants">
        <el-table-column prop="id" label="ID" />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="status" label="状态" />
      </el-table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { request } from '../api/http'

const users = ref([])
const merchants = ref([])

onMounted(async () => {
  users.value = await request('/api/admin/users')
  merchants.value = await request('/api/admin/merchants')
})
</script>
