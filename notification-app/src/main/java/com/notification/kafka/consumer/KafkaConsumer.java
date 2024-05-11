package com.notification.kafka.consumer;

import com.common.notification.dto.NotificationDto;
import com.notification.api.enums.NotificationStatus;
import com.notification.core.entity.NotificationEntity;
import com.notification.core.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "${topic.forum.name}", groupId = "1")
    public void listen(NotificationDto notificationDto) {

        log.info("Received: {}", notificationDto.toString());
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
