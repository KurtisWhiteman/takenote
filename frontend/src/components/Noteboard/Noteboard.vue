<template>
  <div id="notepad" class="row">
    <div class="col-lg-3" id="note-list-div">
      <NoteList @emit-note="getSelectedNote" @emit-first-note="getFirstNote" />
    </div>
    <div class="col-lg-9" id="notepad-div">
      <Notepad :note="selectedNote" />
    </div>
  </div>
</template>

<script lang="ts">
import {Component, Vue} from 'vue-property-decorator';
import NoteList from "@/components/Noteboard/NoteList.vue";
import Notepad from "@/components/Noteboard/Notepad.vue";
import {Note} from "@/model/Note";

@Component({
  components: {NoteList, Notepad},
})
export default class Noteboard extends Vue {
  selectedNote: Note = null

  getSelectedNote (note: Note) {
    this.selectedNote = note
  }

  getFirstNote (note: Note) {
    this.selectedNote = note
  }
}
</script>

<style scoped lang="scss" src="./Noteboard.scss"></style>

