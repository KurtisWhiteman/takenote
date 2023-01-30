<template>
  <div id="nav">
    <nav class="navbar navbar-expand-lg">
      <div class="d-flex flex-grow-1">
        <a class="navbar-brand" href="#">
          <span class="w-100 d-lg-none d-block"><i class="fa-solid fa-note-sticky"></i></span>
        </a>
        <div class="collapse navbar-collapse flex-grow-1 text-right" id="navbarNav">
          <ul class="navbar-nav ml-auto flex-nowrap">
            <li class="nav-item active">
              <a class="nav-link" href="/home">Home</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="/about">About</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component({
  components: {
  }
})
export default class Navbar extends Vue {
  visible = true

}
</script>

<style scoped lang="scss" src="./Navbar.scss"></style>
