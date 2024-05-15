package com.common.kafka.service;

import com.common.kafka.annotation.EnableKafkaConfig;
import com.common.kafka.dto.MessageDto;
import com.common.kafka.dto.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@EnableKafkaConfig
public class StreamNotificationService {

    private final String notificationTopicName;
    private final String emailingTopicName;
    private final KafkaTemplate<String, NotificationDto> notificationKafkaTemplate;
    private final KafkaTemplate<String, MessageDto> emailKafkaTemplate;

    public StreamNotificationService(
            @Value("${topic.notification.name}") String notificationTopicName,
            @Value("${topic.emailing.name}") String emailingTopicName,
            KafkaTemplate<String, NotificationDto> notificationKafkaTemplate,
            KafkaTemplate<String, MessageDto> emailKafkaTemplate
    ) {
        this.notificationTopicName = notificationTopicName;
        this.emailingTopicName = emailingTopicName;
        this.notificationKafkaTemplate = notificationKafkaTemplate;
        this.emailKafkaTemplate = emailKafkaTemplate;
    }

    public void sendNotification(
            String header,
            String text,
            List<UUID> userIds
    ) {
        NotificationDto notificationDto = createNotification(header, text, userIds);
        notificationKafkaTemplate.send(notificationTopicName, notificationDto);
    }

    public void sendMessage(
            String header,
            String text,
            List<String> userEmails
    ) {
        MessageDto messageDto = createMessage(header, text, userEmails);
        emailKafkaTemplate.send(emailingTopicName, messageDto);
    }

    public void sendEverywhere(
            String header,
            String text,
            List<UUID> userIds,
            List<String> userEmails
    ) {
        NotificationDto notificationDto = createNotification(header, text, userIds);
        MessageDto messageDto = createMessage(header, text, userEmails);

        notificationKafkaTemplate.send(notificationTopicName, notificationDto);
        emailKafkaTemplate.send(emailingTopicName, messageDto);
    }

    private NotificationDto createNotification(String header, String text, List<UUID> userIds) {
        return new NotificationDto(header, text, userIds);
    }

    private MessageDto createMessage(String header, String text, List<String> userEmails) {
        return new MessageDto(header, text, userEmails);
    }
}
