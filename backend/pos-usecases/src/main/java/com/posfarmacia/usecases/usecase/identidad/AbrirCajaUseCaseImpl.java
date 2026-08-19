package com.posfarmacia.usecases.usecase.identidad;

import com.posfarmacia.usecases.port.in.identidad.AbrirCajaUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.identidad.CajaRepositoryPort;
import com.posfarmacia.usecases.port.out.identidad.SesionCajaRepositoryPort;
import com.posfarmacia.domain.exception.CajaCerradaException;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.identidad.SesionCaja;
import com.posfarmacia.domain.valueobject.Dinero;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/** RF02: apertura de turno de caja. RN01: no se abre una segunda sesion sobre la misma caja. */
public class AbrirCajaUseCaseImpl implements AbrirCajaUseCase {

    private final CajaRepositoryPort cajas;
    private final SesionCajaRepositoryPort sesiones;
    private final ClockPort clock;

    public AbrirCajaUseCaseImpl(CajaRepositoryPort cajas, SesionCajaRepositoryPort sesiones, ClockPort clock) {
        this.cajas = cajas;
        this.sesiones = sesiones;
        this.clock = clock;
    }

    @Override
    @Transactional
    public SesionCaja abrir(UUID cajaId, UUID usuarioId, Dinero montoInicial) {
        cajas.buscarPorId(cajaId).orElseThrow(() -> new EntidadNoEncontradaException("La caja indicada no existe."));

        sesiones.buscarSesionActiva(cajaId).ifPresent(s -> {
            throw new CajaCerradaException("La caja ya tiene una sesion abierta.");
        });

        SesionCaja sesion = new SesionCaja(cajaId, usuarioId, montoInicial, clock.ahora());
        return sesiones.guardar(sesion);
    }
}
