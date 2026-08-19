package com.posfarmacia.adapters.web.response.venta;

import com.posfarmacia.usecases.dto.venta.DetalleVentaResult;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record DetalleVentaResponse(
        UUID id,
        UUID productoId,
        String nombreProducto,
        int cantidad,
        BigDecimal precioUnitario,
        UUID promocionAplicadaId,
        UUID recetaId,
        BigDecimal descuentoMonto,
        BigDecimal impuestoMonto,
        BigDecimal subtotal,
        List<DetalleVentaLoteResponse> lotes) {

    public static DetalleVentaResponse desde(DetalleVentaResult result) {
        return new DetalleVentaResponse(
                result.id(),
                result.productoId(),
                result.nombreProducto(),
                result.cantidad(),
                result.precioUnitario(),
                result.promocionAplicadaId(),
                result.recetaId(),
                result.descuentoMonto(),
                result.impuestoMonto(),
                result.subtotal(),
                result.lotes().stream().map(DetalleVentaLoteResponse::desde).toList());
    }
}
