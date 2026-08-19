package com.posfarmacia.adapters.web.request.venta;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

/** RF12: registra un pago sobre la venta. */
public record RegistrarPagoRequest(
        @NotNull UUID formaPagoId,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal monto,
        String codigoAutorizacion) {
}
