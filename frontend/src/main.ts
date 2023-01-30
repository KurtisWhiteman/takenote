import Vue from 'vue'
import App from './App.vue'
import axios, { AxiosInstance } from 'axios'
import BootstrapVue from 'bootstrap-vue'
import router from './router'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import Home from '@/views/Home/Home.vue'
import Default from '@/views/Default/Default.vue'

Vue.config.productionTip = false
Vue.prototype.$axios = axios.create({
  baseURL: process.env.VUE_APP_API_URL || 'http://localhost:8098',
  timeout: process.env.VUE_APP_DEFAULT_AXIOS_TIMEOUT || 60000
})

Vue.use(BootstrapVue, {
  BTable: { sortIconLeft: true }
})

Vue.component('default-layout', Default)
Vue.component('home-layout', Home)

declare module 'vue/types/vue' {
  interface Vue {
    $axios: AxiosInstance;
  }
}

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
