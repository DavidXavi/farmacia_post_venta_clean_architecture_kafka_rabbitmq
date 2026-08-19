package com.posfarmacia.adapters.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.posfarmacia.adapters.messaging.MensajeriaDestinos;
import com.posfarmacia.usecases.dto.mensajeria.EventoAuditoria;
import com.posfarmacia.usecases.port.in.identidad.RegistrarAuditoriaUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consume el topico {@code pos.auditoria} (RF19: anulaciones, notas de credito, cambios de
 * precio, ajustes de stock, validacion de recetas, cambios de promocion) y recien aqui
 * persiste el {@code RegistroAuditoria} en la base de datos, via el mismo puerto de entrada
 * que ya expone {@code /api/auditoria}. Antes de este cambio, {@code RegistrarAuditoriaUseCase}
 * no tenia ningun llamador: el evento sensible ocurria pero no quedaba evidencia. Ahora el
 * caso de uso que dispara la operacion (por ejemplo {@code EmitirNotaCreditoUseCaseImpl}) solo
 * publica el hecho en Kafka; escribir la auditoria es responsabilidad de este consumidor,
 * desacoplada de la transaccion original.
 */
@Component
public class AuditoriaConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(AuditoriaConsumer.class);

    private final RegistrarAuditoriaUseCase registrarAuditoria;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public AuditoriaConsumer(RegistrarAuditoriaUseCase registrarAuditoria) {
        this.registrarAuditoria = registrarAuditoria;
    }

    @KafkaListener(topics = MensajeriaDestinos.TOPIC_AUDITORIA, groupId = "pos-auditoria")
    public void escuchar(String payload) {
        try {
            EventoAuditoria evento = objectMapper.readValue(payload, EventoAuditoria.class);
            registrarAuditoria.registrar(evento.usuarioId(), evento.operacion(), evento.entidad(),
                    evento.entidadId().toString(), evento.detalle(), null, null);
        } catch (Exception e) {
            LOG.error("No se pudo registrar el evento de auditoria: {}", payload, e);
        }
    }
}
