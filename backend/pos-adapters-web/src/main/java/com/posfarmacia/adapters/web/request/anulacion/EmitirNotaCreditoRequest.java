package com.posfarmacia.adapters.web.request.anulacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record EmitirNotaCreditoRequest(@NotNull UUID ventaId, @NotNull UUID usuarioId, @NotBlank String motivo) {
}
