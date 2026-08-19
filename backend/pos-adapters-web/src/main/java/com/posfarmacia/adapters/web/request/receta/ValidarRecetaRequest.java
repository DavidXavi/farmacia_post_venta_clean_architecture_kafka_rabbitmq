package com.posfarmacia.adapters.web.request.receta;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

/**
 * Cuerpo de {@code POST /api/recetas/validar}.
 *
 * <p>{@code ventaId} es opcional: se envia cuando esta llamada confirma la dispensacion
 * dentro de una venta ya iniciada (registra el uso y, si aplica, retiene la receta);
 * se omite cuando es solo una evaluacion previa sin efectos.
 */
public record ValidarRecetaRequest(
        @NotNull UUID recetaId,
        @NotNull UUID productoId,
        @NotNull @Positive Integer cantidad,
        UUID ventaId) {
}
