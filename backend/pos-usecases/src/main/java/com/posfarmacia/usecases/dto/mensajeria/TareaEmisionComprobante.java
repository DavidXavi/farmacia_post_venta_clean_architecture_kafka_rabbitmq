package com.posfarmacia.usecases.dto.mensajeria;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Tarea encolada en RabbitMQ (cola {@code pos.comprobantes.emitir}) para emitir el
 * comprobante electronico de una venta de forma asincrona: un unico consumidor la toma,
 * la checkout no espera la respuesta del emisor externo.
 */
public record TareaEmisionComprobante(UUID ventaId, long numeroCorrelativo, String tipoComprobante,
        String serieComprobante, BigDecimal total) {
}
