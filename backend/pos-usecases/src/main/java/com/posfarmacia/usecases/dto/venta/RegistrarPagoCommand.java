package com.posfarmacia.usecases.dto.venta;

import java.math.BigDecimal;
import java.util.UUID;

/** Entrada de {@code RegistrarPagoUseCase} (RF12). */
public record RegistrarPagoCommand(UUID ventaId, UUID formaPagoId, BigDecimal monto, String codigoAutorizacion) {
}
