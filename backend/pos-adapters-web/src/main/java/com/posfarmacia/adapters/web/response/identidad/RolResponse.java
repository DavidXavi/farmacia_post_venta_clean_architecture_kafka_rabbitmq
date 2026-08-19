package com.posfarmacia.adapters.web.response.identidad;

import com.posfarmacia.domain.model.identidad.Rol;
import java.util.UUID;

public record RolResponse(UUID id, String nombre, String descripcion) {

    public static RolResponse desde(Rol rol) {
        return new RolResponse(rol.getId(), rol.getNombre().name(), rol.getDescripcion());
    }
}
