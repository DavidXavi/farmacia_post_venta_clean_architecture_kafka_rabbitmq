package com.posfarmacia.usecases.dto.mensajeria;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento de auditoria (RF19) publicado en Kafka (topico {@code pos.auditoria}) para
 * operaciones sensibles: anulaciones, notas de credito, cambios de precio, ajustes de
 * stock, validacion de recetas, cambios de promocion.
 */
public record EventoAuditoria(String operacion, String entidad, UUID entidadId, UUID usuarioId,
        String detalle, Instant ocurridoEn) {
}
