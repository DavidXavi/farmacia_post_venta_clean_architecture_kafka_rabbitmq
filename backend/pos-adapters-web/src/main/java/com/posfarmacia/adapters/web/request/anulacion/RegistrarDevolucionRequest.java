package com.posfarmacia.adapters.web.request.anulacion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/** RF16/RN41: rutas y forma exactas que ya consume `frontend/src/pages/DevolucionesPage.jsx`. */
public record RegistrarDevolucionRequest(
        @NotNull UUID ventaId,
        @NotNull UUID usuarioId,
        @NotBlank String motivo,
        @NotEmpty List<@Valid LineaDevolucionRequest> lineas) {
}
