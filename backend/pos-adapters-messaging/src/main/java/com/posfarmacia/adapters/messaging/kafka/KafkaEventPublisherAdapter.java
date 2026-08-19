package com.posfarmacia.adapters.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.posfarmacia.adapters.messaging.MensajeriaDestinos;
import com.posfarmacia.usecases.dto.mensajeria.EventoAuditoria;
import com.posfarmacia.usecases.dto.mensajeria.VentaConfirmadaEvent;
import com.posfarmacia.usecases.port.out.mensajeria.EventoDominioPublisherPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Adaptador de salida hacia Apache Kafka: serializa el evento a JSON y lo publica en el
 * topico correspondiente. Un fallo al publicar no revierte la venta ya confirmada (ver
 * {@code ConfirmarVentaUseCaseImpl}); solo impide que el evento llegue a los consumidores.
 */
@Component
public class KafkaEventPublisherAdapter implements EventoDominioPublisherPort {

    // Tipo crudo (sin parametrizar): el KafkaTemplate autoconfigurado por Spring Boot es
    // KafkaTemplate<Object, Object>; usar aqui el mismo <String, String> del resto de la
    // clase impediria a Spring encontrarlo por coincidencia exacta de tipo generico.
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private final KafkaTemplate kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public KafkaEventPublisherAdapter(@SuppressWarnings("rawtypes") KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publicarVentaConfirmada(VentaConfirmadaEvent evento) {
        enviar(MensajeriaDestinos.TOPIC_VENTAS_CONFIRMADAS, evento.ventaId().toString(), evento);
    }

    @Override
    public void publicarAuditoria(EventoAuditoria evento) {
        enviar(MensajeriaDestinos.TOPIC_AUDITORIA, evento.entidadId().toString(), evento);
    }

    private void enviar(String topico, String clave, Object payload) {
        try {
            kafkaTemplate.send(topico, clave, objectMapper.writeValueAsString(payload));
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo publicar el evento en Kafka (topico " + topico + ").", e);
        }
    }
}
