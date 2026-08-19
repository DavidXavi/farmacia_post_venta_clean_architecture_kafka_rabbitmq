package com.posfarmacia.adapters.web.response.inventario;

import com.posfarmacia.usecases.dto.inventario.ProductoResult;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductoResponse(
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

    public static ProductoResponse desde(ProductoResult result) {
        return new ProductoResponse(
                result.id(),
                result.codigoInterno(),
                result.codigoBarras(),
                result.nombreComercial(),
                result.descripcion(),
                result.tipoProducto(),
                result.categoriaId(),
                result.laboratorioId(),
                result.presentacionId(),
                result.precioVenta(),
                result.esControlado(),
                result.requiereReceta(),
                result.tipoRecetaRequerida(),
                result.estado());
    }
}
