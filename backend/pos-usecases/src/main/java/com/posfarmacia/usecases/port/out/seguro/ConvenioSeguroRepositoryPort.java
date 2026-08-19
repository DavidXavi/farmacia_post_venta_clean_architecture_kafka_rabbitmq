package com.posfarmacia.usecases.port.out.seguro;

import com.posfarmacia.domain.model.seguro.ConvenioSeguro;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Puerto de salida hacia la persistencia de {@link ConvenioSeguro} y sus coberturas por producto. */
public interface ConvenioSeguroRepositoryPort {

    ConvenioSeguro guardar(ConvenioSeguro convenio);

    Optional<ConvenioSeguro> buscarPorId(UUID id);

    List<ConvenioSeguro> buscarTodos();
}
