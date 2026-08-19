package com.posfarmacia.usecases.port.in.receta;

import java.util.List;
import java.util.UUID;

/**
 * Puerto de entrada para consultar el historial de usos de una receta (capacidad del
 * quimico farmaceutico: "Consultar el historial de recetas validadas", Word seccion 3.3).
 */
public interface ConsultarHistorialRecetasUseCase {

    List<UsoRecetaView> consultarHistorial(UUID recetaId);
}
