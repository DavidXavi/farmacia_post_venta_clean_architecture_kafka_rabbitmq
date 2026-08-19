package com.posfarmacia.domain.valueobject;

import com.posfarmacia.domain.exception.ValorInvalidoException;

public record CodigoLote(String valor) {

    public CodigoLote {
        if (valor == null || valor.isBlank()) {
            throw new ValorInvalidoException("El codigo de lote no puede ser vacio.");
        }
        valor = valor.trim();
    }

    @Override
    public String toString() {
        return valor;
    }
}
