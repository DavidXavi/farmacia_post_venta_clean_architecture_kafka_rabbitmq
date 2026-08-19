package com.posfarmacia.adapters.web.request.inventario;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/** RF03: edicion de los datos comerciales de un producto existente. */
public record ActualizarProductoRequest(
        @NotBlank String nombreComercial,
        String descripcion,
        @NotNull @DecimalMin(value = "0.0", inclusive = true) BigDecimal precioVenta) {
}
