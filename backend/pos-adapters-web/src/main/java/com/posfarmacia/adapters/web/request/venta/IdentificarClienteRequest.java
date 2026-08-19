package com.posfarmacia.adapters.web.request.venta;

import jakarta.validation.constraints.NotBlank;

/** RF09: identifica al cliente de la venta por su DNI. */
public record IdentificarClienteRequest(@NotBlank String dni) {
}
