package com.posfarmacia.usecases.usecase.cliente;

import com.posfarmacia.usecases.port.in.cliente.ConsultarConveniosClienteUseCase;
import com.posfarmacia.usecases.port.out.cliente.ClienteRepositoryPort;
import com.posfarmacia.usecases.port.out.seguro.AfiliacionClienteRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.seguro.AfiliacionCliente;
import java.util.List;
import java.util.UUID;

public class ConsultarConveniosClienteUseCaseImpl implements ConsultarConveniosClienteUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;
    private final AfiliacionClienteRepositoryPort afiliacionClienteRepositoryPort;

    public ConsultarConveniosClienteUseCaseImpl(ClienteRepositoryPort clienteRepositoryPort,
                                                 AfiliacionClienteRepositoryPort afiliacionClienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.afiliacionClienteRepositoryPort = afiliacionClienteRepositoryPort;
    }

    @Override
    public List<AfiliacionCliente> consultarPorCliente(UUID clienteId) {
        clienteRepositoryPort.buscarPorId(clienteId)
                .orElseThrow(() -> new EntidadNoEncontradaException("El cliente indicado no existe."));
        return afiliacionClienteRepositoryPort.buscarPorCliente(clienteId);
    }
}
