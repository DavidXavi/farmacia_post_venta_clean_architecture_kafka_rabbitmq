package com.posfarmacia.adapters.web.response.seguro;

import com.posfarmacia.domain.model.seguro.ConvenioSeguro;
import java.util.UUID;

public record ConvenioResponse(UUID id, String nombre, boolean activo) {

    public static ConvenioResponse de(ConvenioSeguro convenio) {
        return new ConvenioResponse(convenio.getId(), convenio.getNombre(), convenio.isActivo());
    }
}
