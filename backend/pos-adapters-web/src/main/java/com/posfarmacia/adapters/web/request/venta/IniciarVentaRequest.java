package com.posfarmacia.adapters.web.request.venta;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/** RF05/RN01: inicia una venta. {@code clienteDni} es opcional. */
public record IniciarVentaRequest(
        @NotNull UUID cajaId,
        @NotNull UUID sesionCajaId,
        @NotNull UUID usuarioId,
        String clienteDni) {
}
