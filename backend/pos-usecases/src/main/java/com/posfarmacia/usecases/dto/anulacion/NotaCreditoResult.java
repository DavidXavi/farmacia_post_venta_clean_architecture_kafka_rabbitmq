package com.posfarmacia.usecases.dto.anulacion;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record NotaCreditoResult(UUID id, UUID ventaId, UUID comprobanteId, UUID usuarioId, String motivo,
        BigDecimal montoTotal, Instant fecha) {
}
