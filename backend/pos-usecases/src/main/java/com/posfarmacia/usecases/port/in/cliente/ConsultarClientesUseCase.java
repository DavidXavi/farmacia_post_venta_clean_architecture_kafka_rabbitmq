package com.posfarmacia.usecases.port.in.cliente;

import com.posfarmacia.domain.model.cliente.Cliente;
import java.util.List;

/** Lista todos los clientes registrados (endpoint GET /api/clientes). */
public interface ConsultarClientesUseCase {

    List<Cliente> consultarTodos();
}
