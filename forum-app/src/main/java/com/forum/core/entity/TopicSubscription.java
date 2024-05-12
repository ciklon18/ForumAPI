package com.forum.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "topic_subscription")
@NoArgsConstructor
public class TopicSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID topicId;

    private UUID userId;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    public TopicSubscription(UUID topicId, UUID userId) {
        this.id = UUID.randomUUID();
        this.topicId = topicId;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();

    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
