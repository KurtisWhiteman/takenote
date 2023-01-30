<template>
  <div id="note-list" class="row">
    <h5 class="p-2 pl-4" id="note-card-title">Recent Notes</h5>
    <b-card no-body id="note-card">
      <b-list-group vertical tabs>
        <b-list-group-item v-for="(note, index) in notes" :value="note"
                           v-bind:key="note.id"
            href="#" id="note-list-item" :class="activeIndex === index ? 'note-active' : ''"
                           @click="setActive(note, index)">
          <div class="w-100">
            <h5 class="mb-1">{{ note.title.length === 0 ? 'Untitled Note' : note.title }}</h5>
          </div>

          <p class="mb-1">
            {{ note.description.length === 0 ? 'No content' : note.description.substr(0, 50) + "..." }}
          </p>

          <div class="d-flex w-100 justify-content-between note-small-text">
            <p class="mt-3 mb-0"><i>{{ 'Edited: ' + formatCreatedAt(note.updatedAt) }}</i></p> <!-- in the future tags will go here -->
            <p class="mt-3 mb-0">{{ 'Created: ' + note.createdAt.substring(0, 10) }}</p>
          </div>
        </b-list-group-item>
      </b-list-group>
    </b-card>
  </div>
</template>

<script lang="ts">
import {Component, Vue} from 'vue-property-decorator';
import {AlertModel} from "@/helpers/types";
import {Note} from "@/model/Note";

@Component({
  components: {},
})
export default class NoteList extends Vue {

  activeIndex: number = null

  alertModel: AlertModel = null
  notes: Note[] = []

  todaysNote = false

  created () {
    this.$axios.get('/api/v1/note')
        .then((response) => {
          this.notes = response.data
          this.checkForTodaysNote()
          this.setAlert('success', 'Retrieved Notes successfully')
        }, error => {
          this.setAlert('danger', 'Error retrieving Notes')
        })
  }

  formatCreatedAt(updatedAt: string) {
    const intervals = [
      { label: 'year', seconds: 31536000 },
      { label: 'month', seconds: 2592000 },
      { label: 'day', seconds: 86400 },
      { label: 'hour', seconds: 3600 },
      { label: 'minute', seconds: 60 },
      { label: 'second', seconds: 1 }
    ];

    // 39600 is 11 hours because we're in AEDT at time of development
    // Needs to change to 36000 when AEST - Ultimately will incorporate timezones but for now it's fine
    const seconds = Math.floor((Date.now() - new Date(updatedAt).getTime()) / 1000) + 39600;
    const interval = intervals.find(i => i.seconds < seconds);
    const count = Math.floor(seconds / interval.seconds);
    return `${count} ${interval.label}${count !== 1 ? 's' : ''} ago`;
  }

  setActive(note: Note, index: number) {
    this.activeIndex = index
    this.$emit('emit-note', note)
  }

  checkForTodaysNote() {
    this.notes.forEach(note => {
      if (new Date(Date.parse(note.createdAt.substring(0, 19))).toLocaleDateString() == new Date().toLocaleDateString("en-AU")) {
        this.todaysNote = true
      }
    })


    if (!this.todaysNote) {
      const newNote = new Note(
          null,
          null,
          null,
          false
      )

      this.$axios.post('/api/v1/note', newNote)
          .then((response) => {
            this.notes.push(response.data)
            this.setAlert('success', 'Notes successfully retrieved')
          }, error => {
            this.setAlert('danger', 'Error when retrieving notes')
          })
    }

    this.$emit('emit-first-note', this.notes[0])
  }

  setAlert(variant: string, alertMessage: string) {
    this.alertModel = {
      displayAlert: true,
      variant: variant,
      alertMessage: alertMessage
    }
  }
}
</script>

<style scoped lang="scss" src="./NoteList.scss"></style>
