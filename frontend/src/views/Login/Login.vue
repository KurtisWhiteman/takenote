<template>
  <b-container fluid>
    <b-row>
      <form class="login text-center my-5 mx-auto pt-5 px-0 px-sm-5" @submit.prevent="login">
        <h1>Take Note</h1>
        <Alert v-if="displayAlert" :variant="variant" :text="alertMessage"/>
        <div class="input-group mt-5">
          <div class="input-group-prepend">
            <div class="input-group-text">
              <span class="fa fa-user"/>
            </div>
          </div>
          <b-form-input required v-model="email" :state="validation" class="form-control"
                        placeholder="Username" autocomplete="email"/>
        </div>

        <div class="input-group my-3">
          <div class="input-group-prepend">
            <div class="input-group-text">
              <span class="fa fa-key"/>
            </div>
          </div>
          <b-form-input required type="password" v-model="password" :state="validation" class="form-control"
                        placeholder="Password" autocomplete="current-password"/>
        </div>

        <b-form-invalid-feedback :state="validation">{{ errorMessage }}</b-form-invalid-feedback>

        <button class="btn mb-5 btn-lg btn-primary btn-block" type="submit"
                :disabled="multipleAttempts">Sign in
        </button>
      </form>
    </b-row>
  </b-container>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import {
  setToken
} from '@/helpers/auth/StorageHelpers'
import Alert from '@/components/Alert/Alert.vue'

@Component({ components: { Alert } })
export default class Login extends Vue {
  email = ''
  password = ''
  response = ''
  validation: boolean = null
  errorMessage = 'Error logging in!'
  multipleAttempts = false

  displayAlert = false
  variant: string = null
  alertMessage: string = null

  redirectRoute: string = null

  setAlert (variant: string, alertMessage: string) {
    this.displayAlert = true
    this.variant = variant
    this.alertMessage = alertMessage
  }

  created () {
    if (this.$route.query.redirect) {
      this.redirectRoute = this.$route.query.redirect.toString()
    }

    if (this.$route.query.message) {
      this.setAlert('warning', this.$route.query.message.toString())
    }
  }

  login (): void {
    const user = this.prepareData()
    this.$axios.post('/api/v1/auth/login', user)
        .then(response => {
          const token = response.data
          setToken(token)
          this.validation = true
        }, error => {
          console.log(error)
          if (error.response.data !== null) {
            this.errorMessage = error.response.data
          }
          this.validation = false
          if (error.response.status === 429) {
            this.disableSignIn()
          }
        })
  }

  prepareData () {
    return {
      email: this.email,
      password: this.password
    }
  }

  disableSignIn () {
    this.multipleAttempts = true
    setTimeout(() => {
      this.multipleAttempts = false
    }, 60000)
  }
}
</script>

<style scoped lang="scss" src="./Login.scss"></style>
