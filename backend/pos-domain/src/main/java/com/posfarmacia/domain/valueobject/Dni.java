package com.posfarmacia.domain.valueobject;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import java.util.regex.Pattern;

public record Dni(String valor) {

    private static final Pattern DNI_PATTERN = Pattern.compile("^\\d{8}$");

    public Dni {
        if (valor == null || !DNI_PATTERN.matcher(valor).matches()) {
            throw new ValorInvalidoException("El DNI debe tener exactamente 8 digitos numericos.");
        }
    }

    @Override
    public String toString() {
        return valor;
    }
}
