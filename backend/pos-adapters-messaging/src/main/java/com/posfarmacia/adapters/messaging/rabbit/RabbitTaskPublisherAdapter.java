package com.posfarmacia.adapters.messaging.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.posfarmacia.adapters.messaging.MensajeriaDestinos;
import com.posfarmacia.usecases.dto.mensajeria.TareaEmisionComprobante;
import com.posfarmacia.usecases.port.out.mensajeria.TareaAsincronaPublisherPort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Adaptador de salida hacia RabbitMQ: encola la tarea como JSON para que un unico
 * consumidor la procese fuera del hilo de checkout (ver {@code ComprobanteEmisionConsumer}).
 */
@Component
public class RabbitTaskPublisherAdapter implements TareaAsincronaPublisherPort {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public RabbitTaskPublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void encolarEmisionComprobante(TareaEmisionComprobante tarea) {
        try {
            rabbitTemplate.convertAndSend(MensajeriaDestinos.QUEUE_COMPROBANTES_EMITIR,
                    objectMapper.writeValueAsString(tarea));
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo encolar la emision de comprobante en RabbitMQ.", e);
        }
    }
}
