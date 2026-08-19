package com.posfarmacia.adapters.persistence.mapper.identidad;

import com.posfarmacia.adapters.persistence.entity.identidad.RegistroAuditoriaJpaEntity;
import com.posfarmacia.domain.model.identidad.RegistroAuditoria;

public final class AuditoriaMapper {

    private AuditoriaMapper() {
    }

    public static RegistroAuditoria aDominio(RegistroAuditoriaJpaEntity entity) {
        return new RegistroAuditoria(
                entity.getId(),
                entity.getFecha(),
                entity.getUsuarioId(),
                entity.getAccion(),
                entity.getEntidad(),
                entity.getEntidadId(),
                entity.getDetalle(),
                entity.getDatosAnteriores(),
                entity.getDatosNuevos());
    }

    public static RegistroAuditoriaJpaEntity aEntidad(RegistroAuditoria registro) {
        return new RegistroAuditoriaJpaEntity(
                registro.getId(),
                registro.getFecha(),
                registro.getUsuarioId(),
                registro.getAccion(),
                registro.getEntidad(),
                registro.getEntidadId(),
                registro.getDetalle(),
                registro.getDatosAnteriores(),
                registro.getDatosNuevos());
    }
}
