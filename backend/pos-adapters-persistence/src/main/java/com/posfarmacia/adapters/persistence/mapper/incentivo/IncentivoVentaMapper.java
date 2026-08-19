package com.posfarmacia.adapters.persistence.mapper.incentivo;

import com.posfarmacia.adapters.persistence.entity.incentivo.IncentivoVentaJpaEntity;
import com.posfarmacia.domain.model.incentivo.IncentivoVenta;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;
import org.springframework.stereotype.Component;

/** Traduce entre la entidad JPA de incentivos de venta y el modelo de dominio {@code IncentivoVenta}. */
@Component
public class IncentivoVentaMapper {

    public IncentivoVenta toDomain(IncentivoVentaJpaEntity entity) {
        return IncentivoVenta.reconstruir(
                entity.getId(),
                entity.getReglaIncentivoId(),
                entity.getUsuarioId(),
                entity.getVentaId(),
                entity.getDetalleVentaId(),
                new Cantidad(entity.getCantidad()),
                new Dinero(entity.getMontoCalculado()),
                entity.getFecha());
    }

    public IncentivoVentaJpaEntity toEntity(IncentivoVenta incentivo) {
        return new IncentivoVentaJpaEntity(
                incentivo.getId(),
                incentivo.getReglaIncentivoId(),
                incentivo.getUsuarioId(),
                incentivo.getVentaId(),
                incentivo.getDetalleVentaId(),
                incentivo.getCantidad().valor(),
                incentivo.getMontoCalculado().monto(),
                incentivo.getFecha());
    }
}
