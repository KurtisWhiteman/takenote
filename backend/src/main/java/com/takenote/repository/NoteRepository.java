package com.takenote.repository;

import com.takenote.model.Note;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteRepository extends CrudRepository<Note, Long> {

    String READ_ALL = """
            SELECT id, title, description, is_deleted,
            created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
            FROM note
            WHERE is_deleted = FALSE
            ORDER BY created_at DESC
                """;

    String FIND_BY_ID = """
            SELECT id, title, description, is_deleted,
            created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
            FROM note
            WHERE id = ?1
                """;

    @Query(value = READ_ALL, nativeQuery = true)
    List<Note> readAll();

    @Query(value = FIND_BY_ID, nativeQuery = true)
    Note findNoteById(long noteId);
}
