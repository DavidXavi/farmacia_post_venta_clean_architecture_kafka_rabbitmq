package com.posfarmacia.adapters.web.request.seguro;

import jakarta.validation.constraints.NotBlank;

public record RegistrarConvenioRequest(@NotBlank String nombre) {
}
