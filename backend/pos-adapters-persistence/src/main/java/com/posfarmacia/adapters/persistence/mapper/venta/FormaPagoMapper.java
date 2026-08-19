package com.posfarmacia.adapters.persistence.mapper.venta;

import com.posfarmacia.adapters.persistence.entity.venta.FormaPagoJpaEntity;
import com.posfarmacia.domain.enums.TipoFormaPago;
import com.posfarmacia.domain.model.venta.FormaPago;

public final class FormaPagoMapper {

    private FormaPagoMapper() {
    }

    public static FormaPago aDominio(FormaPagoJpaEntity entity) {
        return new FormaPago(entity.getId(), entity.getNombre(), TipoFormaPago.valueOf(entity.getTipo()),
                entity.isActivo());
    }

    public static FormaPagoJpaEntity aEntidad(FormaPago formaPago) {
        return new FormaPagoJpaEntity(formaPago.getId(), formaPago.getNombre(), formaPago.getTipo().name(),
                formaPago.isActivo());
    }
}
