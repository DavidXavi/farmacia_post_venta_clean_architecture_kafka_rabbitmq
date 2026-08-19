package com.posfarmacia.usecases.port.out.seguro;

import com.posfarmacia.domain.model.seguro.AfiliacionCliente;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Puerto de salida hacia la persistencia de {@link AfiliacionCliente}. */
public interface AfiliacionClienteRepositoryPort {

    AfiliacionCliente guardar(AfiliacionCliente afiliacion);

    Optional<AfiliacionCliente> buscarPorId(UUID id);

    List<AfiliacionCliente> buscarPorCliente(UUID clienteId);

    Optional<AfiliacionCliente> buscarPorClienteYConvenio(UUID clienteId, UUID convenioId);
}
