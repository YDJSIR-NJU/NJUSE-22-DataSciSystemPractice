import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'index',
    component: () => import('../views/Index.vue')
  },
  {
    path: '/abnormalScanner',
    name: 'abnormalScanner',
    component: () => import('../views/AbnormalScanner.vue')
  },
  {
    path: '/contextQuerier',
    name: 'contextQuerier',
    component: () => import('../views/ContextQuerier.vue')
  },
  {
    path: '/consumeChecker',
    name: 'consumeChecker',
    component: () => import('../views/ConsumeChecker.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
