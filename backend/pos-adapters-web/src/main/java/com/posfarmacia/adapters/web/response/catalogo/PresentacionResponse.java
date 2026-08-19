package com.posfarmacia.adapters.web.response.catalogo;

import com.posfarmacia.usecases.dto.catalogo.PresentacionResult;
import java.util.UUID;

public record PresentacionResponse(UUID id, String nombre, String unidadMedida) {

    public static PresentacionResponse desde(PresentacionResult result) {
        return new PresentacionResponse(result.id(), result.nombre(), result.unidadMedida());
    }
}
