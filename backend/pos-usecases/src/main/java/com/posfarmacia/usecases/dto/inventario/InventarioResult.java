package com.posfarmacia.usecases.dto.inventario;

import java.time.Instant;
import java.util.UUID;

/** Proyeccion de lectura del rollup de existencias (RF04/RF15) consultado por producto+local. */
public record InventarioResult(UUID productoId, UUID localId, int cantidadActual, Instant actualizadoEn) {
}
