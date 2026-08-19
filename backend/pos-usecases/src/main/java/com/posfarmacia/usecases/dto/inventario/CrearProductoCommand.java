package com.posfarmacia.usecases.dto.inventario;

import com.posfarmacia.domain.enums.TipoProducto;
import com.posfarmacia.domain.enums.TipoReceta;
import java.math.BigDecimal;
import java.util.UUID;

/** Intencion de registrar un producto en el catalogo (RF03). */
public record CrearProductoCommand(
        String codigoInterno,
        String codigoBarras,
        String nombreComercial,
        String descripcion,
        TipoProducto tipoProducto,
        UUID categoriaId,
        UUID laboratorioId,
        UUID presentacionId,
        BigDecimal precioVenta,
        boolean esControlado,
        boolean requiereReceta,
        TipoReceta tipoRecetaRequerida) {
}
