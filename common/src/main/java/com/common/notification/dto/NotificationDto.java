package com.common.notification.dto;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
public record NotificationDto(
        String header,
        String text,
        List<UUID> userIds
) implements Serializable {
}
