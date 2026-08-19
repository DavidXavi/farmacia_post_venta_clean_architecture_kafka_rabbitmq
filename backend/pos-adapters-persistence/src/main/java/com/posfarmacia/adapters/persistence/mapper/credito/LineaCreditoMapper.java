package com.posfarmacia.adapters.persistence.mapper.credito;

import com.posfarmacia.adapters.persistence.entity.credito.LineaCreditoJpaEntity;
import com.posfarmacia.domain.enums.EstadoLineaCredito;
import com.posfarmacia.domain.model.credito.LineaCredito;
import com.posfarmacia.domain.valueobject.Dinero;
import org.springframework.stereotype.Component;

@Component
public class LineaCreditoMapper {

    public LineaCredito toDomain(LineaCreditoJpaEntity entity) {
        return new LineaCredito(entity.getId(), entity.getClienteId(), new Dinero(entity.getMontoAutorizado()),
                new Dinero(entity.getSaldoDisponible()), entity.getVigenciaInicio(), entity.getVigenciaFin(),
                EstadoLineaCredito.valueOf(entity.getEstado()));
    }

    public LineaCreditoJpaEntity toEntity(LineaCredito lineaCredito) {
        return new LineaCreditoJpaEntity(lineaCredito.getId(), lineaCredito.getClienteId(),
                lineaCredito.getMontoAutorizado().monto(), lineaCredito.getSaldoDisponible().monto(),
                lineaCredito.getVigencia().inicio(), lineaCredito.getVigencia().fin(), lineaCredito.getEstado().name());
    }
}
