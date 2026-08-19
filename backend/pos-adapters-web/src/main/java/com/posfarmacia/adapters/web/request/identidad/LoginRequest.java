package com.posfarmacia.adapters.web.request.identidad;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El nombre de usuario es obligatorio") String nombreUsuario,
        @NotBlank(message = "La contrasena es obligatoria") String password) {
}
