package com.posfarmacia.usecases.port.out.identidad;

import com.posfarmacia.domain.model.identidad.SesionCaja;
import java.util.Optional;
import java.util.UUID;

public interface SesionCajaRepositoryPort {

    Optional<SesionCaja> buscarSesionActiva(UUID cajaId);

    SesionCaja guardar(SesionCaja sesionCaja);
}
