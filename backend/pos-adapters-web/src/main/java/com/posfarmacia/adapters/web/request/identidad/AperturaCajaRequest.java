package com.posfarmacia.adapters.web.request.identidad;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record AperturaCajaRequest(
        @NotNull(message = "El usuario responsable es obligatorio") UUID usuarioId,
        @NotNull(message = "El monto inicial es obligatorio")
        @DecimalMin(value = "0.0", message = "El monto inicial no puede ser negativo") BigDecimal montoInicial) {
}
