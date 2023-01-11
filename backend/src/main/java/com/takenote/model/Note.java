package com.takenote.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

@SuperBuilder
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Note extends BaseEntity<Long> {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    public Note() {
        this.isDeleted = false;
    }
}
