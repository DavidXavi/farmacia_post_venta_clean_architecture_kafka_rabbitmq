package com.posfarmacia.adapters.persistence.mapper.inventario;

import com.posfarmacia.adapters.persistence.entity.inventario.MovimientoInventarioJpaEntity;
import com.posfarmacia.domain.enums.TipoMovimientoStock;
import com.posfarmacia.domain.model.inventario.MovimientoInventario;
import com.posfarmacia.domain.valueobject.Cantidad;

public final class MovimientoInventarioMapper {

    private MovimientoInventarioMapper() {
    }

    public static MovimientoInventario aDominio(MovimientoInventarioJpaEntity entity) {
        return MovimientoInventario.reconstruir(
                entity.getId(),
                entity.getLoteId(),
                TipoMovimientoStock.valueOf(entity.getTipo()),
                new Cantidad(entity.getCantidad()),
                entity.getUsuarioId(),
                entity.getReferencia(),
                entity.getFecha());
    }

    public static MovimientoInventarioJpaEntity aEntidad(MovimientoInventario movimiento) {
        return new MovimientoInventarioJpaEntity(
                movimiento.getId(),
                movimiento.getLoteId(),
                movimiento.getTipo().name(),
                movimiento.getCantidad().valor(),
                movimiento.getUsuarioId(),
                movimiento.getReferencia(),
                movimiento.getFecha());
    }
}
