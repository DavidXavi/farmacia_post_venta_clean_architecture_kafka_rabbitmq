package com.posfarmacia.usecases.dto.venta;

import java.math.BigDecimal;

/** RF10: vista previa del copago al aplicar un convenio (RN04: se recalcula de nuevo al confirmar la venta). */
public record CopagoResult(BigDecimal montoCubierto, BigDecimal copago) {
}
