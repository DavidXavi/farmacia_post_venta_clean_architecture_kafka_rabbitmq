package com.posfarmacia.usecases.dto.reporte;

import java.time.LocalDate;
import java.util.UUID;

/** Filtro de {@code GenerarReporteIncentivosUseCase} (RF18). {@code usuarioId} es opcional. */
public record ReporteIncentivosQuery(LocalDate desde, LocalDate hasta, UUID usuarioId) {
}
