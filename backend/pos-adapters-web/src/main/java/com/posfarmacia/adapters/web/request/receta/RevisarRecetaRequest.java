package com.posfarmacia.adapters.web.request.receta;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/** Cuerpo de {@code POST /api/recetas/validaciones}: revision clinica de una receta. */
public record RevisarRecetaRequest(
        @NotNull UUID recetaId,
        @NotNull UUID usuarioValidadorId,
        @NotNull Boolean aprobar,
        String observaciones) {
}
