package com.posfarmacia.usecases.dto.mensajeria;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Evento de dominio publicado en Kafka (topico {@code pos.ventas.confirmadas}) cuando
 * {@code ConfirmarVentaUseCaseImpl} confirma una venta. Es un hecho de negocio ya ocurrido
 * y consumible por cualquier interesado (analitica, auditoria, actividad reciente), a
 * diferencia de una tarea puntual como {@link TareaEmisionComprobante}.
 */
public record VentaConfirmadaEvent(UUID ventaId, long numeroCorrelativo, String tipoComprobante,
        BigDecimal total, UUID usuarioId, Instant confirmadaEn) {
}
