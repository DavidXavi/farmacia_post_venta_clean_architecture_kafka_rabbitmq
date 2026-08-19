package com.posfarmacia.domain.valueobject;

import com.posfarmacia.domain.exception.ValorInvalidoException;

public record NumeroReceta(String valor) {

    public NumeroReceta {
        if (valor == null || valor.isBlank()) {
            throw new ValorInvalidoException("El numero de receta no puede ser vacio.");
        }
        valor = valor.trim();
    }

    @Override
    public String toString() {
        return valor;
    }
}
