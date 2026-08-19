package com.posfarmacia.usecases.port.in.identidad;

import com.posfarmacia.domain.model.identidad.SesionCaja;
import java.util.Optional;
import java.util.UUID;

/** RF02: consulta la sesion de caja actualmente abierta (si existe) para una caja dada. */
public interface ConsultarSesionActivaUseCase {

    Optional<SesionCaja> consultar(UUID cajaId);
}
