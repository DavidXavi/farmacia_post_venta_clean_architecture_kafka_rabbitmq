package com.posfarmacia.usecases.dto.venta;

import java.util.UUID;

/** Entrada de {@code AplicarConvenioAVentaUseCase} (RF10/RN22-RN26): copago de vista previa, recalculado igualmente al confirmar (RN04). */
public record AplicarConvenioCommand(UUID ventaId, UUID convenioId) {
}
