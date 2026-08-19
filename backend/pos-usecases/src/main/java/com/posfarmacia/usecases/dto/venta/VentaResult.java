package com.posfarmacia.usecases.dto.venta;

import com.posfarmacia.domain.enums.EstadoVenta;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record VentaResult(
        UUID id,
        UUID cajaId,
        UUID sesionCajaId,
        UUID usuarioId,
        UUID clienteId,
        UUID convenioSeguroId,
        UUID lineaCreditoId,
        Instant fecha,
        EstadoVenta estado,
        Long numeroCorrelativo,
        String numeroComprobante,
        BigDecimal total,
        BigDecimal totalPagado,
        List<DetalleVentaResult> detalles,
        List<PagoResult> pagos) {
}
