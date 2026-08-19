package com.posfarmacia.usecases.usecase.credito;

import com.posfarmacia.usecases.dto.credito.RegistrarLineaCreditoCommand;
import com.posfarmacia.usecases.port.in.credito.RegistrarLineaCreditoUseCase;
import com.posfarmacia.usecases.port.out.cliente.ClienteRepositoryPort;
import com.posfarmacia.usecases.port.out.credito.LineaCreditoRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.exception.LineaCreditoInvalidaException;
import com.posfarmacia.domain.model.credito.LineaCredito;
import com.posfarmacia.domain.valueobject.Dinero;
import org.springframework.transaction.annotation.Transactional;

public class RegistrarLineaCreditoUseCaseImpl implements RegistrarLineaCreditoUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;
    private final LineaCreditoRepositoryPort lineaCreditoRepositoryPort;

    public RegistrarLineaCreditoUseCaseImpl(ClienteRepositoryPort clienteRepositoryPort,
                                             LineaCreditoRepositoryPort lineaCreditoRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.lineaCreditoRepositoryPort = lineaCreditoRepositoryPort;
    }

    @Override
    @Transactional
    public LineaCredito registrar(RegistrarLineaCreditoCommand command) {
        clienteRepositoryPort.buscarPorId(command.clienteId())
                .orElseThrow(() -> new EntidadNoEncontradaException("El cliente indicado no existe."));

        if (lineaCreditoRepositoryPort.buscarPorCliente(command.clienteId()).isPresent()) {
            throw new LineaCreditoInvalidaException("El cliente ya tiene una linea de credito registrada.");
        }

        LineaCredito linea = new LineaCredito(command.clienteId(), new Dinero(command.montoAutorizado()),
                command.vigenciaInicio(), command.vigenciaFin());
        return lineaCreditoRepositoryPort.guardar(linea);
    }
}
