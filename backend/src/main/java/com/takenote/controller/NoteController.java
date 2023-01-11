package com.takenote.controller;

import com.takenote.model.Note;
import com.takenote.model.dto.NoteDTO;
import com.takenote.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/note")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public NoteDTO createNote(@RequestBody NoteDTO note) {
        Note createdNote = noteService.createNote(NoteDTO.toNote(note));

        return NoteDTO.fromNote(createdNote);
    }

    @PutMapping
    public NoteDTO updateNote(@RequestBody NoteDTO note) {
        Note updatedNote = noteService.updateNote(NoteDTO.toNote(note));

        return NoteDTO.fromNote(updatedNote);
    }

    @GetMapping
    public List<NoteDTO> readNote() {
        return noteService.readNote().stream()
                .map(NoteDTO::fromNote)
                .collect(Collectors.toList());
    }
}
