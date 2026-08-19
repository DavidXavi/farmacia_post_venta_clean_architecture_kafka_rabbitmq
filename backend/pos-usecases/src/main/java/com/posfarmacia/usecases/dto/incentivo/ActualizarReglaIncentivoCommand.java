package com.posfarmacia.usecases.dto.incentivo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ActualizarReglaIncentivoCommand(
        String nombre,
        UUID productoId,
        UUID categoriaId,
        BigDecimal montoPorUnidad,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        boolean activa) {
}
