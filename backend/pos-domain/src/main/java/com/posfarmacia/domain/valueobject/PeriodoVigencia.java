package com.posfarmacia.domain.valueobject;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import java.time.LocalDate;

public record PeriodoVigencia(LocalDate inicio, LocalDate fin) {

    public PeriodoVigencia {
        if (inicio != null && fin != null && fin.isBefore(inicio)) {
            throw new ValorInvalidoException("La fecha de fin de vigencia no puede ser anterior a la de inicio.");
        }
    }

    public boolean estaVigente(LocalDate hoy) {
        boolean despuesDeInicio = inicio == null || !inicio.isAfter(hoy);
        boolean antesDeFin = fin == null || !fin.isBefore(hoy);
        return despuesDeInicio && antesDeFin;
    }
}
