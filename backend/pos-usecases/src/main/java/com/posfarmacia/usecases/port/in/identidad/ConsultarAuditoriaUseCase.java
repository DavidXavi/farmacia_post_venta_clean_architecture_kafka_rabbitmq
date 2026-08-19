package com.posfarmacia.usecases.port.in.identidad;

import com.posfarmacia.domain.model.identidad.RegistroAuditoria;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/** RF19: permite al administrador revisar la auditoria de operaciones, filtrando opcionalmente. */
public interface ConsultarAuditoriaUseCase {

    List<RegistroAuditoria> consultar(LocalDate fecha, String entidad, UUID usuarioId);
}
