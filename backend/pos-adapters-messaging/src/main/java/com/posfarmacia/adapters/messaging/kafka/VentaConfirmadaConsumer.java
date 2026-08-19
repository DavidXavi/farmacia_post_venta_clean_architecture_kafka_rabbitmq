package com.posfarmacia.adapters.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.posfarmacia.adapters.messaging.MensajeriaDestinos;
import com.posfarmacia.usecases.dto.mensajeria.ActividadReciente;
import com.posfarmacia.usecases.dto.mensajeria.VentaConfirmadaEvent;
import com.posfarmacia.usecases.port.in.mensajeria.RegistrarActividadUseCase;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consume el topico {@code pos.ventas.confirmadas}: en un sistema real este seria el punto
 * de entrada de analitica de ventas o del cierre de caja; aqui alimenta el feed de
 * actividad reciente para comprobar en el navegador que Kafka esta entregando el evento.
 */
@Component
public class VentaConfirmadaConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(VentaConfirmadaConsumer.class);

    private final RegistrarActividadUseCase registrarActividad;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public VentaConfirmadaConsumer(RegistrarActividadUseCase registrarActividad) {
        this.registrarActividad = registrarActividad;
    }

    @KafkaListener(topics = MensajeriaDestinos.TOPIC_VENTAS_CONFIRMADAS, groupId = "pos-actividad-reciente")
    public void escuchar(String payload) {
        try {
            VentaConfirmadaEvent evento = objectMapper.readValue(payload, VentaConfirmadaEvent.class);
            registrarActividad.registrar(new ActividadReciente("KAFKA", "VENTA_CONFIRMADA",
                    "Venta " + evento.ventaId() + " confirmada (" + evento.tipoComprobante() + " por "
                            + evento.total() + ")",
                    Instant.now()));
        } catch (Exception e) {
            LOG.error("No se pudo procesar el evento de venta confirmada: {}", payload, e);
        }
    }
}
