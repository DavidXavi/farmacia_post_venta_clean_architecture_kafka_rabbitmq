package com.posfarmacia.domain.valueobject;

import com.posfarmacia.domain.exception.ValorInvalidoException;

public record CodigoAutorizacionSeguro(String valor) {

    public CodigoAutorizacionSeguro {
        if (valor == null || valor.isBlank()) {
            throw new ValorInvalidoException("El codigo de autorizacion del seguro no puede ser vacio.");
        }
        valor = valor.trim();
    }

    @Override
    public String toString() {
        return valor;
    }
}
