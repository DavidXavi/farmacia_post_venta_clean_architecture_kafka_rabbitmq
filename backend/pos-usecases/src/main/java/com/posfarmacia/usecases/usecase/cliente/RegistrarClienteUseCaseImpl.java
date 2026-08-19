package com.posfarmacia.usecases.usecase.cliente;

import com.posfarmacia.usecases.dto.cliente.RegistrarClienteCommand;
import com.posfarmacia.usecases.port.in.cliente.RegistrarClienteUseCase;
import com.posfarmacia.usecases.port.out.cliente.ClienteCentralPort;
import com.posfarmacia.usecases.port.out.cliente.ClienteRepositoryPort;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.cliente.Cliente;
import com.posfarmacia.domain.valueobject.Dni;
import org.springframework.transaction.annotation.Transactional;

public class RegistrarClienteUseCaseImpl implements RegistrarClienteUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;
    private final ClienteCentralPort clienteCentralPort;

    public RegistrarClienteUseCaseImpl(ClienteRepositoryPort clienteRepositoryPort, ClienteCentralPort clienteCentralPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.clienteCentralPort = clienteCentralPort;
    }

    @Override
    @Transactional
    public Cliente registrar(RegistrarClienteCommand command) {
        Dni dni = new Dni(command.dni());
        if (clienteRepositoryPort.buscarPorDni(dni).isPresent()) {
            throw new ValorInvalidoException("Ya existe un cliente registrado con ese DNI.");
        }

        Cliente cliente = new Cliente(dni, command.nombres(), command.apellidos(), command.fechaNacimiento(),
                command.telefono(), command.correo(), command.direccion());

        Cliente guardado = clienteRepositoryPort.guardar(cliente);
        // Regla "c": el registro del cliente ante convenios se confirma en la central.
        clienteCentralPort.registrar(guardado);
        return guardado;
    }
}
