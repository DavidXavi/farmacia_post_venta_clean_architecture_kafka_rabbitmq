package com.posfarmacia.usecases.dto.venta;

import java.util.UUID;

/** Entrada de {@code IdentificarClienteEnVentaUseCase} (RF09/RN10/RN22/RN28). */
public record IdentificarClienteCommand(UUID ventaId, String dni) {
}
