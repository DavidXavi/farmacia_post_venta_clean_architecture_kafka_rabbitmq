package com.posfarmacia.usecases.port.in.cliente;

import com.posfarmacia.domain.model.cliente.Cliente;

/** RF09: buscar al cliente por su DNI. */
public interface IdentificarClienteUseCase {

    Cliente identificarPorDni(String dni);
}
