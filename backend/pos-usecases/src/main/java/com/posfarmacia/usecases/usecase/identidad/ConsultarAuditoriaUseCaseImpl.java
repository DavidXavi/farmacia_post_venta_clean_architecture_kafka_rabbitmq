package com.posfarmacia.usecases.usecase.identidad;

import com.posfarmacia.usecases.port.in.identidad.ConsultarAuditoriaUseCase;
import com.posfarmacia.usecases.port.out.identidad.AuditoriaRepositoryPort;
import com.posfarmacia.domain.model.identidad.RegistroAuditoria;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/** RF19: consulta de auditoria de operaciones para el administrador, con filtros opcionales. */
public class ConsultarAuditoriaUseCaseImpl implements ConsultarAuditoriaUseCase {

    private final AuditoriaRepositoryPort auditoria;

    public ConsultarAuditoriaUseCaseImpl(AuditoriaRepositoryPort auditoria) {
        this.auditoria = auditoria;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistroAuditoria> consultar(LocalDate fecha, String entidad, UUID usuarioId) {
        return auditoria.buscar(fecha, entidad, usuarioId);
    }
}
