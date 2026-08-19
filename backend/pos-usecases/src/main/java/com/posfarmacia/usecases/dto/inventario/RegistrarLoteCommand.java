package com.posfarmacia.usecases.dto.inventario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/** Intencion de registrar el ingreso de un lote (RF04). */
public record RegistrarLoteCommand(
        String codigo,
        UUID productoId,
        LocalDate fechaVencimiento,
        int cantidadRecibida,
        UUID localId,
        BigDecimal costo) {
}
