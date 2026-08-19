package com.posfarmacia.domain.model.promocion;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import java.util.UUID;

/**
 * Condicion de participacion de una {@link Promocion}: producto al que aplica el beneficio
 * (RN12 - productos participantes). Se persiste en la tabla {@code promocion_condiciones}.
 */
public record CondicionPromocion(UUID productoId) {

    public CondicionPromocion {
        if (productoId == null) {
            throw new ValorInvalidoException("El productoId de la condicion de promocion no puede ser nulo.");
        }
    }
}
