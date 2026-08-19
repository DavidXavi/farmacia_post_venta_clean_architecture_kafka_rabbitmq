package com.posfarmacia.usecases.dto.inventario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/** Proyeccion de lectura del agregado Lote para los adaptadores de entrada. */
public record LoteResult(
        UUID id,
        String codigo,
        UUID productoId,
        LocalDate fechaVencimiento,
        int cantidadRecibida,
        int cantidadDisponible,
        BigDecimal costo,
        UUID localId,
        String estado) {
}
