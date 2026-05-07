import { createRouter, createWebHistory } from 'vue-router'
import Home from './views/Home.vue'
import ProductDetail from './views/ProductDetail.vue'
import Cart from './views/Cart.vue'
import Orders from './views/Orders.vue'
import Payment from './views/Payment.vue'
import Seckill from './views/Seckill.vue'
import Merchant from './views/Merchant.vue'
import Admin from './views/Admin.vue'
import Login from './views/Login.vue'
import { useSessionStore } from './stores/session'

const routes = [
  { path: '/login', component: Login, meta: { public: true } },
  { path: '/', component: Home },
  { path: '/products/:id', component: ProductDetail },
  { path: '/cart', component: Cart },
  { path: '/orders', component: Orders },
  { path: '/pay/:orderId', component: Payment },
  { path: '/seckill', component: Seckill },
  { path: '/merchant', component: Merchant },
  { path: '/admin', component: Admin }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  const session = useSessionStore()
  if (!to.meta.public && !session.accessToken) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  if (to.path === '/login' && session.accessToken) {
    return '/'
  }
  return true
})

export default router
