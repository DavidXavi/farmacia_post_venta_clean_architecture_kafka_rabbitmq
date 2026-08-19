package com.posfarmacia.usecases.usecase.credito;

import com.posfarmacia.usecases.dto.credito.ValidarLineaCreditoCommand;
import com.posfarmacia.usecases.port.in.credito.ValidarLineaCreditoUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.cliente.ClienteRepositoryPort;
import com.posfarmacia.usecases.port.out.credito.LineaCreditoRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.exception.LineaCreditoInvalidaException;
import com.posfarmacia.domain.model.cliente.Cliente;
import com.posfarmacia.domain.model.credito.LineaCredito;
import com.posfarmacia.domain.service.credito.ValidadorLineaCredito;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.Dni;

public class ValidarLineaCreditoUseCaseImpl implements ValidarLineaCreditoUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;
    private final LineaCreditoRepositoryPort lineaCreditoRepositoryPort;
    private final ValidadorLineaCredito validadorLineaCredito;
    private final ClockPort clockPort;

    public ValidarLineaCreditoUseCaseImpl(ClienteRepositoryPort clienteRepositoryPort,
                                           LineaCreditoRepositoryPort lineaCreditoRepositoryPort,
                                           ValidadorLineaCredito validadorLineaCredito,
                                           ClockPort clockPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.lineaCreditoRepositoryPort = lineaCreditoRepositoryPort;
        this.validadorLineaCredito = validadorLineaCredito;
        this.clockPort = clockPort;
    }

    @Override
    public LineaCredito validar(ValidarLineaCreditoCommand command) {
        // RN28: DNI obligatorio para realizar una compra a credito.
        Dni dni = new Dni(command.dni());
        Cliente cliente = clienteRepositoryPort.buscarPorDni(dni)
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe un cliente registrado con ese DNI."));

        LineaCredito lineaCredito = lineaCreditoRepositoryPort.buscarPorCliente(cliente.getId())
                .orElseThrow(() -> new LineaCreditoInvalidaException("El cliente no tiene una linea de credito registrada."));

        validadorLineaCredito.validarParaConsumo(lineaCredito, new Dinero(command.monto()), clockPort.hoy());
        return lineaCredito;
    }
}
