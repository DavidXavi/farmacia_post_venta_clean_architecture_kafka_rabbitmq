package com.posfarmacia.usecases.dto.venta;

import java.time.LocalDate;
import java.util.UUID;

/** Filtro de {@code ConsultarVentasDiariasUseCase} (RF17). Todos los campos son opcionales. */
public record ConsultarVentasDiariasQuery(LocalDate fecha, UUID cajaId, UUID usuarioId, UUID clienteId) {
}
