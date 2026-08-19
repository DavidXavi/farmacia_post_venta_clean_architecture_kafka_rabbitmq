package com.posfarmacia.domain.valueobject;

import com.posfarmacia.domain.exception.ValorInvalidoException;

public record NumeroComprobante(String serie, int correlativo) {

    public NumeroComprobante {
        if (serie == null || serie.isBlank()) {
            throw new ValorInvalidoException("La serie del comprobante no puede ser vacia.");
        }
        if (correlativo <= 0) {
            throw new ValorInvalidoException("El correlativo del comprobante debe ser mayor a cero.");
        }
        serie = serie.trim();
    }

    @Override
    public String toString() {
        return "%s-%08d".formatted(serie, correlativo);
    }
}
