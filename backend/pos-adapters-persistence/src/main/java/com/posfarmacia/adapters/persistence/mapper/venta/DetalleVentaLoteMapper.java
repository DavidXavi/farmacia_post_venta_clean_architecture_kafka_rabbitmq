package com.posfarmacia.adapters.persistence.mapper.venta;

import com.posfarmacia.adapters.persistence.entity.venta.DetalleVentaLoteJpaEntity;
import com.posfarmacia.domain.model.venta.DetalleVentaLote;
import com.posfarmacia.domain.valueobject.Cantidad;

public final class DetalleVentaLoteMapper {

    private DetalleVentaLoteMapper() {
    }

    public static DetalleVentaLote aDominio(DetalleVentaLoteJpaEntity entity) {
        return new DetalleVentaLote(entity.getId(), entity.getDetalleVentaId(), entity.getLoteId(),
                new Cantidad(entity.getCantidadTomada()));
    }

    public static DetalleVentaLoteJpaEntity aEntidad(DetalleVentaLote lote) {
        return new DetalleVentaLoteJpaEntity(lote.getId(), lote.getDetalleVentaId(), lote.getLoteId(),
                lote.getCantidadTomada().valor());
    }
}
