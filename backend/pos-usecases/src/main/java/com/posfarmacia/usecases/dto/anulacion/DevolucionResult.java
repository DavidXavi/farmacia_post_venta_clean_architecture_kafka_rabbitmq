package com.posfarmacia.usecases.dto.anulacion;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DevolucionResult(UUID id, UUID ventaId, UUID usuarioId, String motivo, Instant fecha,
        BigDecimal total, List<DetalleDevolucionResult> detalles) {
}
