package com.posfarmacia.adapters.web.request.venta;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/** RF05: agrega un producto a la venta. {@code recetaId} es obligatorio si el producto es controlado (RN14). */
public record AgregarProductoRequest(@NotNull UUID productoId, @Min(1) int cantidad, UUID recetaId) {
}
