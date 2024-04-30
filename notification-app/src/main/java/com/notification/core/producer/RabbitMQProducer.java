package com.notification.core.producer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class RabbitMQProducer {

    private final AmqpTemplate amqpTemplate;
    private final AmqpAdmin amqpAdmin;
    private final String exchange;
    private final String topicQueueName;
    @Getter
    private final String routingKey;

    public RabbitMQProducer(
            AmqpTemplate amqpTemplate,
            AmqpAdmin amqpAdmin,
            @Value("${rabbitmq.exchange.name:exchange}") String exchange,
            @Value("${rabbitmq.queue.topic:topic_queue}") String topicQueueName,
            @Value("${rabbitmq.routing_key:routing_key}") String routingKey
    ) {
        this.amqpTemplate = amqpTemplate;
        this.amqpAdmin = amqpAdmin;
        this.exchange = exchange;
        this.topicQueueName = topicQueueName;
        this.routingKey = routingKey;
    }

    public boolean isQueueExistByTopicId(UUID topicId) {
        String queueName = topicQueueName + topicId;
        QueueInformation information = amqpAdmin.getQueueInfo(queueName);
        return !Objects.equals(information, null);
    }

    public void sendMessage(String message, String routingKey) {
        log.info("Sending message - {} to {}", message, routingKey);
        amqpTemplate.convertAndSend(exchange, routingKey, message);
    }

    public void createQueueByTopicId(UUID topicId) {
        String queueName = topicQueueName + topicId;
        String queueRoutingKey = routingKey + topicId;

        amqpAdmin.declareQueue(new Queue(queueName, true));
        amqpAdmin.declareBinding(new Binding(
                queueName,
                Binding.DestinationType.QUEUE,
                exchange,
                queueRoutingKey,
                null
        ));
    }
}
