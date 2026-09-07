package com.posfarmacia.adapters.web.request.identidad;

import jakarta.validation.constraints.NotBlank;

public record VerificarMfaRequest(
        @NotBlank(message = "El token de verificacion es obligatorio") String mfaToken,
        @NotBlank(message = "El codigo es obligatorio") String codigo) {
}
