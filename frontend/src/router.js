import { createRouter, createWebHistory } from 'vue-router'
import Home from './views/Home.vue'
import ProductDetail from './views/ProductDetail.vue'
import Cart from './views/Cart.vue'
import Orders from './views/Orders.vue'
import Payment from './views/Payment.vue'
import Seckill from './views/Seckill.vue'
import Merchant from './views/Merchant.vue'
import Admin from './views/Admin.vue'

const routes = [
  { path: '/', component: Home },
  { path: '/products/:id', component: ProductDetail },
  { path: '/cart', component: Cart },
  { path: '/orders', component: Orders },
  { path: '/pay/:orderId', component: Payment },
  { path: '/seckill', component: Seckill },
  { path: '/merchant', component: Merchant },
  { path: '/admin', component: Admin }
]

export default createRouter({
  history: createWebHistory(),
  routes
})
