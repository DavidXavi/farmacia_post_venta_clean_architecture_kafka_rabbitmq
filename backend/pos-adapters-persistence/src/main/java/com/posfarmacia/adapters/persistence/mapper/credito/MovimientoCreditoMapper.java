package com.posfarmacia.adapters.persistence.mapper.credito;

import com.posfarmacia.adapters.persistence.entity.credito.MovimientoCreditoJpaEntity;
import com.posfarmacia.domain.enums.TipoMovimientoCredito;
import com.posfarmacia.domain.model.credito.MovimientoCredito;
import com.posfarmacia.domain.valueobject.Dinero;
import org.springframework.stereotype.Component;

@Component
public class MovimientoCreditoMapper {

    public MovimientoCredito toDomain(MovimientoCreditoJpaEntity entity) {
        return new MovimientoCredito(entity.getId(), entity.getLineaCreditoId(), entity.getVentaId(),
                TipoMovimientoCredito.valueOf(entity.getTipo()), new Dinero(entity.getMonto()), entity.getFecha());
    }

    public MovimientoCreditoJpaEntity toEntity(MovimientoCredito movimiento) {
        return new MovimientoCreditoJpaEntity(movimiento.getId(), movimiento.getLineaCreditoId(), movimiento.getVentaId(),
                movimiento.getTipo().name(), movimiento.getMonto().monto(), movimiento.getFecha());
    }
}
