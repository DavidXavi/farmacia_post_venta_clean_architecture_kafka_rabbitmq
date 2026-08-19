package com.posfarmacia.usecases.dto.anulacion;

import java.math.BigDecimal;
import java.util.UUID;

public record DetalleDevolucionResult(UUID id, UUID detalleVentaId, UUID productoId, int cantidad,
        BigDecimal montoDevuelto) {
}
