package com.posfarmacia.usecases.port.in.seguro;

import com.posfarmacia.usecases.dto.seguro.RegistrarAfiliacionCommand;
import com.posfarmacia.domain.model.seguro.AfiliacionCliente;

/** RF10: afilia a un cliente ya identificado por DNI a un convenio de seguro (endpoint POST /api/convenios/afiliaciones). */
public interface RegistrarAfiliacionUseCase {

    AfiliacionCliente registrar(RegistrarAfiliacionCommand command);
}
