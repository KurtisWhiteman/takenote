import Vue from 'vue'
import VueRouter, { RouteConfig } from 'vue-router'
import Home from '@/views/Home/Home.vue'
import Login from '@/views/Login/Login.vue'
import About from '@/views/About/About.vue'
import { isTokenValid } from "@/helpers/auth/TokenHelpers";
import Constants from "@/helpers/util/Constants";

Vue.use(VueRouter)

const routes: Array<RouteConfig> = [
  {
    path: '/',
    redirect: {
      name: 'Home'
    }
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/about',
    name: 'About',
    component: About
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

/**
 * This Vue method gets called before each navigation, so the best place to validate token, authorization, etc.
 * No validation required to go to Login/ Error pages.
 */
router.beforeEach((to, from, next) => {
  // This will bypass login to the Default route if they hit /login and have a valid token/permissions
  if (to.name === 'Login' && isTokenValid()) {
    next({ name: 'Home' })
    return
  }

  // The following routes will not require token validation
  if (Constants.IGNORE_ROUTE_LIST.includes(to.name)) {
    next()
    return
  }

  // This is required for the below hard reload on logout to not go into an infinite loop
  if (from.path === '/' && !isTokenValid()) {
    next({ name: 'Login' })
    return
  }

  // Does a hard reload on logout, so we can pull in any updated resources from CDN
  if (!isTokenValid()) {
    document.location.href = '/'
    return
  }

  // if there are no permissions required
  if (!to.meta.permission && to.name !== null) {
    next()
    return
  }

  if (isTokenValid()) {
    next()
  }
})


export default router
