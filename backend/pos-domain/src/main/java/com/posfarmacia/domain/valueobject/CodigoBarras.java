package com.posfarmacia.domain.valueobject;

import com.posfarmacia.domain.exception.ValorInvalidoException;

public record CodigoBarras(String valor) {

    public CodigoBarras {
        if (valor == null || valor.isBlank()) {
            throw new ValorInvalidoException("El codigo de barras no puede ser vacio.");
        }
        valor = valor.trim();
    }

    @Override
    public String toString() {
        return valor;
    }
}
