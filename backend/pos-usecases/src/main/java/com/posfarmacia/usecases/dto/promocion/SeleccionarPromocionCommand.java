package com.posfarmacia.usecases.dto.promocion;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

/**
 * Entrada de {@code SeleccionarPromocionUseCase}: la promocion elegida por el cajero para una
 * linea de venta puntual. {@code ventaId}/{@code detalleVentaId} solo se referencian por UUID
 * porque Venta pertenece al contexto de Ventas, no al de Promociones. El conjunto
 * {@code promocionesYaAplicadasEnComprobante} lo aporta el caller (dueño del comprobante) para
 * que el dominio valide RN09.
 */
public record SeleccionarPromocionCommand(
        UUID promocionId,
        UUID ventaId,
        UUID detalleVentaId,
        UUID productoId,
        int cantidad,
        BigDecimal precioUnitario,
        boolean clienteIdentificado,
        Set<UUID> promocionesYaAplicadasEnComprobante) {
}
