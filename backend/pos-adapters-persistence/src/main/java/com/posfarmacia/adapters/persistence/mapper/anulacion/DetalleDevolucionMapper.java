package com.posfarmacia.adapters.persistence.mapper.anulacion;

import com.posfarmacia.adapters.persistence.entity.anulacion.DetalleDevolucionJpaEntity;
import com.posfarmacia.domain.model.anulacion.DetalleDevolucion;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;

public final class DetalleDevolucionMapper {

    private DetalleDevolucionMapper() {
    }

    public static DetalleDevolucion aDominio(DetalleDevolucionJpaEntity entity) {
        return DetalleDevolucion.reconstruir(entity.getId(), entity.getDevolucionId(), entity.getDetalleVentaId(),
                entity.getProductoId(), new Cantidad(entity.getCantidad()), new Dinero(entity.getMontoDevuelto()));
    }

    public static DetalleDevolucionJpaEntity aEntidad(DetalleDevolucion detalle) {
        return new DetalleDevolucionJpaEntity(detalle.getId(), detalle.getDevolucionId(), detalle.getDetalleVentaId(),
                detalle.getProductoId(), detalle.getCantidad().valor(), detalle.getMontoDevuelto().monto());
    }
}
