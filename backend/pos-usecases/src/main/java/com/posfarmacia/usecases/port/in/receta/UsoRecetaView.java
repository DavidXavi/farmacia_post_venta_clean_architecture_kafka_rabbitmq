package com.posfarmacia.usecases.port.in.receta;

import java.time.Instant;
import java.util.UUID;

/** Fila del historial de usos de una receta, salida de {@link ConsultarHistorialRecetasUseCase}. */
public record UsoRecetaView(UUID id, UUID recetaId, UUID ventaId, Instant fecha) {
}
