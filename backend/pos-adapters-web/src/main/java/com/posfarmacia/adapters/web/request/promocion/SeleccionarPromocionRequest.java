package com.posfarmacia.adapters.web.request.promocion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

/**
 * Cuerpo del endpoint que registra la promocion elegida por el cajero para una linea de venta.
 * {@code promocionesYaAplicadasEnComprobante} lo aporta el contexto de Ventas (dueño del
 * comprobante) para que el dominio valide RN09.
 */
public record SeleccionarPromocionRequest(
        @NotNull UUID promocionId,
        @NotNull UUID ventaId,
        @NotNull UUID detalleVentaId,
        @NotNull UUID productoId,
        @Min(1) int cantidad,
        @NotNull @PositiveOrZero BigDecimal precioUnitario,
        boolean clienteIdentificado,
        Set<UUID> promocionesYaAplicadasEnComprobante) {
}
