package com.posfarmacia.adapters.web.request.seguro;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record RegistrarAfiliacionRequest(@NotNull UUID clienteId, @NotNull UUID convenioId,
                                          LocalDate vigenciaInicio, LocalDate vigenciaFin) {
}
