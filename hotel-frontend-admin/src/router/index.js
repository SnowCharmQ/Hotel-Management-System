import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: () => import("../views/Login.vue")

  },
  {
    path: '/layout',
    component: () => import("@/views/Layout.vue"),
    // meta: {
    //   requireAuth: true
    // },
    children: [
      {
        path: 'customer',
        component: () => import('@/views/Role/Customer.vue')
      },
      {
        path: 'admin',
        component: () => import('@/views/Role/Admin.vue')
      },
      {
        path: 'type',
        component:() => import('@/views/Room/Type.vue')
      },
      {
        path: 'room',
        component:() => import('@/views/Room/Room.vue')
      },
      {
        path: 'order',
        component:() => import('@/views/Order/Order.vue')
      },
      {
        path: 'graph1',
        component:() => import('@/views/Graphs/graphs1.vue')
      }, {
        path: 'graph2',
        component:() => import('@/views/Graphs/graphs2.vue')
      }, {
        path: 'graph3',
        component:() => import('@/views/Graphs/graphs3.vue')
      }, {
        path: 'graph4',
        component:() => import('@/views/Graphs/graphs4.vue')
      }, {
        path: 'chat',
        component:() => import('@/views/chat.vue')
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes
})

export default router

router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) {
    if (sessionStorage.getItem('token')) {
      next();
    } else {
      next({
        path: '/',
        query: {redirect: to.fullPath}
      })
    }
  } else {
    next();
  }
})
