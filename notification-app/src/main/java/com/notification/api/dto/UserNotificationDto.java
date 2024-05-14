package com.notification.api.dto;

import com.notification.api.enums.NotificationStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserNotificationDto(
        UUID id,
        String header,
        String text,
        String userId,
        NotificationStatus status,
        LocalDateTime createdAt
) {
}
