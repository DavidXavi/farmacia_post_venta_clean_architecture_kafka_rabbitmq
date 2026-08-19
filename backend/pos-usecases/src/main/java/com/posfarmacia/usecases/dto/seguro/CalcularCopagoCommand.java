package com.posfarmacia.usecases.dto.seguro;

import java.math.BigDecimal;

/**
 * Entrada pura para {@code CalcularCopagoUseCase}: pensado para que otros contextos
 * (por ejemplo Ventas) obtengan el copago de una linea ya con la cobertura resuelta,
 * sin depender de la consulta a la central.
 */
public record CalcularCopagoCommand(
        BigDecimal montoLinea,
        boolean convenioActivo,
        boolean afiliacionActivaYVigente,
        BigDecimal porcentajeCubierto) {
}
