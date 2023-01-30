<template>
  <div class="about">
    <Navbar />
    <div class="row">
      <div class="col-12">
        <h1>About Takenote</h1>
      </div>
    </div>
    <div class="row">
      <div class="col-12">
        <p class="about-text">Takenote is a super simple note taking app.<br>
        Every day an empty note is created as long as you visit the website.<br><br>
        There is no option to add additional notes, it is limited to one note per day<br>
        A note is only created if the site is visited<br>
        This limitation is countered by being able to tag notes and specific phrases words that you can then filter/search by<br><br><br>
        Enjoy!</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Navbar from '@/components/Navbar/Navbar.vue';
// @ is an alias to /src

@Component({
  components: {
    Navbar
  },
})
export default class About extends Vue {}
</script>

<style scoped lang="scss" src="./About.scss"></style>
