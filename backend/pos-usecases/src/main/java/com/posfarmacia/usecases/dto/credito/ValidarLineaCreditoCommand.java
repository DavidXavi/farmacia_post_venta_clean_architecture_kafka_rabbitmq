package com.posfarmacia.usecases.dto.credito;

import java.math.BigDecimal;

/** RN28: el cliente debe identificarse con su DNI para una compra a credito. */
public record ValidarLineaCreditoCommand(String dni, BigDecimal monto) {
}
