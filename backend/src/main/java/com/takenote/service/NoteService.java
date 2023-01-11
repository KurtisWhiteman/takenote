package com.takenote.service;

import com.takenote.model.Note;
import com.takenote.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note createNote(Note note) {

        note.setCreatedAt(LocalDateTime.now());
        note.setCreatedBy(1L);

        noteRepository.save(note);

        return note;
    }

    public Note updateNote(Note note) {

        Note exisingNote = noteRepository.findNoteById(note.getId());

        exisingNote.setTitle(note.getTitle());
        exisingNote.setDescription(note.getDescription());
        exisingNote.setUpdatedAt(LocalDateTime.now());
        exisingNote.setUpdatedBy(1L);

        noteRepository.save(exisingNote);

        return exisingNote;
    }

    public List<Note> readNote() {
        return noteRepository.readAll();
    }
}
