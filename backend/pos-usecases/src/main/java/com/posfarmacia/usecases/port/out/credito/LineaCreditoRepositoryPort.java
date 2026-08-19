package com.posfarmacia.usecases.port.out.credito;

import com.posfarmacia.domain.model.credito.LineaCredito;
import java.util.Optional;
import java.util.UUID;

/** Puerto de salida hacia la persistencia de {@link LineaCredito}. */
public interface LineaCreditoRepositoryPort {

    LineaCredito guardar(LineaCredito lineaCredito);

    Optional<LineaCredito> buscarPorId(UUID id);

    Optional<LineaCredito> buscarPorCliente(UUID clienteId);
}
