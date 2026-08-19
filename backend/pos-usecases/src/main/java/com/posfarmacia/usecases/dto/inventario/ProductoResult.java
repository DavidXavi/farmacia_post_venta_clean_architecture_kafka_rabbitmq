package com.posfarmacia.usecases.dto.inventario;

import java.math.BigDecimal;
import java.util.UUID;

/** Proyeccion de lectura del agregado Producto para los adaptadores de entrada. */
public record ProductoResult(
        UUID id,
        String codigoInterno,
        String codigoBarras,
        String nombreComercial,
        String descripcion,
        String tipoProducto,
        UUID categoriaId,
        UUID laboratorioId,
        UUID presentacionId,
        BigDecimal precioVenta,
        boolean esControlado,
        boolean requiereReceta,
        String tipoRecetaRequerida,
        String estado) {
}
