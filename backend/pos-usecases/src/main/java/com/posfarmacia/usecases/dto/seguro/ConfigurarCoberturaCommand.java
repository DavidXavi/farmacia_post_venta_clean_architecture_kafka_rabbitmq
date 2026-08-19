package com.posfarmacia.usecases.dto.seguro;

import java.math.BigDecimal;
import java.util.UUID;

public record ConfigurarCoberturaCommand(UUID productoId, BigDecimal porcentajeCubierto) {
}
