package com.posfarmacia.domain.valueobject;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import java.time.LocalDate;

public record FechaVencimiento(LocalDate valor) {

    public FechaVencimiento {
        if (valor == null) {
            throw new ValorInvalidoException("La fecha de vencimiento no puede ser nula.");
        }
    }

    public boolean estaVencida(LocalDate hoy) {
        return valor.isBefore(hoy);
    }

    /** RN36: bloquea venta si vence dentro de los proximos {@code mesesPreventivos} (default 3), salvo que ya este vencida. */
    public boolean estaEnPeriodoPreventivo(LocalDate hoy) {
        return estaEnPeriodoPreventivo(hoy, 3);
    }

    public boolean estaEnPeriodoPreventivo(LocalDate hoy, int mesesPreventivos) {
        return !estaVencida(hoy) && !valor.isAfter(hoy.plusMonths(mesesPreventivos));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
