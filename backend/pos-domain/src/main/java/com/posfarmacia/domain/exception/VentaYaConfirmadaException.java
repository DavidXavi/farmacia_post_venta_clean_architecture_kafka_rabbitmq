package com.posfarmacia.domain.exception;

public final class VentaYaConfirmadaException extends DomainException {
    public VentaYaConfirmadaException() {
        this("La venta ya fue confirmada y no puede modificarse.");
    }

    public VentaYaConfirmadaException(String mensaje) {
        super(mensaje);
    }
}
