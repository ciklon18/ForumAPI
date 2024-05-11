package com.forum.integration.notification;

import com.common.notification.dto.NotificationDto;
import com.forum.core.entity.TopicSubscription;
import com.forum.core.repository.TopicSubscriptionRepository;
import com.forum.kafka.StreamNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final StreamNotificationService streamNotificationService;
    private final TopicSubscriptionRepository topicSubscriptionRepository;

    public void sendMessageNotificationToUsers(UUID topicId, UUID authorId) {
        List<UUID> userIds = getUserIdsForMailing(topicId, authorId);
        if (userIds.isEmpty()) return;
        NotificationDto notification = createNotification(userIds, authorId);
        streamNotificationService.sendNotification(notification);
    }

    private List<UUID> getUserIdsForMailing(UUID topicId, UUID authorId) {
        return topicSubscriptionRepository.findAllByTopicId(topicId)
                .stream()
                .map(TopicSubscription::getUserId)
                .filter(uuid -> !Objects.equals(uuid, authorId))
                .toList();
    }

    private NotificationDto createNotification(List<UUID> userIds, UUID authorId) {
        return NotificationDto.builder()
                .header("Новое сообщение в чате")
                .text("Пользователь с id=" + authorId + " написал новое сообщение.")
                .userIds(userIds)
                .build();
    }
}
