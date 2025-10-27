import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/user'
  },
  {
    path: '/user',
    name: 'User',
    component: () => import('./pages/UserPage.vue')
  },
  {
    path: '/role',
    name: 'Role',
    component: () => import('./pages/RolePage.vue')
  },
  {
    path: '/resource',
    name: 'Resource',
    component: () => import('./pages/ResourcePage.vue')
  },
  {
    path: '/permission-check',
    name: 'PermissionCheck',
    component: () => import('./pages/PermissionCheckPage.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router