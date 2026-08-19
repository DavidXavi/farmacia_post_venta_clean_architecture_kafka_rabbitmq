package com.posfarmacia.adapters.persistence.mapper.venta;

import com.posfarmacia.adapters.persistence.entity.venta.PagoJpaEntity;
import com.posfarmacia.domain.model.venta.Pago;
import com.posfarmacia.domain.valueobject.Dinero;

public final class PagoMapper {

    private PagoMapper() {
    }

    public static Pago aDominio(PagoJpaEntity entity) {
        return new Pago(entity.getId(), entity.getVentaId(), entity.getFormaPagoId(), new Dinero(entity.getMonto()),
                entity.getCodigoAutorizacion(), entity.getFecha());
    }

    public static PagoJpaEntity aEntidad(Pago pago) {
        return new PagoJpaEntity(pago.getId(), pago.getVentaId(), pago.getFormaPagoId(), pago.getMonto().monto(),
                pago.getCodigoAutorizacion(), pago.getFecha());
    }
}
