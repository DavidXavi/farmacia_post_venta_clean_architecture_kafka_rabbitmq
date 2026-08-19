package com.posfarmacia.adapters.persistence.mapper.seguro;

import com.posfarmacia.adapters.persistence.entity.seguro.AfiliacionClienteJpaEntity;
import com.posfarmacia.domain.enums.EstadoAfiliacion;
import com.posfarmacia.domain.model.seguro.AfiliacionCliente;
import org.springframework.stereotype.Component;

@Component
public class AfiliacionClienteMapper {

    public AfiliacionCliente toDomain(AfiliacionClienteJpaEntity entity) {
        return new AfiliacionCliente(entity.getId(), entity.getClienteId(), entity.getConvenioId(),
                entity.getVigenciaInicio(), entity.getVigenciaFin(), EstadoAfiliacion.valueOf(entity.getEstado()));
    }

    public AfiliacionClienteJpaEntity toEntity(AfiliacionCliente afiliacion) {
        return new AfiliacionClienteJpaEntity(afiliacion.getId(), afiliacion.getClienteId(), afiliacion.getConvenioId(),
                afiliacion.getVigencia().inicio(), afiliacion.getVigencia().fin(), afiliacion.getEstado().name());
    }
}
