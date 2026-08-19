package com.posfarmacia.adapters.web.request.identidad;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/** El monto esperado se calcula en el servidor a partir de las ventas confirmadas del turno (ver {@code CerrarCajaUseCase}). */
public record CierreCajaRequest(
        @NotNull(message = "El monto declarado es obligatorio")
        @DecimalMin(value = "0.0", message = "El monto declarado no puede ser negativo") BigDecimal montoDeclarado,
        String observacion) {
}
