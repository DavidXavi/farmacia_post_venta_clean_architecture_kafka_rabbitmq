package com.posfarmacia.adapters.messaging.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.posfarmacia.adapters.messaging.MensajeriaDestinos;
import com.posfarmacia.usecases.dto.mensajeria.ActividadReciente;
import com.posfarmacia.usecases.dto.mensajeria.TareaEmisionComprobante;
import com.posfarmacia.usecases.port.in.mensajeria.RegistrarActividadUseCase;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consume la cola {@code pos.comprobantes.emitir}: simula la llamada al emisor de
 * comprobante electronico externo (SUNAT u homologo) de forma asincrona, desacoplada del
 * checkout. En un sistema real, aqui iria el cliente HTTP hacia ese servicio.
 */
@Component
public class ComprobanteEmisionConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(ComprobanteEmisionConsumer.class);

    private final RegistrarActividadUseCase registrarActividad;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public ComprobanteEmisionConsumer(RegistrarActividadUseCase registrarActividad) {
        this.registrarActividad = registrarActividad;
    }

    @RabbitListener(queues = MensajeriaDestinos.QUEUE_COMPROBANTES_EMITIR)
    public void escuchar(String payload) {
        try {
            TareaEmisionComprobante tarea = objectMapper.readValue(payload, TareaEmisionComprobante.class);
            registrarActividad.registrar(new ActividadReciente("RABBITMQ", "COMPROBANTE_EMITIDO",
                    "Comprobante " + tarea.tipoComprobante() + " " + tarea.serieComprobante() + "-"
                            + tarea.numeroCorrelativo() + " emitido para la venta " + tarea.ventaId() + " (total "
                            + tarea.total() + ")",
                    Instant.now()));
        } catch (Exception e) {
            LOG.error("No se pudo procesar la emision de comprobante: {}", payload, e);
        }
    }
}
