package com.posfarmacia.adapters.persistence.mapper.catalogo;

import com.posfarmacia.adapters.persistence.entity.catalogo.PresentacionJpaEntity;
import com.posfarmacia.domain.model.catalogo.Presentacion;

public final class PresentacionMapper {

    private PresentacionMapper() {
    }

    public static Presentacion aDominio(PresentacionJpaEntity entity) {
        return Presentacion.reconstruir(entity.getId(), entity.getNombre(), entity.getUnidadMedida());
    }

    public static PresentacionJpaEntity aEntidad(Presentacion presentacion) {
        return new PresentacionJpaEntity(presentacion.getId(), presentacion.getNombre(), presentacion.getUnidadMedida());
    }
}
