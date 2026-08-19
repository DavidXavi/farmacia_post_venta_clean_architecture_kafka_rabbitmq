package com.posfarmacia.adapters.web.response.identidad;

import com.posfarmacia.domain.model.identidad.RegistroAuditoria;
import java.time.Instant;
import java.util.UUID;

public record AuditoriaResponse(
        UUID id,
        Instant fecha,
        UUID usuarioId,
        String accion,
        String entidad,
        String entidadId,
        String detalle) {

    public static AuditoriaResponse desde(RegistroAuditoria registro) {
        return new AuditoriaResponse(
                registro.getId(),
                registro.getFecha(),
                registro.getUsuarioId(),
                registro.getAccion(),
                registro.getEntidad(),
                registro.getEntidadId(),
                registro.getDetalle());
    }
}
