package com.common.notification.dto;

public record MessageDto(
        String recipient,
        String subject,
        String content
) {
}
