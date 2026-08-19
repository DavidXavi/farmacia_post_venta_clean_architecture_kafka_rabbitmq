package com.posfarmacia.adapters.web.request.promocion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/** Cuerpo de {@code POST /api/v1/promociones/evaluar} (Word, seccion 10). */
public record EvaluarPromocionesRequest(
        @NotNull UUID productoId,
        @Min(1) int cantidad,
        boolean clienteIdentificado) {
}
