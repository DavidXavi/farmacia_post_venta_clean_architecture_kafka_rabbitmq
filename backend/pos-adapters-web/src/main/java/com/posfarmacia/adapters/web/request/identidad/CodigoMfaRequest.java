package com.posfarmacia.adapters.web.request.identidad;

import jakarta.validation.constraints.NotBlank;

public record CodigoMfaRequest(@NotBlank(message = "El codigo es obligatorio") String codigo) {
}
