package com.posfarmacia.adapters.web.request.inventario;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

/** RF03: alta de un producto del catalogo. */
public record CrearProductoRequest(
        @NotBlank String codigoInterno,
        String codigoBarras,
        @NotBlank String nombreComercial,
        String descripcion,
        @NotBlank String tipoProducto,
        @NotNull UUID categoriaId,
        @NotNull UUID laboratorioId,
        @NotNull UUID presentacionId,
        @NotNull @DecimalMin(value = "0.0", inclusive = true) BigDecimal precioVenta,
        boolean esControlado,
        boolean requiereReceta,
        String tipoRecetaRequerida) {
}
