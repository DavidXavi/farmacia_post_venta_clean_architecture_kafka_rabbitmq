package com.posfarmacia.usecases.dto.seguro;

import java.math.BigDecimal;
import java.util.UUID;

public record ConsultarCoberturaResult(
        UUID clienteId,
        UUID convenioId,
        UUID productoId,
        BigDecimal montoCubierto,
        BigDecimal copago,
        String codigoAutorizacion) {
}
