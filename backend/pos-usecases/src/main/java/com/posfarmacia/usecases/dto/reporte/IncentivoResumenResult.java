package com.posfarmacia.usecases.dto.reporte;

import java.math.BigDecimal;
import java.util.UUID;

/** Fila del reporte de incentivos (RF18): trabajador, producto, cantidad, regla y monto resultante. */
public record IncentivoResumenResult(
        UUID usuarioId,
        UUID productoId,
        int cantidadVendida,
        String reglaAplicada,
        BigDecimal montoTotal) {
}
