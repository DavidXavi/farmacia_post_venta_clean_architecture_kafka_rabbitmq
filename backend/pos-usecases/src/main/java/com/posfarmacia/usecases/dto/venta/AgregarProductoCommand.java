package com.posfarmacia.usecases.dto.venta;

import java.util.UUID;

/** Entrada de {@code AgregarProductoAVentaUseCase} (RF05). {@code recetaId} es obligatorio si el producto es controlado (RN14). */
public record AgregarProductoCommand(UUID productoId, int cantidad, UUID recetaId) {
}
