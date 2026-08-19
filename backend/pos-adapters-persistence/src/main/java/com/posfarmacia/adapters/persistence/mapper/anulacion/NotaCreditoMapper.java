package com.posfarmacia.adapters.persistence.mapper.anulacion;

import com.posfarmacia.adapters.persistence.entity.anulacion.NotaCreditoJpaEntity;
import com.posfarmacia.domain.model.anulacion.NotaCredito;
import com.posfarmacia.domain.valueobject.Dinero;

public final class NotaCreditoMapper {

    private NotaCreditoMapper() {
    }

    public static NotaCredito aDominio(NotaCreditoJpaEntity entity) {
        return NotaCredito.reconstruir(entity.getId(), entity.getVentaId(), entity.getComprobanteId(),
                entity.getUsuarioId(), entity.getMotivo(), new Dinero(entity.getMontoTotal()), entity.getFecha());
    }

    public static NotaCreditoJpaEntity aEntidad(NotaCredito notaCredito) {
        return new NotaCreditoJpaEntity(notaCredito.getId(), notaCredito.getVentaId(), notaCredito.getComprobanteId(),
                notaCredito.getUsuarioId(), notaCredito.getMotivo(), notaCredito.getMontoTotal().monto(),
                notaCredito.getFecha());
    }
}
