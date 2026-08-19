package com.posfarmacia.usecases.port.in.receta;

/**
 * Puerto de entrada para RF07: valida que una receta ampare la dispensacion de un
 * medicamento controlado (RN14-RN20). Ver {@link ValidarRecetaCommand} para el
 * significado de {@code ventaId}.
 */
public interface ValidarRecetaUseCase {

    ValidarRecetaResultado validar(ValidarRecetaCommand command);
}
