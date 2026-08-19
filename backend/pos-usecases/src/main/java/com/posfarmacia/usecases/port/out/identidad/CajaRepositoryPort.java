package com.posfarmacia.usecases.port.out.identidad;

import com.posfarmacia.domain.model.identidad.Caja;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CajaRepositoryPort {

    Optional<Caja> buscarPorId(UUID id);

    /** RF02: listado de cajas registradas, usado por el frontend para elegir la caja a operar. */
    List<Caja> listarTodas();
}
