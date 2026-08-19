package com.posfarmacia.adapters.web.request.identidad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

public record RegistrarUsuarioRequest(
        @NotBlank(message = "El nombre de usuario es obligatorio") String nombreUsuario,
        @NotBlank(message = "La contrasena es obligatoria") String password,
        @NotNull(message = "El local es obligatorio") UUID localId,
        @NotEmpty(message = "Debe indicar al menos un rol") Set<String> roles) {
}
