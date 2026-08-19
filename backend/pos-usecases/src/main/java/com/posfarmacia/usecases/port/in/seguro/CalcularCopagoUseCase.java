package com.posfarmacia.usecases.port.in.seguro;

import com.posfarmacia.usecases.dto.seguro.CalcularCopagoCommand;
import com.posfarmacia.domain.service.seguro.ResultadoCopago;

/**
 * Puerto de entrada puro que expone {@code CalculadorCopago} para que otros contextos
 * (por ejemplo Ventas al confirmar una linea con convenio) calculen el copago sin
 * depender de la clase de dominio directamente.
 */
public interface CalcularCopagoUseCase {

    ResultadoCopago calcular(CalcularCopagoCommand command);
}
