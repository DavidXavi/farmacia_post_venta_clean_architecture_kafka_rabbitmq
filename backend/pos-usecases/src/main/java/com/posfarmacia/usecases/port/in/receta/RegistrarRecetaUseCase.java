package com.posfarmacia.usecases.port.in.receta;

import com.posfarmacia.domain.model.receta.Receta;

/**
 * Puerto de entrada: registra una receta nueva (aun sin validar), traducido de
 * {@code PosFarmacia.Application.UseCases.RegistrarRecetaUseCase} (.NET).
 */
public interface RegistrarRecetaUseCase {

    Receta registrar(RegistrarRecetaCommand command);
}
