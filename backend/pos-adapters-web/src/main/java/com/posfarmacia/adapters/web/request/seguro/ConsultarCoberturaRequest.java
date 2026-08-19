package com.posfarmacia.adapters.web.request.seguro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record ConsultarCoberturaRequest(
        @NotBlank String dni,
        @NotNull UUID convenioId,
        @NotNull UUID productoId,
        @NotNull @Positive BigDecimal montoLinea) {
}
