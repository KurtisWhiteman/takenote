package com.takenote.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.takenote.model.Note;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteDTO {

    private long id;
    private String title;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime updatedAt;

    public static Note toNote(NoteDTO dto) {
        Note.NoteBuilder<?, ?> builder = Note.builder();

        return builder
                .id(dto.id)
                .title(dto.title)
                .description(dto.description)
                .build();
    }

    public static NoteDTO fromNote(Note note) {

        if (Objects.isNull(note)) {
            return null;
        }

        return NoteDTO.builder()
                .id(note.getId())
                .title(Objects.isNull(note.getTitle()) ? "" : note.getTitle())
                .description(Objects.isNull(note.getDescription()) ? "" : note.getDescription())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }
}
