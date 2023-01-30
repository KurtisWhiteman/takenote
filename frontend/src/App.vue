<template>
  <div id="app">
    <component :is="layout">
      <router-view/>
    </component>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import Constants from "@/helpers/util/Constants";
import { isTokenValid } from "@/helpers/auth/TokenHelpers";

@Component
export default class App extends Vue {
  get layout () {
    return (this.$route.meta.layout || 'default') + '-layout'
  }

  mounted () {
    // We need to check against Ignored route list to prevent the app from redirecting to log on
    if (!isTokenValid() && !Constants.IGNORE_ROUTE_LIST.includes(this.$router.currentRoute.name)) {
      this.$router.replace({ name: 'Login' })
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: #30323d;
  height: 100%;
}
</style>
