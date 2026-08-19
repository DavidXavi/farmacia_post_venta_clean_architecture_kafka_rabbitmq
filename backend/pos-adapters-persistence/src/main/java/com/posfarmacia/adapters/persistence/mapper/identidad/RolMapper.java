package com.posfarmacia.adapters.persistence.mapper.identidad;

import com.posfarmacia.adapters.persistence.entity.identidad.RolJpaEntity;
import com.posfarmacia.domain.enums.RolNombre;
import com.posfarmacia.domain.model.identidad.Rol;

public final class RolMapper {

    private RolMapper() {
    }

    public static Rol aDominio(RolJpaEntity entity) {
        return new Rol(entity.getId(), RolNombre.valueOf(entity.getNombre()), entity.getDescripcion());
    }

    public static RolJpaEntity aEntidad(Rol rol) {
        return new RolJpaEntity(rol.getId(), rol.getNombre().name(), rol.getDescripcion());
    }
}
