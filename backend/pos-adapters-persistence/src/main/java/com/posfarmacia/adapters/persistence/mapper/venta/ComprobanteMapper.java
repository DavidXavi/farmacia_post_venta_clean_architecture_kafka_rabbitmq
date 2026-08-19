package com.posfarmacia.adapters.persistence.mapper.venta;

import com.posfarmacia.adapters.persistence.entity.venta.ComprobanteJpaEntity;
import com.posfarmacia.domain.enums.TipoComprobante;
import com.posfarmacia.domain.model.venta.Comprobante;
import com.posfarmacia.domain.valueobject.NumeroComprobante;

public final class ComprobanteMapper {

    private ComprobanteMapper() {
    }

    public static Comprobante aDominio(ComprobanteJpaEntity entity) {
        return new Comprobante(entity.getId(), entity.getVentaId(), TipoComprobante.valueOf(entity.getTipo()),
                new NumeroComprobante(entity.getSerie(), entity.getCorrelativo()), entity.getFechaEmision());
    }

    public static ComprobanteJpaEntity aEntidad(Comprobante comprobante) {
        return new ComprobanteJpaEntity(comprobante.getId(), comprobante.getVentaId(), comprobante.getTipo().name(),
                comprobante.getNumero().serie(), comprobante.getNumero().correlativo(), comprobante.getFechaEmision());
    }
}
