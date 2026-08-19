package com.posfarmacia.adapters.web.request.credito;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RegistrarLineaCreditoRequest(@NotNull UUID clienteId, @NotNull BigDecimal montoAutorizado,
                                            LocalDate vigenciaInicio, LocalDate vigenciaFin) {
}
