package com.posfarmacia.adapters.web.response.venta;

import com.posfarmacia.usecases.dto.venta.VentaResult;
import com.posfarmacia.domain.enums.EstadoVenta;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * {@code estado} se serializa en PascalCase ("EnProceso"/"Confirmada"/"Anulada") en vez del nombre
 * literal del enum de Java, porque el frontend React ya copiado de arquitectura_2_t2
 * (`VentaPage.jsx`) compara {@code venta.estado === 'Confirmada'} tal como lo devolvia el backend
 * .NET original; es la unica forma de no romper ese contrato sin reescribir el frontend.
 */
public record VentaResponse(
        UUID id,
        UUID cajaId,
        UUID sesionCajaId,
        UUID usuarioId,
        UUID clienteId,
        UUID convenioSeguroId,
        UUID lineaCreditoId,
        Instant fecha,
        String estado,
        Long numeroCorrelativo,
        String numeroComprobante,
        BigDecimal total,
        BigDecimal totalPagado,
        List<DetalleVentaResponse> detalles,
        List<PagoResponse> pagos) {

    public static VentaResponse desde(VentaResult result) {
        return new VentaResponse(
                result.id(),
                result.cajaId(),
                result.sesionCajaId(),
                result.usuarioId(),
                result.clienteId(),
                result.convenioSeguroId(),
                result.lineaCreditoId(),
                result.fecha(),
                aTextoFrontend(result.estado()),
                result.numeroCorrelativo(),
                result.numeroComprobante(),
                result.total(),
                result.totalPagado(),
                result.detalles().stream().map(DetalleVentaResponse::desde).toList(),
                result.pagos().stream().map(PagoResponse::desde).toList());
    }

    private static String aTextoFrontend(EstadoVenta estado) {
        return switch (estado) {
            case EN_PROCESO -> "EnProceso";
            case CONFIRMADA -> "Confirmada";
            case ANULADA -> "Anulada";
        };
    }
}
