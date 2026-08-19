package com.posfarmacia.adapters.persistence.mapper.identidad;

import com.posfarmacia.adapters.persistence.entity.identidad.LocalJpaEntity;
import com.posfarmacia.domain.model.identidad.Local;

public final class LocalMapper {

    private LocalMapper() {
    }

    public static Local aDominio(LocalJpaEntity entity) {
        return new Local(entity.getId(), entity.getNombre(), entity.getDireccion(), entity.isActivo());
    }

    public static LocalJpaEntity aEntidad(Local local) {
        return new LocalJpaEntity(local.getId(), local.getNombre(), local.getDireccion(), local.isActivo());
    }
}
