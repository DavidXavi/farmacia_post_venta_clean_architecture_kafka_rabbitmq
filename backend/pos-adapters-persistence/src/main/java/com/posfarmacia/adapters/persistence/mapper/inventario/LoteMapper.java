package com.posfarmacia.adapters.persistence.mapper.inventario;

import com.posfarmacia.adapters.persistence.entity.inventario.LoteJpaEntity;
import com.posfarmacia.domain.enums.EstadoLote;
import com.posfarmacia.domain.model.inventario.Lote;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.CodigoLote;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.FechaVencimiento;

public final class LoteMapper {

    private LoteMapper() {
    }

    public static Lote aDominio(LoteJpaEntity entity) {
        return Lote.reconstruir(
                entity.getId(),
                new CodigoLote(entity.getCodigo()),
                entity.getProductoId(),
                new FechaVencimiento(entity.getFechaVencimiento()),
                new Cantidad(entity.getCantidadRecibida()),
                new Cantidad(entity.getCantidadDisponible()),
                entity.getCosto() == null ? null : new Dinero(entity.getCosto()),
                entity.getLocalId(),
                EstadoLote.valueOf(entity.getEstado()));
    }

    public static LoteJpaEntity aEntidad(Lote lote) {
        return new LoteJpaEntity(
                lote.getId(),
                lote.getCodigo().valor(),
                lote.getProductoId(),
                lote.getFechaVencimiento().valor(),
                lote.getCantidadRecibida().valor(),
                lote.getCantidadDisponible().valor(),
                lote.getCosto() == null ? null : lote.getCosto().monto(),
                lote.getLocalId(),
                lote.getEstado().name());
    }
}
