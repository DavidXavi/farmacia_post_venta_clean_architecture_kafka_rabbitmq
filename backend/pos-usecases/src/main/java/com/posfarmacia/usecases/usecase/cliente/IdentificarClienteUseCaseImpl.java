package com.posfarmacia.usecases.usecase.cliente;

import com.posfarmacia.usecases.port.in.cliente.IdentificarClienteUseCase;
import com.posfarmacia.usecases.port.out.cliente.ClienteRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.cliente.Cliente;
import com.posfarmacia.domain.valueobject.Dni;

public class IdentificarClienteUseCaseImpl implements IdentificarClienteUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;

    public IdentificarClienteUseCaseImpl(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    @Override
    public Cliente identificarPorDni(String dni) {
        return clienteRepositoryPort.buscarPorDni(new Dni(dni))
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe un cliente registrado con ese DNI."));
    }
}
