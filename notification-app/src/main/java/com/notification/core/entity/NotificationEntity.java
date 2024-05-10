package com.notification.core.entity;

import com.notification.api.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity(name = "notification")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String header;

    private String text;

    @Column(nullable = false)
    private NotificationStatus notificationStatus;

    @Column(nullable = false)
    private UUID userId;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
