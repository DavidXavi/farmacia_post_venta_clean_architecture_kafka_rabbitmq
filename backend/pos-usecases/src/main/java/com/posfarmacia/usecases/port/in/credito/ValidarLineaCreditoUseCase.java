package com.posfarmacia.usecases.port.in.credito;

import com.posfarmacia.usecases.dto.credito.ValidarLineaCreditoCommand;
import com.posfarmacia.domain.model.credito.LineaCredito;

/**
 * RF11, RN28-RN30: valida que el cliente pueda financiar una compra a credito.
 * No consume el saldo (eso ocurre al confirmar la venta, responsabilidad del contexto
 * de Ventas usando {@code LineaCreditoRepositoryPort}/{@code MovimientoCreditoRepositoryPort}).
 */
public interface ValidarLineaCreditoUseCase {

    LineaCredito validar(ValidarLineaCreditoCommand command);
}
