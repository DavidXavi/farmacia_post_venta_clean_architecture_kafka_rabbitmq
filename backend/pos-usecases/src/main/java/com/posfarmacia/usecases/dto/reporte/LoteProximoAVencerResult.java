package com.posfarmacia.usecases.dto.reporte;

import java.time.LocalDate;
import java.util.UUID;

/** Fila del reporte de lotes proximos a vencer (RF17/RN36). */
public record LoteProximoAVencerResult(
        UUID loteId,
        String codigo,
        UUID productoId,
        LocalDate fechaVencimiento,
        int cantidadDisponible) {
}
