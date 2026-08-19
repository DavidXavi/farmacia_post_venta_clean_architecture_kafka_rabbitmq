package com.posfarmacia.adapters.web.request.inventario;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/** RF04: registro del ingreso de un lote de mercaderia. */
public record RegistrarLoteRequest(
        @NotBlank String codigo,
        @NotNull UUID productoId,
        @NotNull LocalDate fechaVencimiento,
        @Min(1) int cantidadRecibida,
        @NotNull UUID localId,
        @DecimalMin(value = "0.0", inclusive = true) BigDecimal costo) {
}
