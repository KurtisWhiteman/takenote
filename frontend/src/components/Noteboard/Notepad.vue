<template>
  <div id="notepad">
    <div>
      <b-form-input class="mb-2" id="note-title" type="text" Placeholder="Title..." v-model="noteTitle"></b-form-input>
    </div>
    <div class="container">
        <b-form-textarea
            id="note"
            class="form-control"
            placeholder="The notes go here..."
            no-resize
            v-model="noteDescription"
        ></b-form-textarea>
      <b-button class="mt-2" variant="success" @click="handleSubmit">Save</b-button>
    </div>
  </div>
</template>

<script lang="ts">
import {Component, Prop, Vue, Watch} from 'vue-property-decorator'
import {AlertModel} from '@/helpers/types'
import {Note} from '@/model/Note'

@Component({
  components: {},
})
export default class Notepad extends Vue {
  @Prop() note: Note

  noteTitle: string = null
  noteDescription: string = null
  alertModel: AlertModel = null

  handleSubmit() {
    if (this.note && this.note.id !== null) {
      this.updateNote()
    } else {
      this.saveNote()
    }
  }

  updateNote() {
    this.note.title = this.noteTitle
    this.note.description = this.noteDescription

    this.$axios.put('/api/v1/note', this.note)
        .then((response) => {
          this.setAlert('success', 'Customer Carrier Account deleted successfully')
        }, error => {
          this.setAlert('danger', 'Error deleting Customer Carrier Account')
        })
  }

  saveNote() {
    const note = new Note(
        0,
        this.noteTitle,
        this.noteDescription,
        false
    )

    this.$axios.post('/api/v1/note', note)
        .then((response) => {
          this.setAlert('success', 'Customer Carrier Account deleted successfully')
        }, error => {
          this.setAlert('danger', 'Error deleting Customer Carrier Account')
        })
  }

  setAlert(variant: string, alertMessage: string) {
    this.alertModel = {
      displayAlert: true,
      variant: variant,
      alertMessage: alertMessage
    }
  }

  @Watch('note', { immediate: true, deep: true })
  setActiveNote () {
    if (!this.note) {
      return
    }

    this.noteTitle = this.note.title
    this.noteDescription = this.note.description
  }
}
</script>

<style lang="scss" src="./Notepad.scss"></style>
