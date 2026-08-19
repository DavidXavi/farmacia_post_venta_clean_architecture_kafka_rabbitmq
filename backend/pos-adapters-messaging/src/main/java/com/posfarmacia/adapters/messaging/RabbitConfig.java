package com.posfarmacia.adapters.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Declara las colas de RabbitMQ; se crean al iniciar si el broker esta disponible. */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue queueComprobantesEmitir() {
        return new Queue(MensajeriaDestinos.QUEUE_COMPROBANTES_EMITIR, true);
    }
}
