package com.posfarmacia.adapters.web.response.reporte;

import com.posfarmacia.usecases.dto.reporte.IncentivoResumenResult;
import java.math.BigDecimal;
import java.util.UUID;

public record IncentivoResumenResponse(
        UUID usuarioId,
        UUID productoId,
        int cantidadVendida,
        String reglaAplicada,
        BigDecimal montoTotal) {

    public static IncentivoResumenResponse desde(IncentivoResumenResult result) {
        return new IncentivoResumenResponse(result.usuarioId(), result.productoId(), result.cantidadVendida(),
                result.reglaAplicada(), result.montoTotal());
    }
}
