package com.takenote.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity<U> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    @CreatedBy
    U createdBy;

    @LastModifiedBy
    U updatedBy;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    @Builder.Default
    Boolean isDeleted = false;
    LocalDateTime deletedAt;
    Long deletedBy;

    public BaseEntity() {
        this.isDeleted = false;
    }

    /**
     * This function is to prepare new entity by having isDeleted to false
     * When new entity is created using builder().build() isDeleted will not be
     */
    public void initNewEntity() {
        this.isDeleted = false;
    }
}

