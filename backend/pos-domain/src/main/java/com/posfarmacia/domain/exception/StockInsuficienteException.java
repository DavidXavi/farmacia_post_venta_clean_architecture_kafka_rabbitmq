package com.posfarmacia.domain.exception;

public final class StockInsuficienteException extends DomainException {
    public StockInsuficienteException() {
        this("No hay stock vendible suficiente para la cantidad solicitada.");
    }

    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
