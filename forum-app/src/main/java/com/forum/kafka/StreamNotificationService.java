package com.forum.kafka;

import com.common.notification.dto.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StreamNotificationService {

    private final String topicName;
    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;

    public StreamNotificationService(
            @Value("${topic.notification.name}") String topicName,
            KafkaTemplate<String, NotificationDto> kafkaTemplate
    ) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(NotificationDto notificationDto) {
        kafkaTemplate.send(topicName, notificationDto);
    }
}
