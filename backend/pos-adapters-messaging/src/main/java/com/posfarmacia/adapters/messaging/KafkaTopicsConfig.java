package com.posfarmacia.adapters.messaging;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/** Declara los topicos de Kafka; KafkaAdmin los crea al iniciar si el broker esta disponible. */
@Configuration
public class KafkaTopicsConfig {

    @Bean
    public NewTopic topicVentasConfirmadas() {
        return TopicBuilder.name(MensajeriaDestinos.TOPIC_VENTAS_CONFIRMADAS).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic topicAuditoria() {
        return TopicBuilder.name(MensajeriaDestinos.TOPIC_AUDITORIA).partitions(1).replicas(1).build();
    }
}
