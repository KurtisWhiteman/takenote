<template>
  <div class="alert-container"
       v-if="!autoDismissible || (dismissCountDown && autoDismissible)">
    <b-alert v-if="autoDismissible"
             :variant="variant"
             dismissible
             :show="dismissCountDown"
             @dismissed="dismissCountDown=0"
             @dismiss-count-down="countDownChanged"
             fade
    >
      <div>{{text}}</div>
    </b-alert>
    <b-alert v-else
             :variant="variant"
             dismissible
             show
             fade
    >
      <div>{{text}}</div>
    </b-alert>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'

type AlertVariant = 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'light' | 'dark'

@Component
export default class Alert extends Vue {
  @Prop({ required: true }) private text!: string
  @Prop() private variant: AlertVariant
  @Prop() private autoDismissInterval: number

  autoDismissible = false
  dismissCountDown = 0

  created () {
    if (this.autoDismissInterval && this.autoDismissInterval > 0) {
      this.autoDismissible = true
      this.dismissCountDown = this.autoDismissInterval
    }
  }

  countDownChanged (dismissCountDown: number) {
    this.dismissCountDown = dismissCountDown
  }
}
</script>

<style scoped lang="scss" src="./Alert.scss"></style>
