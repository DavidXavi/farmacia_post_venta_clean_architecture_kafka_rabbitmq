package com.posfarmacia.usecases.port.in.cliente;

import com.posfarmacia.usecases.dto.cliente.ActualizarClienteCommand;
import com.posfarmacia.domain.model.cliente.Cliente;
import java.util.UUID;

/** RF09: actualiza telefono, correo y/o direccion de un cliente ya registrado (endpoint PATCH /api/clientes/{id}). */
public interface ActualizarClienteUseCase {

    Cliente actualizar(UUID clienteId, ActualizarClienteCommand command);
}
