package com.posfarmacia.usecases.dto.promocion;

import java.util.UUID;

/**
 * Entrada de {@code EvaluarPromocionesUseCase}: datos minimos de la linea de venta que se
 * quiere evaluar (RF06). El "hoy" no viaja aqui: el caso de uso lo obtiene de {@code ClockPort}.
 */
public record EvaluarPromocionesQuery(UUID productoId, int cantidad, boolean clienteIdentificado) {
}
