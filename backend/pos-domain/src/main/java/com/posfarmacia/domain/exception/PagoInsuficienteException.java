package com.posfarmacia.domain.exception;

public final class PagoInsuficienteException extends DomainException {
    public PagoInsuficienteException() {
        this("El monto pagado no cubre el total que corresponde al cliente.");
    }

    public PagoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
