package com.notification.api.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record NotificationPaginationResponse(
        Integer pageNumber,
        Integer totalPagesAmount,
        List<UserNotificationDto> notifications
) {
}
