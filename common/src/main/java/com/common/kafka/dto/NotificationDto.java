package com.common.kafka.dto;

import com.common.kafka.enums.NotificationType;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public record NotificationDto(
        String header,
        String text,
        List<String> userData,
        NotificationType notificationType
) implements Serializable {
}
