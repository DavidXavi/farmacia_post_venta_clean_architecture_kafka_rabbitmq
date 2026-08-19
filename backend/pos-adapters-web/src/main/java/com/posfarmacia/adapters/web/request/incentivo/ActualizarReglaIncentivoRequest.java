package com.posfarmacia.adapters.web.request.incentivo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ActualizarReglaIncentivoRequest(
        @NotBlank String nombre,
        UUID productoId,
        UUID categoriaId,
        @NotNull BigDecimal montoPorUnidad,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        boolean activa) {
}
