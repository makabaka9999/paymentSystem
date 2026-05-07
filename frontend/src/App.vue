<template>
  <router-view v-if="$route.path === '/login'" />
  <el-container v-else class="shell">
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
        <el-button size="small" @click="logout">退出</el-button>
      </el-header>
      <el-main class="content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useSessionStore } from './stores/session'

const router = useRouter()
const session = useSessionStore()

function logout() {
  session.logout()
  router.push('/login')
}
</script>
