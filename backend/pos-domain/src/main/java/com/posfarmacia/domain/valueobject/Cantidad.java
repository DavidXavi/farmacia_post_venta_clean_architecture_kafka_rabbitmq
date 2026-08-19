package com.posfarmacia.domain.valueobject;

import com.posfarmacia.domain.exception.ValorInvalidoException;

public record Cantidad(int valor) implements Comparable<Cantidad> {

    public static final Cantidad CERO = new Cantidad(0);

    public Cantidad {
        if (valor < 0) {
            throw new ValorInvalidoException("La cantidad no puede ser negativa.");
        }
    }

    public Cantidad sumar(Cantidad otra) {
        return new Cantidad(this.valor + otra.valor);
    }

    public Cantidad restar(Cantidad otra) {
        return new Cantidad(this.valor - otra.valor);
    }

    public boolean esMayorQue(Cantidad otra) {
        return this.valor > otra.valor;
    }

    public boolean esMenorQue(Cantidad otra) {
        return this.valor < otra.valor;
    }

    public boolean esMayorOIgualQue(Cantidad otra) {
        return this.valor >= otra.valor;
    }

    public boolean esMenorOIgualQue(Cantidad otra) {
        return this.valor <= otra.valor;
    }

    @Override
    public int compareTo(Cantidad otra) {
        return Integer.compare(this.valor, otra.valor);
    }

    @Override
    public String toString() {
        return Integer.toString(valor);
    }
}
