package com.posfarmacia.usecases.usecase.identidad;

import com.posfarmacia.usecases.port.in.identidad.RegistrarAuditoriaUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.identidad.AuditoriaRepositoryPort;
import com.posfarmacia.domain.model.identidad.RegistroAuditoria;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/**
 * RF19: registra evidencia de una operacion sensible. No hace commit propio: queda dentro
 * de la misma transaccion que la operacion auditada (igual que AuditService en arquitectura_2_t2),
 * por eso el metodo tambien es @Transactional para poder participar de una transaccion existente.
 */
public class RegistrarAuditoriaUseCaseImpl implements RegistrarAuditoriaUseCase {

    private final AuditoriaRepositoryPort auditoria;
    private final ClockPort clock;

    public RegistrarAuditoriaUseCaseImpl(AuditoriaRepositoryPort auditoria, ClockPort clock) {
        this.auditoria = auditoria;
        this.clock = clock;
    }

    @Override
    @Transactional
    public RegistroAuditoria registrar(UUID usuarioId, String accion, String entidad, String entidadId,
                                        String detalle, String datosAnteriores, String datosNuevos) {
        RegistroAuditoria registro = new RegistroAuditoria(
                usuarioId, accion, entidad, entidadId, detalle, datosAnteriores, datosNuevos, clock.ahora());
        return auditoria.guardar(registro);
    }
}
