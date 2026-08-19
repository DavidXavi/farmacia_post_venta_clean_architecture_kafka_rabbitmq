package com.posfarmacia.domain.valueobject;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public record Porcentaje(BigDecimal valor) {

    public static final Porcentaje CERO = new Porcentaje(BigDecimal.ZERO);

    public Porcentaje {
        if (valor == null) {
            throw new ValorInvalidoException("El porcentaje no puede ser nulo.");
        }
        if (valor.compareTo(BigDecimal.ZERO) < 0 || valor.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new ValorInvalidoException("El porcentaje debe estar entre 0 y 100.");
        }
    }

    public static Porcentaje de(double valor) {
        return new Porcentaje(BigDecimal.valueOf(valor));
    }

    public Dinero aplicarSobre(Dinero monto) {
        BigDecimal resultado = monto.monto()
                .multiply(valor)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return new Dinero(resultado);
    }

    @Override
    public String toString() {
        return valor.toPlainString() + "%";
    }
}
