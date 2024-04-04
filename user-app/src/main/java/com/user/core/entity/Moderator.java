package com.user.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "moderator")
public class Moderator {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "category_id")
    private UUID categoryId;

    @PrePersist
    public void ensureIdAssigned() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
