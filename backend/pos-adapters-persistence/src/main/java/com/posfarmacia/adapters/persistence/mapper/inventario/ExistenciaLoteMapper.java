package com.posfarmacia.adapters.persistence.mapper.inventario;

import com.posfarmacia.adapters.persistence.entity.inventario.ExistenciaLoteJpaEntity;
import com.posfarmacia.domain.model.inventario.ExistenciaLote;
import com.posfarmacia.domain.valueobject.Cantidad;

public final class ExistenciaLoteMapper {

    private ExistenciaLoteMapper() {
    }

    public static ExistenciaLote aDominio(ExistenciaLoteJpaEntity entity) {
        return ExistenciaLote.reconstruir(
                entity.getId(),
                entity.getProductoId(),
                entity.getLocalId(),
                new Cantidad(entity.getCantidadActual()),
                entity.getActualizadoEn());
    }

    public static ExistenciaLoteJpaEntity aEntidad(ExistenciaLote existencia) {
        return new ExistenciaLoteJpaEntity(
                existencia.getId(),
                existencia.getProductoId(),
                existencia.getLocalId(),
                existencia.getCantidadActual().valor(),
                existencia.getActualizadoEn());
    }
}
