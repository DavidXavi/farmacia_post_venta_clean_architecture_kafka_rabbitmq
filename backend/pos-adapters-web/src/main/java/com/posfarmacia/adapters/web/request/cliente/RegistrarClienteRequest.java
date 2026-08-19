package com.posfarmacia.adapters.web.request.cliente;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record RegistrarClienteRequest(
        @NotBlank String dni,
        @NotBlank String nombres,
        @NotBlank String apellidos,
        LocalDate fechaNacimiento,
        String telefono,
        String correo,
        String direccion) {
}
