package com.posfarmacia.domain.valueobject;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public record Dinero(BigDecimal monto) implements Comparable<Dinero> {

    public static final Dinero CERO = new Dinero(BigDecimal.ZERO);

    public Dinero {
        if (monto == null) {
            throw new ValorInvalidoException("El monto de dinero no puede ser nulo.");
        }
        if (monto.signum() < 0) {
            throw new ValorInvalidoException("El monto de dinero no puede ser negativo.");
        }
        monto = monto.setScale(2, RoundingMode.HALF_UP);
    }

    public static Dinero de(double monto) {
        return new Dinero(BigDecimal.valueOf(monto));
    }

    public Dinero sumar(Dinero otro) {
        return new Dinero(this.monto.add(otro.monto));
    }

    public Dinero restar(Dinero otro) {
        return new Dinero(this.monto.subtract(otro.monto));
    }

    public Dinero multiplicar(BigDecimal factor) {
        return new Dinero(this.monto.multiply(factor));
    }

    public boolean esMayorQue(Dinero otro) {
        return this.monto.compareTo(otro.monto) > 0;
    }

    public boolean esMenorQue(Dinero otro) {
        return this.monto.compareTo(otro.monto) < 0;
    }

    public boolean esMayorOIgualQue(Dinero otro) {
        return this.monto.compareTo(otro.monto) >= 0;
    }

    public boolean esMenorOIgualQue(Dinero otro) {
        return this.monto.compareTo(otro.monto) <= 0;
    }

    @Override
    public int compareTo(Dinero otro) {
        return this.monto.compareTo(otro.monto);
    }

    @Override
    public String toString() {
        return monto.toPlainString();
    }
}
