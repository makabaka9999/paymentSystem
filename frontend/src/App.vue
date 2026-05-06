<template>
  <el-container class="shell">
    <el-aside width="236px" class="sidebar">
      <div class="brand">PayMVP</div>
      <el-menu router :default-active="$route.path" class="nav">
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/seckill">秒杀专区</el-menu-item>
        <el-menu-item index="/cart">购物车</el-menu-item>
        <el-menu-item index="/orders">我的订单</el-menu-item>
        <el-menu-item index="/merchant">商户后台</el-menu-item>
        <el-menu-item index="/admin">管理端</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="topbar">
        <div>
          <strong>{{ session.user?.username || '未登录' }}</strong>
          <span class="role">{{ session.user?.role || 'GUEST' }}</span>
        </div>
        <div class="login-row">
          <el-input v-model="username" size="small" placeholder="账号 user/merchant/admin" />
          <el-input v-model="password" size="small" type="password" placeholder="密码 password" />
          <el-button size="small" type="primary" @click="login">登录</el-button>
        </div>
      </el-header>
      <el-main class="content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from 'vue'
import { useSessionStore } from './stores/session'

const session = useSessionStore()
const username = ref('user')
const password = ref('password')

async function login() {
  await session.login(username.value, password.value)
}
</script>
