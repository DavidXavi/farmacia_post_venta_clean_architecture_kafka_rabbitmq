package com.posfarmacia.adapters.web.response.catalogo;

import com.posfarmacia.usecases.dto.catalogo.LaboratorioResult;
import java.util.UUID;

public record LaboratorioResponse(UUID id, String nombre) {

    public static LaboratorioResponse desde(LaboratorioResult result) {
        return new LaboratorioResponse(result.id(), result.nombre());
    }
}
