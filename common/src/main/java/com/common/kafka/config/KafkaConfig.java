package com.common.kafka.config;

import com.common.kafka.dto.MessageDto;
import com.common.kafka.dto.NotificationDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

//@EnableKafka
@Configuration
public class KafkaConfig {
    private final String bootstrapServers;

    public KafkaConfig(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    @Bean
    public KafkaTemplate<String, NotificationDto> notificationKafkaTemplate() {
        return new KafkaTemplate<>(notificationProducerFactory());
    }

    @Bean
    ProducerFactory<String, NotificationDto> notificationProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerFactoryConfigProps());
    }

    @Bean
    public KafkaTemplate<String, MessageDto> messageKafkaTemplate() {
        return new KafkaTemplate<>(messageProducerFactory());
    }

    @Bean
    ProducerFactory<String, MessageDto> messageProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerFactoryConfigProps());
    }

    private Map<String, Object> producerFactoryConfigProps() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return configProps;
    }
}
