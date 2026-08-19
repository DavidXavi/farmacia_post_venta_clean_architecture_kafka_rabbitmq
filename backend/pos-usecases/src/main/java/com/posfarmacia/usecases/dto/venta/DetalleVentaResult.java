package com.posfarmacia.usecases.dto.venta;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record DetalleVentaResult(
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
        List<DetalleVentaLoteResult> lotes) {
}
