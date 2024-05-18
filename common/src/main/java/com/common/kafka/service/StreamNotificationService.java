package com.common.kafka.service;

import com.common.kafka.annotation.EnableKafkaConfig;
import com.common.kafka.dto.NotificationDto;
import com.common.kafka.enums.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@EnableKafkaConfig
public class StreamNotificationService {

    private final String notificationTopicName;
    private final KafkaTemplate<String, NotificationDto> notificationKafkaTemplate;

    public StreamNotificationService(
            @Value("${topic.notification.name}") String notificationTopicName,
            KafkaTemplate<String, NotificationDto> notificationKafkaTemplate
    ) {
        this.notificationTopicName = notificationTopicName;
        this.notificationKafkaTemplate = notificationKafkaTemplate;
    }

    public void sendNotification(
            String header,
            String text,
            List<String> userData,
            NotificationType notificationType

    ) {
        NotificationDto notificationDto = createNotification(header, text, userData, notificationType);
        notificationKafkaTemplate.send(notificationTopicName, notificationDto);
    }

    private NotificationDto createNotification(
            String header,
            String text,
            List<String> userData,
            NotificationType type
    ) {
        return new NotificationDto(header, text, userData, type);
    }
}
