package com.posfarmacia.usecases.usecase.cliente;

import com.posfarmacia.usecases.dto.cliente.ActualizarClienteCommand;
import com.posfarmacia.usecases.port.in.cliente.ActualizarClienteUseCase;
import com.posfarmacia.usecases.port.out.cliente.ClienteRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.cliente.Cliente;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public class ActualizarClienteUseCaseImpl implements ActualizarClienteUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;

    public ActualizarClienteUseCaseImpl(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    @Override
    @Transactional
    public Cliente actualizar(UUID clienteId, ActualizarClienteCommand command) {
        Cliente cliente = clienteRepositoryPort.buscarPorId(clienteId)
                .orElseThrow(() -> new EntidadNoEncontradaException("El cliente indicado no existe."));

        cliente.actualizarDatos(command.telefono(), command.correo(), command.direccion());
        return clienteRepositoryPort.guardar(cliente);
    }
}
