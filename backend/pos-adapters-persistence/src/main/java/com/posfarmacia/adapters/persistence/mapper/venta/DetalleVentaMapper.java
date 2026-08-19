package com.posfarmacia.adapters.persistence.mapper.venta;

import com.posfarmacia.adapters.persistence.entity.venta.DetalleVentaJpaEntity;
import com.posfarmacia.adapters.persistence.entity.venta.DetalleVentaLoteJpaEntity;
import com.posfarmacia.domain.model.venta.DetalleVenta;
import com.posfarmacia.domain.model.venta.DetalleVentaLote;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.Porcentaje;
import java.util.List;

public final class DetalleVentaMapper {

    private DetalleVentaMapper() {
    }

    public static DetalleVenta aDominio(DetalleVentaJpaEntity entity, List<DetalleVentaLoteJpaEntity> lotesEntidad) {
        List<DetalleVentaLote> lotes = lotesEntidad.stream().map(DetalleVentaLoteMapper::aDominio).toList();
        return DetalleVenta.reconstruir(
                entity.getId(),
                entity.getVentaId(),
                entity.getProductoId(),
                new Cantidad(entity.getCantidad()),
                new Dinero(entity.getPrecioUnitario()),
                new Porcentaje(entity.getTasaImpuesto()),
                entity.getRecetaId(),
                entity.getPromocionAplicadaId(),
                new Dinero(entity.getDescuentoMonto()),
                lotes);
    }

    public static DetalleVentaJpaEntity aEntidad(DetalleVenta detalle) {
        return new DetalleVentaJpaEntity(
                detalle.getId(),
                detalle.getVentaId(),
                detalle.getProductoId(),
                detalle.getCantidad().valor(),
                detalle.getPrecioUnitario().monto(),
                detalle.getTasaImpuesto().valor(),
                detalle.getPromocionAplicadaId(),
                detalle.getRecetaId(),
                detalle.getDescuentoMonto().monto());
    }
}
