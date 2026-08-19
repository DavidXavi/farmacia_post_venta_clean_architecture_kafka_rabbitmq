package com.posfarmacia.domain.exception;

public final class CajaCerradaException extends DomainException {
    public CajaCerradaException() {
        this("No se puede registrar una venta sin una sesion de caja abierta.");
    }

    public CajaCerradaException(String mensaje) {
        super(mensaje);
    }
}
