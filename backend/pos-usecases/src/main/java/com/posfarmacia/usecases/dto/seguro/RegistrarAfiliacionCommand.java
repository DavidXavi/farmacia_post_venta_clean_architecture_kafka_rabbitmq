package com.posfarmacia.usecases.dto.seguro;

import java.time.LocalDate;
import java.util.UUID;

public record RegistrarAfiliacionCommand(UUID clienteId, UUID convenioId, LocalDate vigenciaInicio, LocalDate vigenciaFin) {
}
