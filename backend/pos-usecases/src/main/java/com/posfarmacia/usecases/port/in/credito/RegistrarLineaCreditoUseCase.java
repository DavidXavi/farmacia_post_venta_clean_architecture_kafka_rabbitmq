package com.posfarmacia.usecases.port.in.credito;

import com.posfarmacia.usecases.dto.credito.RegistrarLineaCreditoCommand;
import com.posfarmacia.domain.model.credito.LineaCredito;

/** RF11: registra una nueva linea de credito para un cliente (endpoint POST /api/lineas-credito). */
public interface RegistrarLineaCreditoUseCase {

    LineaCredito registrar(RegistrarLineaCreditoCommand command);
}
