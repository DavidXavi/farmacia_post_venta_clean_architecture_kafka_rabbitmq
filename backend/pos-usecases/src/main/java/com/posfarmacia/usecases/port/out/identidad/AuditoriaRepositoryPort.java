package com.posfarmacia.usecases.port.out.identidad;

import com.posfarmacia.domain.model.identidad.RegistroAuditoria;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AuditoriaRepositoryPort {

    RegistroAuditoria guardar(RegistroAuditoria registro);

    List<RegistroAuditoria> buscar(LocalDate fecha, String entidad, UUID usuarioId);
}
