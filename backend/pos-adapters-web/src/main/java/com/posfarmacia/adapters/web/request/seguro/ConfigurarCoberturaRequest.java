package com.posfarmacia.adapters.web.request.seguro;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record ConfigurarCoberturaRequest(@NotNull UUID productoId, @NotNull BigDecimal porcentajeCubierto) {
}
