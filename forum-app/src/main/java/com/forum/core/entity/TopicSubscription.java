package com.forum.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class TopicSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID topicId;

    private UUID userId;

    public TopicSubscription(UUID topicId, UUID userId) {
        this.id = UUID.randomUUID();
        this.topicId = topicId;
        this.userId = userId;
    }
}
