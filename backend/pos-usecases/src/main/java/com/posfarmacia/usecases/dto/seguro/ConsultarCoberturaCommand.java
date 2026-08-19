package com.posfarmacia.usecases.dto.seguro;

import java.math.BigDecimal;
import java.util.UUID;

/** RN22: el cliente debe identificarse con su DNI para usar un convenio de seguro. */
public record ConsultarCoberturaCommand(String dni, UUID convenioId, UUID productoId, BigDecimal montoLinea) {
}
