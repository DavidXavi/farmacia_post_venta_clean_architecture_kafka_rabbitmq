package com.posfarmacia.domain.valueobject;

import com.posfarmacia.domain.exception.ValorInvalidoException;

public record CodigoProducto(String valor) {

    public CodigoProducto {
        if (valor == null || valor.isBlank()) {
            throw new ValorInvalidoException("El codigo de producto no puede ser vacio.");
        }
        valor = valor.trim();
    }

    @Override
    public String toString() {
        return valor;
    }
}
