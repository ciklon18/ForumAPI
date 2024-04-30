package com.forum.core.service;

import com.notification.core.producer.EnableRabbitMQProducer;
import com.notification.core.producer.RabbitMQProducer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@EnableRabbitMQProducer
public class ForumStreamService {

    private final RabbitMQProducer rabbitMQProducer;

    public ForumStreamService(RabbitMQProducer rabbitMQProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
    }

    private String getRoutingKeyByTopicId(UUID topicId) {
        return rabbitMQProducer.getRoutingKey().concat(String.valueOf(topicId));
    }

    public void sendNotificationToUsers(String message, UUID topicId) {
        if (!rabbitMQProducer.isQueueExistByTopicId(topicId)) {
            rabbitMQProducer.createQueueByTopicId(topicId);
        }
        rabbitMQProducer.sendMessage(message, getRoutingKeyByTopicId(topicId));
    }
}
