package com.posfarmacia.usecases.port.in.receta;

import com.posfarmacia.domain.model.receta.Receta;

/**
 * Puerto de entrada: revision clinica de una receta (aprobar/rechazar) por el quimico
 * farmaceutico, paso obligatorio antes de que {@link ValidarRecetaUseCase} pueda
 * amparar su uso en una dispensacion (ver {@code ValidadorReceta.validarParaDispensacion},
 * que exige {@code EstadoReceta.APROBADA}).
 *
 * <p>Traducido de {@code ValidarRecetaUseCase} (.NET). Se renombra en Java para no
 * colisionar con {@link ValidarRecetaUseCase}, que en este backend cubre una
 * responsabilidad distinta (RF07: validar que la receta ampare una dispensacion
 * concreta), ya existente y sin relacion con esta revision clinica.
 */
public interface RevisarRecetaUseCase {

    Receta revisar(RevisarRecetaCommand command);
}
