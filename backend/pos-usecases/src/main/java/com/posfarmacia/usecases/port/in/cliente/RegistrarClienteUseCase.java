package com.posfarmacia.usecases.port.in.cliente;

import com.posfarmacia.usecases.dto.cliente.RegistrarClienteCommand;
import com.posfarmacia.domain.model.cliente.Cliente;

/** RF09: registrar los datos basicos de un cliente cuando aun no existe. */
public interface RegistrarClienteUseCase {

    Cliente registrar(RegistrarClienteCommand command);
}
