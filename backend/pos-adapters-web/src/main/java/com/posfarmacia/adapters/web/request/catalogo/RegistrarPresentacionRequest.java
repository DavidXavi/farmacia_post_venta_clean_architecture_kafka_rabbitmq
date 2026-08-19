package com.posfarmacia.adapters.web.request.catalogo;

import jakarta.validation.constraints.NotBlank;

/** RF03: alta de una presentacion del catalogo (nombre + unidad de medida). */
public record RegistrarPresentacionRequest(@NotBlank String nombre, @NotBlank String unidadMedida) {
}
