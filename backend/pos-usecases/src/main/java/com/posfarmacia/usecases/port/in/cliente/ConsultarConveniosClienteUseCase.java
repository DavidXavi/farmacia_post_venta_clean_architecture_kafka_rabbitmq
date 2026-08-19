package com.posfarmacia.usecases.port.in.cliente;

import com.posfarmacia.domain.model.seguro.AfiliacionCliente;
import java.util.List;
import java.util.UUID;

/** Consulta las afiliaciones a convenios de seguro de un cliente (endpoint GET /clientes/{id}/convenios). */
public interface ConsultarConveniosClienteUseCase {

    List<AfiliacionCliente> consultarPorCliente(UUID clienteId);
}
