package com.notification.kafka.consumer;

import com.common.kafka.dto.NotificationDto;
import com.notification.core.service.NotificationStreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final NotificationStreamService notificationStreamService;

    @KafkaListener(topics = "${topic.notification.name}", groupId = "${topic.notification.group-id}")
    public void listenNotificationTopic(NotificationDto notificationDto) {
        log.info("Received from history topic: {}", notificationDto.toString());
        notificationStreamService.handleNotification(notificationDto);
    }
}
