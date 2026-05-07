<template>
  <section class="login-page">
    <form class="login-panel" @submit.prevent="login">
      <div>
        <div class="login-brand">PayMVP</div>
        <h1>登录系统</h1>
      </div>
      <el-alert v-if="error" :title="error" type="error" show-icon :closable="false" />
      <el-input v-model="username" size="large" placeholder="账号 user / merchant / admin" autofocus />
      <el-input v-model="password" size="large" type="password" placeholder="密码 password" show-password />
      <el-button native-type="submit" size="large" type="primary" :loading="loading">登录</el-button>
    </form>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useSessionStore } from '../stores/session'

const route = useRoute()
const router = useRouter()
const session = useSessionStore()
const username = ref('user')
const password = ref('password')
const loading = ref(false)
const error = ref('')

async function login() {
  loading.value = true
  error.value = ''
  try {
    await session.login(username.value, password.value)
    router.push(route.query.redirect || '/')
  } catch (err) {
    error.value = err.message || '登录失败'
  } finally {
    loading.value = false
  }
}
</script>
