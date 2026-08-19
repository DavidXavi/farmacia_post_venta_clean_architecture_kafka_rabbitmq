package com.posfarmacia.adapters.web.response.identidad;

import com.posfarmacia.domain.model.identidad.Local;
import java.util.UUID;

public record LocalResponse(UUID id, String nombre, String direccion, boolean activo) {

    public static LocalResponse desde(Local local) {
        return new LocalResponse(local.getId(), local.getNombre(), local.getDireccion(), local.isActivo());
    }
}
