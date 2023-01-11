<template>
  <div class="home">
    <Navbar />
    <Noteboard />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
// @ is an alias to /src
import Navbar from '@/components/Navbar/navigation-bar.vue';
import Noteboard from '@/components/Noteboard/Noteboard.vue';

@Component({
  components: {
    Navbar,
    Noteboard
  },
})
export default class Home extends Vue {}
</script>

<style scoped lang="scss" src="./Home.scss"></style>
