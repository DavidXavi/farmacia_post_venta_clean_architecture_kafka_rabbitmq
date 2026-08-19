package com.posfarmacia.usecases.usecase.credito;

import com.posfarmacia.usecases.port.in.credito.ConsultarLineaCreditoClienteUseCase;
import com.posfarmacia.usecases.port.out.cliente.ClienteRepositoryPort;
import com.posfarmacia.usecases.port.out.credito.LineaCreditoRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.credito.LineaCredito;
import java.util.Optional;
import java.util.UUID;

public class ConsultarLineaCreditoClienteUseCaseImpl implements ConsultarLineaCreditoClienteUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;
    private final LineaCreditoRepositoryPort lineaCreditoRepositoryPort;

    public ConsultarLineaCreditoClienteUseCaseImpl(ClienteRepositoryPort clienteRepositoryPort,
                                                    LineaCreditoRepositoryPort lineaCreditoRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.lineaCreditoRepositoryPort = lineaCreditoRepositoryPort;
    }

    @Override
    public Optional<LineaCredito> consultarPorCliente(UUID clienteId) {
        clienteRepositoryPort.buscarPorId(clienteId)
                .orElseThrow(() -> new EntidadNoEncontradaException("El cliente indicado no existe."));
        return lineaCreditoRepositoryPort.buscarPorCliente(clienteId);
    }
}
