package com.posfarmacia.adapters.persistence.mapper.incentivo;

import com.posfarmacia.adapters.persistence.entity.incentivo.ReglaIncentivoJpaEntity;
import com.posfarmacia.domain.model.incentivo.ReglaIncentivo;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.PeriodoVigencia;
import org.springframework.stereotype.Component;

/** Traduce entre la entidad JPA de reglas de incentivo y el modelo de dominio {@code ReglaIncentivo}. */
@Component
public class ReglaIncentivoMapper {

    public ReglaIncentivo toDomain(ReglaIncentivoJpaEntity entity) {
        return ReglaIncentivo.reconstruir(
                entity.getId(),
                entity.getNombre(),
                entity.getProductoId(),
                entity.getCategoriaId(),
                new Dinero(entity.getMontoPorUnidad()),
                new PeriodoVigencia(entity.getVigenciaInicio(), entity.getVigenciaFin()),
                entity.isActiva());
    }

    public ReglaIncentivoJpaEntity toEntity(ReglaIncentivo regla) {
        return new ReglaIncentivoJpaEntity(
                regla.getId(),
                regla.getNombre(),
                regla.getProductoId(),
                regla.getCategoriaId(),
                regla.getMontoPorUnidad().monto(),
                regla.getVigencia().inicio(),
                regla.getVigencia().fin(),
                regla.isActiva());
    }
}
