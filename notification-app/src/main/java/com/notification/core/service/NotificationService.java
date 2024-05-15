package com.notification.core.service;

import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.common.kafka.dto.NotificationDto;
import com.notification.api.dto.NotificationPaginationResponse;
import com.notification.api.dto.UserNotificationDto;
import com.notification.api.enums.NotificationStatus;
import com.notification.core.entity.NotificationEntity;
import com.notification.core.mapper.NotificationMapper;
import com.notification.core.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    public static final Integer DEFAULT_PAGE_NUMBER = 1;
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Transactional(readOnly = true)
    public NotificationPaginationResponse getNotifications(
            UUID userId,
            Integer pageNumber,
            Integer pageSize,
            String query,
            Boolean isSortedByDateDescending
    ) {
        Integer totalPagesAmount =
                (int) Math.ceil((double) notificationRepository.getNotificationsCount(userId, query) / pageSize);
        pageNumber = pageNumber <= totalPagesAmount ? pageNumber : totalPagesAmount;
        Integer offset = (Math.max((pageNumber - 1), 0)) * pageSize;

        List<UserNotificationDto> notifications = notificationRepository.getNotifications(
                        userId,
                        query,
                        pageSize,
                        offset
                )
                .stream()
                .map(notificationMapper::map)
                .sorted((notification1, notification2) -> {
                    int statusComparison = notification1.status().compareTo(notification2.status());
                    if (statusComparison != 0) {
                        return statusComparison;
                    }
                    if (isSortedByDateDescending) {
                        return notification2.createdAt().compareTo(notification1.createdAt());
                    } else {
                        return notification1.createdAt().compareTo(notification2.createdAt());
                    }
                })
                .toList();

        return NotificationPaginationResponse.builder()
                .notifications(notifications)
                .pageNumber(pageNumber)
                .totalPagesAmount(totalPagesAmount)
                .build();
    }

    @Transactional(readOnly = true)
    public Integer getUnreadNotificationsCount(UUID userId) {
        return notificationRepository.getUnreadNotificationsCount(userId);
    }

    @Transactional
    public void readNotifications(List<UUID> notificationIds) {
        if (notificationIds.isEmpty()) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "No one notificationId was presented");
        }
        notificationRepository.updateNotificationsStatus(
                notificationIds,
                NotificationStatus.READ.name()
        );
    }

    public void saveNotificationsToHistory(NotificationDto notificationDto) {
        List<NotificationEntity> entityList = notificationDto.userIds()
                .stream()
                .map(userId -> {
                    NotificationEntity entity = new NotificationEntity();
                    entity.setHeader(notificationDto.header());
                    entity.setText(notificationDto.text());
                    entity.setNotificationStatus(NotificationStatus.NEW);
                    entity.setUserId(userId);
                    entity.setCreatedAt(LocalDateTime.now());
                    entity.setUpdatedAt(LocalDateTime.now());
                    return entity;
                })
                .toList();

        notificationRepository.saveAll(entityList);
    }
}
