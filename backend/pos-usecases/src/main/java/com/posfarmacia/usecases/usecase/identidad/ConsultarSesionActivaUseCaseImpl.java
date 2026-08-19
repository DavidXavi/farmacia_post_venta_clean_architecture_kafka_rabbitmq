package com.posfarmacia.usecases.usecase.identidad;

import com.posfarmacia.usecases.port.in.identidad.ConsultarSesionActivaUseCase;
import com.posfarmacia.usecases.port.out.identidad.SesionCajaRepositoryPort;
import com.posfarmacia.domain.model.identidad.SesionCaja;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/** RF02: consulta de la sesion de caja actualmente abierta (si existe) para una caja dada. */
public class ConsultarSesionActivaUseCaseImpl implements ConsultarSesionActivaUseCase {

    private final SesionCajaRepositoryPort sesiones;

    public ConsultarSesionActivaUseCaseImpl(SesionCajaRepositoryPort sesiones) {
        this.sesiones = sesiones;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SesionCaja> consultar(UUID cajaId) {
        return sesiones.buscarSesionActiva(cajaId);
    }
}
