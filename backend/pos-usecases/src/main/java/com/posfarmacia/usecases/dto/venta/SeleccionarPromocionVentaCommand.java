package com.posfarmacia.usecases.dto.venta;

import java.util.UUID;

/** Entrada de {@code SeleccionarPromocionVentaUseCase} (RN07-RN12): la promocion elegida por el cajero para una linea. */
public record SeleccionarPromocionVentaCommand(UUID ventaId, UUID detalleVentaId, UUID promocionId) {
}
